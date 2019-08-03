package com.sams.sams.web.rest;

import com.sams.sams.SamsApp;
import com.sams.sams.domain.Bo2mCar;
import com.sams.sams.domain.Bo2mOwner;
import com.sams.sams.repository.Bo2mCarRepository;
import com.sams.sams.service.Bo2mCarService;
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
 * Integration tests for the {@link Bo2mCarResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SamsApp.class)
public class Bo2mCarResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CREATED_AT = Instant.ofEpochMilli(-1L);

    @Autowired
    private Bo2mCarRepository bo2mCarRepository;

    @Autowired
    private Bo2mCarService bo2mCarService;

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

    private MockMvc restBo2mCarMockMvc;

    private Bo2mCar bo2mCar;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Bo2mCarResource bo2mCarResource = new Bo2mCarResource(bo2mCarService);
        this.restBo2mCarMockMvc = MockMvcBuilders.standaloneSetup(bo2mCarResource)
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
    public static Bo2mCar createEntity(EntityManager em) {
        Bo2mCar bo2mCar = new Bo2mCar()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Bo2mOwner bo2mOwner;
        if (TestUtil.findAll(em, Bo2mOwner.class).isEmpty()) {
            bo2mOwner = Bo2mOwnerResourceIT.createEntity(em);
            em.persist(bo2mOwner);
            em.flush();
        } else {
            bo2mOwner = TestUtil.findAll(em, Bo2mOwner.class).get(0);
        }
        bo2mCar.setBo2mOwner(bo2mOwner);
        return bo2mCar;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bo2mCar createUpdatedEntity(EntityManager em) {
        Bo2mCar bo2mCar = new Bo2mCar()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Bo2mOwner bo2mOwner;
        if (TestUtil.findAll(em, Bo2mOwner.class).isEmpty()) {
            bo2mOwner = Bo2mOwnerResourceIT.createUpdatedEntity(em);
            em.persist(bo2mOwner);
            em.flush();
        } else {
            bo2mOwner = TestUtil.findAll(em, Bo2mOwner.class).get(0);
        }
        bo2mCar.setBo2mOwner(bo2mOwner);
        return bo2mCar;
    }

    @BeforeEach
    public void initTest() {
        bo2mCar = createEntity(em);
    }

    @Test
    @Transactional
    public void createBo2mCar() throws Exception {
        int databaseSizeBeforeCreate = bo2mCarRepository.findAll().size();

        // Create the Bo2mCar
        restBo2mCarMockMvc.perform(post("/api/bo-2-m-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mCar)))
            .andExpect(status().isCreated());

        // Validate the Bo2mCar in the database
        List<Bo2mCar> bo2mCarList = bo2mCarRepository.findAll();
        assertThat(bo2mCarList).hasSize(databaseSizeBeforeCreate + 1);
        Bo2mCar testBo2mCar = bo2mCarList.get(bo2mCarList.size() - 1);
        assertThat(testBo2mCar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBo2mCar.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createBo2mCarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bo2mCarRepository.findAll().size();

        // Create the Bo2mCar with an existing ID
        bo2mCar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBo2mCarMockMvc.perform(post("/api/bo-2-m-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mCar)))
            .andExpect(status().isBadRequest());

        // Validate the Bo2mCar in the database
        List<Bo2mCar> bo2mCarList = bo2mCarRepository.findAll();
        assertThat(bo2mCarList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBo2mCars() throws Exception {
        // Initialize the database
        bo2mCarRepository.saveAndFlush(bo2mCar);

        // Get all the bo2mCarList
        restBo2mCarMockMvc.perform(get("/api/bo-2-m-cars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bo2mCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getBo2mCar() throws Exception {
        // Initialize the database
        bo2mCarRepository.saveAndFlush(bo2mCar);

        // Get the bo2mCar
        restBo2mCarMockMvc.perform(get("/api/bo-2-m-cars/{id}", bo2mCar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bo2mCar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBo2mCar() throws Exception {
        // Get the bo2mCar
        restBo2mCarMockMvc.perform(get("/api/bo-2-m-cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBo2mCar() throws Exception {
        // Initialize the database
        bo2mCarService.save(bo2mCar);

        int databaseSizeBeforeUpdate = bo2mCarRepository.findAll().size();

        // Update the bo2mCar
        Bo2mCar updatedBo2mCar = bo2mCarRepository.findById(bo2mCar.getId()).get();
        // Disconnect from session so that the updates on updatedBo2mCar are not directly saved in db
        em.detach(updatedBo2mCar);
        updatedBo2mCar
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT);

        restBo2mCarMockMvc.perform(put("/api/bo-2-m-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBo2mCar)))
            .andExpect(status().isOk());

        // Validate the Bo2mCar in the database
        List<Bo2mCar> bo2mCarList = bo2mCarRepository.findAll();
        assertThat(bo2mCarList).hasSize(databaseSizeBeforeUpdate);
        Bo2mCar testBo2mCar = bo2mCarList.get(bo2mCarList.size() - 1);
        assertThat(testBo2mCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBo2mCar.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingBo2mCar() throws Exception {
        int databaseSizeBeforeUpdate = bo2mCarRepository.findAll().size();

        // Create the Bo2mCar

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBo2mCarMockMvc.perform(put("/api/bo-2-m-cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bo2mCar)))
            .andExpect(status().isBadRequest());

        // Validate the Bo2mCar in the database
        List<Bo2mCar> bo2mCarList = bo2mCarRepository.findAll();
        assertThat(bo2mCarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBo2mCar() throws Exception {
        // Initialize the database
        bo2mCarService.save(bo2mCar);

        int databaseSizeBeforeDelete = bo2mCarRepository.findAll().size();

        // Delete the bo2mCar
        restBo2mCarMockMvc.perform(delete("/api/bo-2-m-cars/{id}", bo2mCar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bo2mCar> bo2mCarList = bo2mCarRepository.findAll();
        assertThat(bo2mCarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bo2mCar.class);
        Bo2mCar bo2mCar1 = new Bo2mCar();
        bo2mCar1.setId(1L);
        Bo2mCar bo2mCar2 = new Bo2mCar();
        bo2mCar2.setId(bo2mCar1.getId());
        assertThat(bo2mCar1).isEqualTo(bo2mCar2);
        bo2mCar2.setId(2L);
        assertThat(bo2mCar1).isNotEqualTo(bo2mCar2);
        bo2mCar1.setId(null);
        assertThat(bo2mCar1).isNotEqualTo(bo2mCar2);
    }
}
