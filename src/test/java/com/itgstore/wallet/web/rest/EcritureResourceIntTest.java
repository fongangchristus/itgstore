package com.itgstore.wallet.web.rest;

import com.itgstore.wallet.WalletApp;

import com.itgstore.wallet.domain.Ecriture;
import com.itgstore.wallet.domain.Compte;
import com.itgstore.wallet.domain.Transaction;
import com.itgstore.wallet.repository.EcritureRepository;
import com.itgstore.wallet.service.EcritureService;
import com.itgstore.wallet.service.dto.EcritureDTO;
import com.itgstore.wallet.service.mapper.EcritureMapper;
import com.itgstore.wallet.web.rest.errors.ExceptionTranslator;
import com.itgstore.wallet.service.dto.EcritureCriteria;
import com.itgstore.wallet.service.EcritureQueryService;

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

import com.itgstore.wallet.domain.enumeration.SensEcriture;
/**
 * Test class for the EcritureResource REST controller.
 *
 * @see EcritureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApp.class)
public class EcritureResourceIntTest {

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRE_PARTIE = "AAAAAAAAAA";
    private static final String UPDATED_CONTRE_PARTIE = "BBBBBBBBBB";

    private static final SensEcriture DEFAULT_SENS_ECRITURE = SensEcriture.DEBIT;
    private static final SensEcriture UPDATED_SENS_ECRITURE = SensEcriture.CREDIT;

    @Autowired
    private EcritureRepository ecritureRepository;

    @Autowired
    private EcritureMapper ecritureMapper;

    @Autowired
    private EcritureService ecritureService;

    @Autowired
    private EcritureQueryService ecritureQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEcritureMockMvc;

    private Ecriture ecriture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EcritureResource ecritureResource = new EcritureResource(ecritureService, ecritureQueryService);
        this.restEcritureMockMvc = MockMvcBuilders.standaloneSetup(ecritureResource)
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
    public static Ecriture createEntity(EntityManager em) {
        Ecriture ecriture = new Ecriture()
            .montant(DEFAULT_MONTANT)
            .libelle(DEFAULT_LIBELLE)
            .contrePartie(DEFAULT_CONTRE_PARTIE)
            .sensEcriture(DEFAULT_SENS_ECRITURE);
        // Add required entity
        Compte compte = CompteResourceIntTest.createEntity(em);
        em.persist(compte);
        em.flush();
        ecriture.setCompte(compte);
        // Add required entity
        Transaction transaction = TransactionResourceIntTest.createEntity(em);
        em.persist(transaction);
        em.flush();
        ecriture.setTransaction(transaction);
        return ecriture;
    }

    @Before
    public void initTest() {
        ecriture = createEntity(em);
    }

    @Test
    @Transactional
    public void createEcriture() throws Exception {
        int databaseSizeBeforeCreate = ecritureRepository.findAll().size();

        // Create the Ecriture
        EcritureDTO ecritureDTO = ecritureMapper.toDto(ecriture);
        restEcritureMockMvc.perform(post("/api/ecritures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecritureDTO)))
            .andExpect(status().isCreated());

        // Validate the Ecriture in the database
        List<Ecriture> ecritureList = ecritureRepository.findAll();
        assertThat(ecritureList).hasSize(databaseSizeBeforeCreate + 1);
        Ecriture testEcriture = ecritureList.get(ecritureList.size() - 1);
        assertThat(testEcriture.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testEcriture.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testEcriture.getContrePartie()).isEqualTo(DEFAULT_CONTRE_PARTIE);
        assertThat(testEcriture.getSensEcriture()).isEqualTo(DEFAULT_SENS_ECRITURE);
    }

    @Test
    @Transactional
    public void createEcritureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ecritureRepository.findAll().size();

        // Create the Ecriture with an existing ID
        ecriture.setId(1L);
        EcritureDTO ecritureDTO = ecritureMapper.toDto(ecriture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEcritureMockMvc.perform(post("/api/ecritures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecritureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ecriture in the database
        List<Ecriture> ecritureList = ecritureRepository.findAll();
        assertThat(ecritureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecritureRepository.findAll().size();
        // set the field null
        ecriture.setMontant(null);

        // Create the Ecriture, which fails.
        EcritureDTO ecritureDTO = ecritureMapper.toDto(ecriture);

        restEcritureMockMvc.perform(post("/api/ecritures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecritureDTO)))
            .andExpect(status().isBadRequest());

        List<Ecriture> ecritureList = ecritureRepository.findAll();
        assertThat(ecritureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecritureRepository.findAll().size();
        // set the field null
        ecriture.setLibelle(null);

        // Create the Ecriture, which fails.
        EcritureDTO ecritureDTO = ecritureMapper.toDto(ecriture);

        restEcritureMockMvc.perform(post("/api/ecritures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecritureDTO)))
            .andExpect(status().isBadRequest());

        List<Ecriture> ecritureList = ecritureRepository.findAll();
        assertThat(ecritureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSensEcritureIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecritureRepository.findAll().size();
        // set the field null
        ecriture.setSensEcriture(null);

        // Create the Ecriture, which fails.
        EcritureDTO ecritureDTO = ecritureMapper.toDto(ecriture);

        restEcritureMockMvc.perform(post("/api/ecritures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecritureDTO)))
            .andExpect(status().isBadRequest());

        List<Ecriture> ecritureList = ecritureRepository.findAll();
        assertThat(ecritureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEcritures() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList
        restEcritureMockMvc.perform(get("/api/ecritures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ecriture.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].contrePartie").value(hasItem(DEFAULT_CONTRE_PARTIE.toString())))
            .andExpect(jsonPath("$.[*].sensEcriture").value(hasItem(DEFAULT_SENS_ECRITURE.toString())));
    }
    
    @Test
    @Transactional
    public void getEcriture() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get the ecriture
        restEcritureMockMvc.perform(get("/api/ecritures/{id}", ecriture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ecriture.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.contrePartie").value(DEFAULT_CONTRE_PARTIE.toString()))
            .andExpect(jsonPath("$.sensEcriture").value(DEFAULT_SENS_ECRITURE.toString()));
    }

    @Test
    @Transactional
    public void getAllEcrituresByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where montant equals to DEFAULT_MONTANT
        defaultEcritureShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the ecritureList where montant equals to UPDATED_MONTANT
        defaultEcritureShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllEcrituresByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultEcritureShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the ecritureList where montant equals to UPDATED_MONTANT
        defaultEcritureShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllEcrituresByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where montant is not null
        defaultEcritureShouldBeFound("montant.specified=true");

        // Get all the ecritureList where montant is null
        defaultEcritureShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    public void getAllEcrituresByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where libelle equals to DEFAULT_LIBELLE
        defaultEcritureShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the ecritureList where libelle equals to UPDATED_LIBELLE
        defaultEcritureShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllEcrituresByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultEcritureShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the ecritureList where libelle equals to UPDATED_LIBELLE
        defaultEcritureShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllEcrituresByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where libelle is not null
        defaultEcritureShouldBeFound("libelle.specified=true");

        // Get all the ecritureList where libelle is null
        defaultEcritureShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllEcrituresByContrePartieIsEqualToSomething() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where contrePartie equals to DEFAULT_CONTRE_PARTIE
        defaultEcritureShouldBeFound("contrePartie.equals=" + DEFAULT_CONTRE_PARTIE);

        // Get all the ecritureList where contrePartie equals to UPDATED_CONTRE_PARTIE
        defaultEcritureShouldNotBeFound("contrePartie.equals=" + UPDATED_CONTRE_PARTIE);
    }

    @Test
    @Transactional
    public void getAllEcrituresByContrePartieIsInShouldWork() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where contrePartie in DEFAULT_CONTRE_PARTIE or UPDATED_CONTRE_PARTIE
        defaultEcritureShouldBeFound("contrePartie.in=" + DEFAULT_CONTRE_PARTIE + "," + UPDATED_CONTRE_PARTIE);

        // Get all the ecritureList where contrePartie equals to UPDATED_CONTRE_PARTIE
        defaultEcritureShouldNotBeFound("contrePartie.in=" + UPDATED_CONTRE_PARTIE);
    }

    @Test
    @Transactional
    public void getAllEcrituresByContrePartieIsNullOrNotNull() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where contrePartie is not null
        defaultEcritureShouldBeFound("contrePartie.specified=true");

        // Get all the ecritureList where contrePartie is null
        defaultEcritureShouldNotBeFound("contrePartie.specified=false");
    }

    @Test
    @Transactional
    public void getAllEcrituresBySensEcritureIsEqualToSomething() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where sensEcriture equals to DEFAULT_SENS_ECRITURE
        defaultEcritureShouldBeFound("sensEcriture.equals=" + DEFAULT_SENS_ECRITURE);

        // Get all the ecritureList where sensEcriture equals to UPDATED_SENS_ECRITURE
        defaultEcritureShouldNotBeFound("sensEcriture.equals=" + UPDATED_SENS_ECRITURE);
    }

    @Test
    @Transactional
    public void getAllEcrituresBySensEcritureIsInShouldWork() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where sensEcriture in DEFAULT_SENS_ECRITURE or UPDATED_SENS_ECRITURE
        defaultEcritureShouldBeFound("sensEcriture.in=" + DEFAULT_SENS_ECRITURE + "," + UPDATED_SENS_ECRITURE);

        // Get all the ecritureList where sensEcriture equals to UPDATED_SENS_ECRITURE
        defaultEcritureShouldNotBeFound("sensEcriture.in=" + UPDATED_SENS_ECRITURE);
    }

    @Test
    @Transactional
    public void getAllEcrituresBySensEcritureIsNullOrNotNull() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        // Get all the ecritureList where sensEcriture is not null
        defaultEcritureShouldBeFound("sensEcriture.specified=true");

        // Get all the ecritureList where sensEcriture is null
        defaultEcritureShouldNotBeFound("sensEcriture.specified=false");
    }

    @Test
    @Transactional
    public void getAllEcrituresByCompteIsEqualToSomething() throws Exception {
        // Initialize the database
        Compte compte = CompteResourceIntTest.createEntity(em);
        em.persist(compte);
        em.flush();
        ecriture.setCompte(compte);
        ecritureRepository.saveAndFlush(ecriture);
        Long compteId = compte.getId();

        // Get all the ecritureList where compte equals to compteId
        defaultEcritureShouldBeFound("compteId.equals=" + compteId);

        // Get all the ecritureList where compte equals to compteId + 1
        defaultEcritureShouldNotBeFound("compteId.equals=" + (compteId + 1));
    }


    @Test
    @Transactional
    public void getAllEcrituresByTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        Transaction transaction = TransactionResourceIntTest.createEntity(em);
        em.persist(transaction);
        em.flush();
        ecriture.setTransaction(transaction);
        ecritureRepository.saveAndFlush(ecriture);
        Long transactionId = transaction.getId();

        // Get all the ecritureList where transaction equals to transactionId
        defaultEcritureShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the ecritureList where transaction equals to transactionId + 1
        defaultEcritureShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEcritureShouldBeFound(String filter) throws Exception {
        restEcritureMockMvc.perform(get("/api/ecritures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ecriture.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].contrePartie").value(hasItem(DEFAULT_CONTRE_PARTIE.toString())))
            .andExpect(jsonPath("$.[*].sensEcriture").value(hasItem(DEFAULT_SENS_ECRITURE.toString())));

        // Check, that the count call also returns 1
        restEcritureMockMvc.perform(get("/api/ecritures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEcritureShouldNotBeFound(String filter) throws Exception {
        restEcritureMockMvc.perform(get("/api/ecritures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEcritureMockMvc.perform(get("/api/ecritures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEcriture() throws Exception {
        // Get the ecriture
        restEcritureMockMvc.perform(get("/api/ecritures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEcriture() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        int databaseSizeBeforeUpdate = ecritureRepository.findAll().size();

        // Update the ecriture
        Ecriture updatedEcriture = ecritureRepository.findById(ecriture.getId()).get();
        // Disconnect from session so that the updates on updatedEcriture are not directly saved in db
        em.detach(updatedEcriture);
        updatedEcriture
            .montant(UPDATED_MONTANT)
            .libelle(UPDATED_LIBELLE)
            .contrePartie(UPDATED_CONTRE_PARTIE)
            .sensEcriture(UPDATED_SENS_ECRITURE);
        EcritureDTO ecritureDTO = ecritureMapper.toDto(updatedEcriture);

        restEcritureMockMvc.perform(put("/api/ecritures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecritureDTO)))
            .andExpect(status().isOk());

        // Validate the Ecriture in the database
        List<Ecriture> ecritureList = ecritureRepository.findAll();
        assertThat(ecritureList).hasSize(databaseSizeBeforeUpdate);
        Ecriture testEcriture = ecritureList.get(ecritureList.size() - 1);
        assertThat(testEcriture.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testEcriture.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEcriture.getContrePartie()).isEqualTo(UPDATED_CONTRE_PARTIE);
        assertThat(testEcriture.getSensEcriture()).isEqualTo(UPDATED_SENS_ECRITURE);
    }

    @Test
    @Transactional
    public void updateNonExistingEcriture() throws Exception {
        int databaseSizeBeforeUpdate = ecritureRepository.findAll().size();

        // Create the Ecriture
        EcritureDTO ecritureDTO = ecritureMapper.toDto(ecriture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEcritureMockMvc.perform(put("/api/ecritures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecritureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ecriture in the database
        List<Ecriture> ecritureList = ecritureRepository.findAll();
        assertThat(ecritureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEcriture() throws Exception {
        // Initialize the database
        ecritureRepository.saveAndFlush(ecriture);

        int databaseSizeBeforeDelete = ecritureRepository.findAll().size();

        // Get the ecriture
        restEcritureMockMvc.perform(delete("/api/ecritures/{id}", ecriture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ecriture> ecritureList = ecritureRepository.findAll();
        assertThat(ecritureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ecriture.class);
        Ecriture ecriture1 = new Ecriture();
        ecriture1.setId(1L);
        Ecriture ecriture2 = new Ecriture();
        ecriture2.setId(ecriture1.getId());
        assertThat(ecriture1).isEqualTo(ecriture2);
        ecriture2.setId(2L);
        assertThat(ecriture1).isNotEqualTo(ecriture2);
        ecriture1.setId(null);
        assertThat(ecriture1).isNotEqualTo(ecriture2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EcritureDTO.class);
        EcritureDTO ecritureDTO1 = new EcritureDTO();
        ecritureDTO1.setId(1L);
        EcritureDTO ecritureDTO2 = new EcritureDTO();
        assertThat(ecritureDTO1).isNotEqualTo(ecritureDTO2);
        ecritureDTO2.setId(ecritureDTO1.getId());
        assertThat(ecritureDTO1).isEqualTo(ecritureDTO2);
        ecritureDTO2.setId(2L);
        assertThat(ecritureDTO1).isNotEqualTo(ecritureDTO2);
        ecritureDTO1.setId(null);
        assertThat(ecritureDTO1).isNotEqualTo(ecritureDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ecritureMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ecritureMapper.fromId(null)).isNull();
    }
}
