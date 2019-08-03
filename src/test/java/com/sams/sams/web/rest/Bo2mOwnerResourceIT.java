package com.sams.sams.web.rest;

import com.sams.sams.SamsApp;
import com.sams.sams.domain.Bo2mOwner;
import com.sams.sams.repository.Bo2mOwnerRepository;
import com.sams.sams.service.Bo2mOwnerService;
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
 * Integration tests for the {@link Bo2mOwnerResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SamsApp.class)
public class Bo2mOwnerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private Bo2mOwnerRepository bo2mOwnerRepository;

    @Autowired
    private Bo2mOwnerService bo2mOwnerService;

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

    private MockMvc restBo2mOwnerMockMvc;

    private Bo2mOwner bo2mOwner;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Bo2mOwnerResource bo2mOwnerResource = new Bo2mOwnerResource(bo2mOwnerService);
        this.restBo2mOwnerMockMvc = MockMvcBuilders.standaloneSetup(bo2mOwnerResource)
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
    public static Bo2mOwner createEntity(EntityManager em) {
        Bo2mOwner bo2mOwner = new Bo2mOwner()
            .name(DEFAULT_NAME);
        return bo2mOwner;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bo2mOwner createUpdatedEntity(EntityManager em) {
        Bo2mOwner bo2mOwner = new Bo2mOwner()
            .name(UPDATED_NAME);
        return bo2mOwner;
    }

    @BeforeEach
    public void initTest() {
        bo2mOwner = createEntity(em);
    }

    @Test
    @Transactional
    public void createBo2mOwner() throws Exception {
        int databaseSizeBeforeCreate = bo2mOwnerRepository.findAll().size();

        // Create the Bo2mOwner
        restBo2mOwnerMockMvc.perform(post("/api/bo-2-m-owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mOwner)))
            .andExpect(status().isCreated());

        // Validate the Bo2mOwner in the database
        List<Bo2mOwner> bo2mOwnerList = bo2mOwnerRepository.findAll();
        assertThat(bo2mOwnerList).hasSize(databaseSizeBeforeCreate + 1);
        Bo2mOwner testBo2mOwner = bo2mOwnerList.get(bo2mOwnerList.size() - 1);
        assertThat(testBo2mOwner.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBo2mOwnerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bo2mOwnerRepository.findAll().size();

        // Create the Bo2mOwner with an existing ID
        bo2mOwner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBo2mOwnerMockMvc.perform(post("/api/bo-2-m-owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mOwner)))
            .andExpect(status().isBadRequest());

        // Validate the Bo2mOwner in the database
        List<Bo2mOwner> bo2mOwnerList = bo2mOwnerRepository.findAll();
        assertThat(bo2mOwnerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBo2mOwners() throws Exception {
        // Initialize the database
        bo2mOwnerRepository.saveAndFlush(bo2mOwner);

        // Get all the bo2mOwnerList
        restBo2mOwnerMockMvc.perform(get("/api/bo-2-m-owners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bo2mOwner.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getBo2mOwner() throws Exception {
        // Initialize the database
        bo2mOwnerRepository.saveAndFlush(bo2mOwner);

        // Get the bo2mOwner
        restBo2mOwnerMockMvc.perform(get("/api/bo-2-m-owners/{id}", bo2mOwner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bo2mOwner.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBo2mOwner() throws Exception {
        // Get the bo2mOwner
        restBo2mOwnerMockMvc.perform(get("/api/bo-2-m-owners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBo2mOwner() throws Exception {
        // Initialize the database
        bo2mOwnerService.save(bo2mOwner);

        int databaseSizeBeforeUpdate = bo2mOwnerRepository.findAll().size();

        // Update the bo2mOwner
        Bo2mOwner updatedBo2mOwner = bo2mOwnerRepository.findById(bo2mOwner.getId()).get();
        // Disconnect from session so that the updates on updatedBo2mOwner are not directly saved in db
        em.detach(updatedBo2mOwner);
        updatedBo2mOwner
            .name(UPDATED_NAME);

        restBo2mOwnerMockMvc.perform(put("/api/bo-2-m-owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBo2mOwner)))
            .andExpect(status().isOk());

        // Validate the Bo2mOwner in the database
        List<Bo2mOwner> bo2mOwnerList = bo2mOwnerRepository.findAll();
        assertThat(bo2mOwnerList).hasSize(databaseSizeBeforeUpdate);
        Bo2mOwner testBo2mOwner = bo2mOwnerList.get(bo2mOwnerList.size() - 1);
        assertThat(testBo2mOwner.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBo2mOwner() throws Exception {
        int databaseSizeBeforeUpdate = bo2mOwnerRepository.findAll().size();

        // Create the Bo2mOwner

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBo2mOwnerMockMvc.perform(put("/api/bo-2-m-owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mOwner)))
            .andExpect(status().isBadRequest());

        // Validate the Bo2mOwner in the database
        List<Bo2mOwner> bo2mOwnerList = bo2mOwnerRepository.findAll();
        assertThat(bo2mOwnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBo2mOwner() throws Exception {
        // Initialize the database
        bo2mOwnerService.save(bo2mOwner);

        int databaseSizeBeforeDelete = bo2mOwnerRepository.findAll().size();

        // Delete the bo2mOwner
        restBo2mOwnerMockMvc.perform(delete("/api/bo-2-m-owners/{id}", bo2mOwner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bo2mOwner> bo2mOwnerList = bo2mOwnerRepository.findAll();
        assertThat(bo2mOwnerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bo2mOwner.class);
        Bo2mOwner bo2mOwner1 = new Bo2mOwner();
        bo2mOwner1.setId(1L);
        Bo2mOwner bo2mOwner2 = new Bo2mOwner();
        bo2mOwner2.setId(bo2mOwner1.getId());
        assertThat(bo2mOwner1).isEqualTo(bo2mOwner2);
        bo2mOwner2.setId(2L);
        assertThat(bo2mOwner1).isNotEqualTo(bo2mOwner2);
        bo2mOwner1.setId(null);
        assertThat(bo2mOwner1).isNotEqualTo(bo2mOwner2);
    }
}
