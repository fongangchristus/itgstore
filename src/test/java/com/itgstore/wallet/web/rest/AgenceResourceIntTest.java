package com.itgstore.wallet.web.rest;

import com.itgstore.wallet.WalletApp;

import com.itgstore.wallet.domain.Agence;
import com.itgstore.wallet.domain.Operation;
import com.itgstore.wallet.domain.Operation;
import com.itgstore.wallet.repository.AgenceRepository;
import com.itgstore.wallet.service.AgenceService;
import com.itgstore.wallet.service.dto.AgenceDTO;
import com.itgstore.wallet.service.mapper.AgenceMapper;
import com.itgstore.wallet.web.rest.errors.ExceptionTranslator;
import com.itgstore.wallet.service.dto.AgenceCriteria;
import com.itgstore.wallet.service.AgenceQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.itgstore.wallet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AgenceResource REST controller.
 *
 * @see AgenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApp.class)
public class AgenceResourceIntTest {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private AgenceRepository agenceRepository;

    @Autowired
    private AgenceMapper agenceMapper;

    @Autowired
    private AgenceService agenceService;

    @Autowired
    private AgenceQueryService agenceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgenceMockMvc;

    private Agence agence;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgenceResource agenceResource = new AgenceResource(agenceService, agenceQueryService);
        this.restAgenceMockMvc = MockMvcBuilders.standaloneSetup(agenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agence createEntity(EntityManager em) {
        Agence agence = new Agence()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE);
        return agence;
    }

    @Before
    public void initTest() {
        agence = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgence() throws Exception {
        int databaseSizeBeforeCreate = agenceRepository.findAll().size();

        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);
        restAgenceMockMvc.perform(post("/api/agences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeCreate + 1);
        Agence testAgence = agenceList.get(agenceList.size() - 1);
        assertThat(testAgence.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAgence.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createAgenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agenceRepository.findAll().size();

        // Create the Agence with an existing ID
        agence.setId(1L);
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgenceMockMvc.perform(post("/api/agences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = agenceRepository.findAll().size();
        // set the field null
        agence.setCode(null);

        // Create the Agence, which fails.
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        restAgenceMockMvc.perform(post("/api/agences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenceDTO)))
            .andExpect(status().isBadRequest());

        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = agenceRepository.findAll().size();
        // set the field null
        agence.setLibelle(null);

        // Create the Agence, which fails.
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        restAgenceMockMvc.perform(post("/api/agences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenceDTO)))
            .andExpect(status().isBadRequest());

        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgences() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList
        restAgenceMockMvc.perform(get("/api/agences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agence.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
    
    @Test
    @Transactional
    public void getAgence() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get the agence
        restAgenceMockMvc.perform(get("/api/agences/{id}", agence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agence.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getAllAgencesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList where code equals to DEFAULT_CODE
        defaultAgenceShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the agenceList where code equals to UPDATED_CODE
        defaultAgenceShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAgencesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList where code in DEFAULT_CODE or UPDATED_CODE
        defaultAgenceShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the agenceList where code equals to UPDATED_CODE
        defaultAgenceShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAgencesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList where code is not null
        defaultAgenceShouldBeFound("code.specified=true");

        // Get all the agenceList where code is null
        defaultAgenceShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllAgencesByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList where code greater than or equals to DEFAULT_CODE
        defaultAgenceShouldBeFound("code.greaterOrEqualThan=" + DEFAULT_CODE);

        // Get all the agenceList where code greater than or equals to UPDATED_CODE
        defaultAgenceShouldNotBeFound("code.greaterOrEqualThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllAgencesByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList where code less than or equals to DEFAULT_CODE
        defaultAgenceShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the agenceList where code less than or equals to UPDATED_CODE
        defaultAgenceShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllAgencesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList where libelle equals to DEFAULT_LIBELLE
        defaultAgenceShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the agenceList where libelle equals to UPDATED_LIBELLE
        defaultAgenceShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllAgencesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultAgenceShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the agenceList where libelle equals to UPDATED_LIBELLE
        defaultAgenceShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllAgencesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList where libelle is not null
        defaultAgenceShouldBeFound("libelle.specified=true");

        // Get all the agenceList where libelle is null
        defaultAgenceShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllAgencesByOperationAgenceEmeteurIsEqualToSomething() throws Exception {
        // Initialize the database
        Operation operationAgenceEmeteur = OperationResourceIntTest.createEntity(em);
        em.persist(operationAgenceEmeteur);
        em.flush();
        agence.addOperationAgenceEmeteur(operationAgenceEmeteur);
        agenceRepository.saveAndFlush(agence);
        Long operationAgenceEmeteurId = operationAgenceEmeteur.getId();

        // Get all the agenceList where operationAgenceEmeteur equals to operationAgenceEmeteurId
        defaultAgenceShouldBeFound("operationAgenceEmeteurId.equals=" + operationAgenceEmeteurId);

        // Get all the agenceList where operationAgenceEmeteur equals to operationAgenceEmeteurId + 1
        defaultAgenceShouldNotBeFound("operationAgenceEmeteurId.equals=" + (operationAgenceEmeteurId + 1));
    }


    @Test
    @Transactional
    public void getAllAgencesByOperationAgencePayeurIsEqualToSomething() throws Exception {
        // Initialize the database
        Operation operationAgencePayeur = OperationResourceIntTest.createEntity(em);
        em.persist(operationAgencePayeur);
        em.flush();
        agence.addOperationAgencePayeur(operationAgencePayeur);
        agenceRepository.saveAndFlush(agence);
        Long operationAgencePayeurId = operationAgencePayeur.getId();

        // Get all the agenceList where operationAgencePayeur equals to operationAgencePayeurId
        defaultAgenceShouldBeFound("operationAgencePayeurId.equals=" + operationAgencePayeurId);

        // Get all the agenceList where operationAgencePayeur equals to operationAgencePayeurId + 1
        defaultAgenceShouldNotBeFound("operationAgencePayeurId.equals=" + (operationAgencePayeurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAgenceShouldBeFound(String filter) throws Exception {
        restAgenceMockMvc.perform(get("/api/agences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agence.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));

        // Check, that the count call also returns 1
        restAgenceMockMvc.perform(get("/api/agences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAgenceShouldNotBeFound(String filter) throws Exception {
        restAgenceMockMvc.perform(get("/api/agences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgenceMockMvc.perform(get("/api/agences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAgence() throws Exception {
        // Get the agence
        restAgenceMockMvc.perform(get("/api/agences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgence() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();

        // Update the agence
        Agence updatedAgence = agenceRepository.findById(agence.getId()).get();
        // Disconnect from session so that the updates on updatedAgence are not directly saved in db
        em.detach(updatedAgence);
        updatedAgence
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        AgenceDTO agenceDTO = agenceMapper.toDto(updatedAgence);

        restAgenceMockMvc.perform(put("/api/agences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenceDTO)))
            .andExpect(status().isOk());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
        Agence testAgence = agenceList.get(agenceList.size() - 1);
        assertThat(testAgence.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAgence.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAgence() throws Exception {
        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();

        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgenceMockMvc.perform(put("/api/agences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgence() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        int databaseSizeBeforeDelete = agenceRepository.findAll().size();

        // Get the agence
        restAgenceMockMvc.perform(delete("/api/agences/{id}", agence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agence.class);
        Agence agence1 = new Agence();
        agence1.setId(1L);
        Agence agence2 = new Agence();
        agence2.setId(agence1.getId());
        assertThat(agence1).isEqualTo(agence2);
        agence2.setId(2L);
        assertThat(agence1).isNotEqualTo(agence2);
        agence1.setId(null);
        assertThat(agence1).isNotEqualTo(agence2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgenceDTO.class);
        AgenceDTO agenceDTO1 = new AgenceDTO();
        agenceDTO1.setId(1L);
        AgenceDTO agenceDTO2 = new AgenceDTO();
        assertThat(agenceDTO1).isNotEqualTo(agenceDTO2);
        agenceDTO2.setId(agenceDTO1.getId());
        assertThat(agenceDTO1).isEqualTo(agenceDTO2);
        agenceDTO2.setId(2L);
        assertThat(agenceDTO1).isNotEqualTo(agenceDTO2);
        agenceDTO1.setId(null);
        assertThat(agenceDTO1).isNotEqualTo(agenceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(agenceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(agenceMapper.fromId(null)).isNull();
    }
}
