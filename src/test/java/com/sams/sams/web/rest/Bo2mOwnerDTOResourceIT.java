package com.sams.sams.web.rest;

import com.sams.sams.SamsApp;
import com.sams.sams.domain.Bo2mOwnerDTO;
import com.sams.sams.repository.Bo2mOwnerDTORepository;
import com.sams.sams.service.Bo2mOwnerDTOService;
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
import java.util.List;

import static com.sams.sams.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link Bo2mOwnerDTOResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SamsApp.class)
public class Bo2mOwnerDTOResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private Bo2mOwnerDTORepository bo2mOwnerDTORepository;

    @Autowired
    private Bo2mOwnerDTOService bo2mOwnerDTOService;

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

    private MockMvc restBo2mOwnerDTOMockMvc;

    private Bo2mOwnerDTO bo2mOwnerDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Bo2mOwnerDTOResource bo2mOwnerDTOResource = new Bo2mOwnerDTOResource(bo2mOwnerDTOService);
        this.restBo2mOwnerDTOMockMvc = MockMvcBuilders.standaloneSetup(bo2mOwnerDTOResource)
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
    public static Bo2mOwnerDTO createEntity(EntityManager em) {
        Bo2mOwnerDTO bo2mOwnerDTO = new Bo2mOwnerDTO()
            .name(DEFAULT_NAME);
        return bo2mOwnerDTO;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bo2mOwnerDTO createUpdatedEntity(EntityManager em) {
        Bo2mOwnerDTO bo2mOwnerDTO = new Bo2mOwnerDTO()
            .name(UPDATED_NAME);
        return bo2mOwnerDTO;
    }

    @BeforeEach
    public void initTest() {
        bo2mOwnerDTO = createEntity(em);
    }

    @Test
    @Transactional
    public void createBo2mOwnerDTO() throws Exception {
        int databaseSizeBeforeCreate = bo2mOwnerDTORepository.findAll().size();

        // Create the Bo2mOwnerDTO
        restBo2mOwnerDTOMockMvc.perform(post("/api/bo-2-m-owner-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mOwnerDTO)))
            .andExpect(status().isCreated());

        // Validate the Bo2mOwnerDTO in the database
        List<Bo2mOwnerDTO> bo2mOwnerDTOList = bo2mOwnerDTORepository.findAll();
        assertThat(bo2mOwnerDTOList).hasSize(databaseSizeBeforeCreate + 1);
        Bo2mOwnerDTO testBo2mOwnerDTO = bo2mOwnerDTOList.get(bo2mOwnerDTOList.size() - 1);
        assertThat(testBo2mOwnerDTO.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBo2mOwnerDTOWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bo2mOwnerDTORepository.findAll().size();

        // Create the Bo2mOwnerDTO with an existing ID
        bo2mOwnerDTO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBo2mOwnerDTOMockMvc.perform(post("/api/bo-2-m-owner-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mOwnerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bo2mOwnerDTO in the database
        List<Bo2mOwnerDTO> bo2mOwnerDTOList = bo2mOwnerDTORepository.findAll();
        assertThat(bo2mOwnerDTOList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBo2mOwnerDTOS() throws Exception {
        // Initialize the database
        bo2mOwnerDTORepository.saveAndFlush(bo2mOwnerDTO);

        // Get all the bo2mOwnerDTOList
        restBo2mOwnerDTOMockMvc.perform(get("/api/bo-2-m-owner-dtos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bo2mOwnerDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getBo2mOwnerDTO() throws Exception {
        // Initialize the database
        bo2mOwnerDTORepository.saveAndFlush(bo2mOwnerDTO);

        // Get the bo2mOwnerDTO
        restBo2mOwnerDTOMockMvc.perform(get("/api/bo-2-m-owner-dtos/{id}", bo2mOwnerDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bo2mOwnerDTO.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBo2mOwnerDTO() throws Exception {
        // Get the bo2mOwnerDTO
        restBo2mOwnerDTOMockMvc.perform(get("/api/bo-2-m-owner-dtos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBo2mOwnerDTO() throws Exception {
        // Initialize the database
        bo2mOwnerDTOService.save(bo2mOwnerDTO);

        int databaseSizeBeforeUpdate = bo2mOwnerDTORepository.findAll().size();

        // Update the bo2mOwnerDTO
        Bo2mOwnerDTO updatedBo2mOwnerDTO = bo2mOwnerDTORepository.findById(bo2mOwnerDTO.getId()).get();
        // Disconnect from session so that the updates on updatedBo2mOwnerDTO are not directly saved in db
        em.detach(updatedBo2mOwnerDTO);
        updatedBo2mOwnerDTO
            .name(UPDATED_NAME);

        restBo2mOwnerDTOMockMvc.perform(put("/api/bo-2-m-owner-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBo2mOwnerDTO)))
            .andExpect(status().isOk());

        // Validate the Bo2mOwnerDTO in the database
        List<Bo2mOwnerDTO> bo2mOwnerDTOList = bo2mOwnerDTORepository.findAll();
        assertThat(bo2mOwnerDTOList).hasSize(databaseSizeBeforeUpdate);
        Bo2mOwnerDTO testBo2mOwnerDTO = bo2mOwnerDTOList.get(bo2mOwnerDTOList.size() - 1);
        assertThat(testBo2mOwnerDTO.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBo2mOwnerDTO() throws Exception {
        int databaseSizeBeforeUpdate = bo2mOwnerDTORepository.findAll().size();

        // Create the Bo2mOwnerDTO

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBo2mOwnerDTOMockMvc.perform(put("/api/bo-2-m-owner-dtos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mOwnerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bo2mOwnerDTO in the database
        List<Bo2mOwnerDTO> bo2mOwnerDTOList = bo2mOwnerDTORepository.findAll();
        assertThat(bo2mOwnerDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBo2mOwnerDTO() throws Exception {
        // Initialize the database
        bo2mOwnerDTOService.save(bo2mOwnerDTO);

        int databaseSizeBeforeDelete = bo2mOwnerDTORepository.findAll().size();

        // Delete the bo2mOwnerDTO
        restBo2mOwnerDTOMockMvc.perform(delete("/api/bo-2-m-owner-dtos/{id}", bo2mOwnerDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bo2mOwnerDTO> bo2mOwnerDTOList = bo2mOwnerDTORepository.findAll();
        assertThat(bo2mOwnerDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bo2mOwnerDTO.class);
        Bo2mOwnerDTO bo2mOwnerDTO1 = new Bo2mOwnerDTO();
        bo2mOwnerDTO1.setId(1L);
        Bo2mOwnerDTO bo2mOwnerDTO2 = new Bo2mOwnerDTO();
        bo2mOwnerDTO2.setId(bo2mOwnerDTO1.getId());
        assertThat(bo2mOwnerDTO1).isEqualTo(bo2mOwnerDTO2);
        bo2mOwnerDTO2.setId(2L);
        assertThat(bo2mOwnerDTO1).isNotEqualTo(bo2mOwnerDTO2);
        bo2mOwnerDTO1.setId(null);
        assertThat(bo2mOwnerDTO1).isNotEqualTo(bo2mOwnerDTO2);
    }
}
