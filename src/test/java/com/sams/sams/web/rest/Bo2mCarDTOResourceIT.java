package com.sams.sams.web.rest;

import com.sams.sams.SamsApp;
import com.sams.sams.domain.Bo2mCarDTO;
import com.sams.sams.domain.Bo2mOwnerDTO;
import com.sams.sams.repository.Bo2mCarDTORepository;
import com.sams.sams.service.Bo2mCarDTOService;
import com.sams.sams.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.sams.sams.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link Bo2mCarDTOResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SamsApp.class)
public class Bo2mCarDTOResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATED_AT = Instant.ofEpochMilli(-1L);

    @Autowired
    private Bo2mCarDTORepository bo2mCarDTORepository;

    @Autowired
    private Bo2mCarDTOService bo2mCarDTOService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restBo2mCarDTOMockMvc;

    private Bo2mCarDTO bo2mCarDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Bo2mCarDTOResource bo2mCarDTOResource = new Bo2mCarDTOResource(bo2mCarDTOService);
        this.restBo2mCarDTOMockMvc = MockMvcBuilders.standaloneSetup(bo2mCarDTOResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bo2mCarDTO createEntity(EntityManager em) {
        Bo2mCarDTO bo2mCarDTO = new Bo2mCarDTO()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Bo2mOwnerDTO bo2mOwnerDTO;
        if (TestUtil.findAll(em, Bo2mOwnerDTO.class).isEmpty()) {
            bo2mOwnerDTO = Bo2mOwnerDTOResourceIT.createEntity(em);
            em.persist(bo2mOwnerDTO);
            em.flush();
        } else {
            bo2mOwnerDTO = TestUtil.findAll(em, Bo2mOwnerDTO.class).get(0);
        }
        bo2mCarDTO.setBo2mOwnerDTO(bo2mOwnerDTO);
        return bo2mCarDTO;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bo2mCarDTO createUpdatedEntity(EntityManager em) {
        Bo2mCarDTO bo2mCarDTO = new Bo2mCarDTO()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Bo2mOwnerDTO bo2mOwnerDTO;
        if (TestUtil.findAll(em, Bo2mOwnerDTO.class).isEmpty()) {
            bo2mOwnerDTO = Bo2mOwnerDTOResourceIT.createUpdatedEntity(em);
            em.persist(bo2mOwnerDTO);
            em.flush();
        } else {
            bo2mOwnerDTO = TestUtil.findAll(em, Bo2mOwnerDTO.class).get(0);
        }
        bo2mCarDTO.setBo2mOwnerDTO(bo2mOwnerDTO);
        return bo2mCarDTO;
    }

    @BeforeEach
    public void initTest() {
        bo2mCarDTO = createEntity(em);
    }

    @Test
    @Transactional
    public void createBo2mCarDTO() throws Exception {
        int databaseSizeBeforeCreate = bo2mCarDTORepository.findAll().size();

        // Create the Bo2mCarDTO
        restBo2mCarDTOMockMvc.perform(post("/api/bo-2-m-car-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mCarDTO)))
            .andExpect(status().isCreated());

        // Validate the Bo2mCarDTO in the database
        List<Bo2mCarDTO> bo2mCarDTOList = bo2mCarDTORepository.findAll();
        assertThat(bo2mCarDTOList).hasSize(databaseSizeBeforeCreate + 1);
        Bo2mCarDTO testBo2mCarDTO = bo2mCarDTOList.get(bo2mCarDTOList.size() - 1);
        assertThat(testBo2mCarDTO.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBo2mCarDTO.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createBo2mCarDTOWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bo2mCarDTORepository.findAll().size();

        // Create the Bo2mCarDTO with an existing ID
        bo2mCarDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBo2mCarDTOMockMvc.perform(post("/api/bo-2-m-car-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mCarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bo2mCarDTO in the database
        List<Bo2mCarDTO> bo2mCarDTOList = bo2mCarDTORepository.findAll();
        assertThat(bo2mCarDTOList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBo2mCarDTOS() throws Exception {
        // Initialize the database
        bo2mCarDTORepository.saveAndFlush(bo2mCarDTO);

        // Get all the bo2mCarDTOList
        restBo2mCarDTOMockMvc.perform(get("/api/bo-2-m-car-dtos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bo2mCarDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getBo2mCarDTO() throws Exception {
        // Initialize the database
        bo2mCarDTORepository.saveAndFlush(bo2mCarDTO);

        // Get the bo2mCarDTO
        restBo2mCarDTOMockMvc.perform(get("/api/bo-2-m-car-dtos/{id}", bo2mCarDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bo2mCarDTO.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBo2mCarDTO() throws Exception {
        // Get the bo2mCarDTO
        restBo2mCarDTOMockMvc.perform(get("/api/bo-2-m-car-dtos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBo2mCarDTO() throws Exception {
        // Initialize the database
        bo2mCarDTOService.save(bo2mCarDTO);

        int databaseSizeBeforeUpdate = bo2mCarDTORepository.findAll().size();

        // Update the bo2mCarDTO
        Bo2mCarDTO updatedBo2mCarDTO = bo2mCarDTORepository.findById(bo2mCarDTO.getId()).get();
        // Disconnect from session so that the updates on updatedBo2mCarDTO are not directly saved in db
        em.detach(updatedBo2mCarDTO);
        updatedBo2mCarDTO
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT);

        restBo2mCarDTOMockMvc.perform(put("/api/bo-2-m-car-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBo2mCarDTO)))
            .andExpect(status().isOk());

        // Validate the Bo2mCarDTO in the database
        List<Bo2mCarDTO> bo2mCarDTOList = bo2mCarDTORepository.findAll();
        assertThat(bo2mCarDTOList).hasSize(databaseSizeBeforeUpdate);
        Bo2mCarDTO testBo2mCarDTO = bo2mCarDTOList.get(bo2mCarDTOList.size() - 1);
        assertThat(testBo2mCarDTO.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBo2mCarDTO.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingBo2mCarDTO() throws Exception {
        int databaseSizeBeforeUpdate = bo2mCarDTORepository.findAll().size();

        // Create the Bo2mCarDTO

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBo2mCarDTOMockMvc.perform(put("/api/bo-2-m-car-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mCarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bo2mCarDTO in the database
        List<Bo2mCarDTO> bo2mCarDTOList = bo2mCarDTORepository.findAll();
        assertThat(bo2mCarDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBo2mCarDTO() throws Exception {
        // Initialize the database
        bo2mCarDTOService.save(bo2mCarDTO);

        int databaseSizeBeforeDelete = bo2mCarDTORepository.findAll().size();

        // Delete the bo2mCarDTO
        restBo2mCarDTOMockMvc.perform(delete("/api/bo-2-m-car-dtos/{id}", bo2mCarDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bo2mCarDTO> bo2mCarDTOList = bo2mCarDTORepository.findAll();
        assertThat(bo2mCarDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bo2mCarDTO.class);
        Bo2mCarDTO bo2mCarDTO1 = new Bo2mCarDTO();
        bo2mCarDTO1.setId(1L);
        Bo2mCarDTO bo2mCarDTO2 = new Bo2mCarDTO();
        bo2mCarDTO2.setId(bo2mCarDTO1.getId());
        assertThat(bo2mCarDTO1).isEqualTo(bo2mCarDTO2);
        bo2mCarDTO2.setId(2L);
        assertThat(bo2mCarDTO1).isNotEqualTo(bo2mCarDTO2);
        bo2mCarDTO1.setId(null);
        assertThat(bo2mCarDTO1).isNotEqualTo(bo2mCarDTO2);
    }
}
