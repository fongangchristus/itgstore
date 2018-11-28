package com.itgstore.wallet.web.rest;

import com.itgstore.wallet.WalletApp;

import com.itgstore.wallet.domain.Facture;
import com.itgstore.wallet.domain.Partenaire;
import com.itgstore.wallet.domain.Client;
import com.itgstore.wallet.domain.Device;
import com.itgstore.wallet.repository.FactureRepository;
import com.itgstore.wallet.service.FactureService;
import com.itgstore.wallet.service.dto.FactureDTO;
import com.itgstore.wallet.service.mapper.FactureMapper;
import com.itgstore.wallet.web.rest.errors.ExceptionTranslator;
import com.itgstore.wallet.service.dto.FactureCriteria;
import com.itgstore.wallet.service.FactureQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.itgstore.wallet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FactureResource REST controller.
 *
 * @see FactureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApp.class)
public class FactureResourceIntTest {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_EMISSION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EMISSION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_REGLEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REGLEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_ECHEANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ECHEANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureMapper factureMapper;

    @Autowired
    private FactureService factureService;

    @Autowired
    private FactureQueryService factureQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFactureMockMvc;

    private Facture facture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FactureResource factureResource = new FactureResource(factureService, factureQueryService);
        this.restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
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
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .reference(DEFAULT_REFERENCE)
            .libelle(DEFAULT_LIBELLE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateEmission(DEFAULT_DATE_EMISSION)
            .dateReglement(DEFAULT_DATE_REGLEMENT)
            .dateEcheance(DEFAULT_DATE_ECHEANCE)
            .montant(DEFAULT_MONTANT);
        // Add required entity
        Partenaire partenaire = PartenaireResourceIntTest.createEntity(em);
        em.persist(partenaire);
        em.flush();
        facture.setPartenaire(partenaire);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        facture.setClient(client);
        // Add required entity
        Device device = DeviceResourceIntTest.createEntity(em);
        em.persist(device);
        em.flush();
        facture.setDevice(device);
        return facture;
    }

    @Before
    public void initTest() {
        facture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testFacture.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testFacture.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testFacture.getDateEmission()).isEqualTo(DEFAULT_DATE_EMISSION);
        assertThat(testFacture.getDateReglement()).isEqualTo(DEFAULT_DATE_REGLEMENT);
        assertThat(testFacture.getDateEcheance()).isEqualTo(DEFAULT_DATE_ECHEANCE);
        assertThat(testFacture.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    public void createFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture with an existing ID
        facture.setId(1L);
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setReference(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setLibelle(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateCreation(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateEmissionIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateEmission(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateReglementIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateReglement(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateEcheanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateEcheance(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setMontant(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].dateReglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())))
            .andExpect(jsonPath("$.[*].dateEcheance").value(hasItem(DEFAULT_DATE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateEmission").value(DEFAULT_DATE_EMISSION.toString()))
            .andExpect(jsonPath("$.dateReglement").value(DEFAULT_DATE_REGLEMENT.toString()))
            .andExpect(jsonPath("$.dateEcheance").value(DEFAULT_DATE_ECHEANCE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllFacturesByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where reference equals to DEFAULT_REFERENCE
        defaultFactureShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the factureList where reference equals to UPDATED_REFERENCE
        defaultFactureShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllFacturesByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultFactureShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the factureList where reference equals to UPDATED_REFERENCE
        defaultFactureShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllFacturesByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where reference is not null
        defaultFactureShouldBeFound("reference.specified=true");

        // Get all the factureList where reference is null
        defaultFactureShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where libelle equals to DEFAULT_LIBELLE
        defaultFactureShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the factureList where libelle equals to UPDATED_LIBELLE
        defaultFactureShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllFacturesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultFactureShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the factureList where libelle equals to UPDATED_LIBELLE
        defaultFactureShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllFacturesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where libelle is not null
        defaultFactureShouldBeFound("libelle.specified=true");

        // Get all the factureList where libelle is null
        defaultFactureShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultFactureShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the factureList where dateCreation equals to UPDATED_DATE_CREATION
        defaultFactureShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultFactureShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the factureList where dateCreation equals to UPDATED_DATE_CREATION
        defaultFactureShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateCreation is not null
        defaultFactureShouldBeFound("dateCreation.specified=true");

        // Get all the factureList where dateCreation is null
        defaultFactureShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByDateEmissionIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission equals to DEFAULT_DATE_EMISSION
        defaultFactureShouldBeFound("dateEmission.equals=" + DEFAULT_DATE_EMISSION);

        // Get all the factureList where dateEmission equals to UPDATED_DATE_EMISSION
        defaultFactureShouldNotBeFound("dateEmission.equals=" + UPDATED_DATE_EMISSION);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateEmissionIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission in DEFAULT_DATE_EMISSION or UPDATED_DATE_EMISSION
        defaultFactureShouldBeFound("dateEmission.in=" + DEFAULT_DATE_EMISSION + "," + UPDATED_DATE_EMISSION);

        // Get all the factureList where dateEmission equals to UPDATED_DATE_EMISSION
        defaultFactureShouldNotBeFound("dateEmission.in=" + UPDATED_DATE_EMISSION);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateEmissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission is not null
        defaultFactureShouldBeFound("dateEmission.specified=true");

        // Get all the factureList where dateEmission is null
        defaultFactureShouldNotBeFound("dateEmission.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByDateReglementIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateReglement equals to DEFAULT_DATE_REGLEMENT
        defaultFactureShouldBeFound("dateReglement.equals=" + DEFAULT_DATE_REGLEMENT);

        // Get all the factureList where dateReglement equals to UPDATED_DATE_REGLEMENT
        defaultFactureShouldNotBeFound("dateReglement.equals=" + UPDATED_DATE_REGLEMENT);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateReglementIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateReglement in DEFAULT_DATE_REGLEMENT or UPDATED_DATE_REGLEMENT
        defaultFactureShouldBeFound("dateReglement.in=" + DEFAULT_DATE_REGLEMENT + "," + UPDATED_DATE_REGLEMENT);

        // Get all the factureList where dateReglement equals to UPDATED_DATE_REGLEMENT
        defaultFactureShouldNotBeFound("dateReglement.in=" + UPDATED_DATE_REGLEMENT);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateReglementIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateReglement is not null
        defaultFactureShouldBeFound("dateReglement.specified=true");

        // Get all the factureList where dateReglement is null
        defaultFactureShouldNotBeFound("dateReglement.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByDateEcheanceIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEcheance equals to DEFAULT_DATE_ECHEANCE
        defaultFactureShouldBeFound("dateEcheance.equals=" + DEFAULT_DATE_ECHEANCE);

        // Get all the factureList where dateEcheance equals to UPDATED_DATE_ECHEANCE
        defaultFactureShouldNotBeFound("dateEcheance.equals=" + UPDATED_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateEcheanceIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEcheance in DEFAULT_DATE_ECHEANCE or UPDATED_DATE_ECHEANCE
        defaultFactureShouldBeFound("dateEcheance.in=" + DEFAULT_DATE_ECHEANCE + "," + UPDATED_DATE_ECHEANCE);

        // Get all the factureList where dateEcheance equals to UPDATED_DATE_ECHEANCE
        defaultFactureShouldNotBeFound("dateEcheance.in=" + UPDATED_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateEcheanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEcheance is not null
        defaultFactureShouldBeFound("dateEcheance.specified=true");

        // Get all the factureList where dateEcheance is null
        defaultFactureShouldNotBeFound("dateEcheance.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant equals to DEFAULT_MONTANT
        defaultFactureShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the factureList where montant equals to UPDATED_MONTANT
        defaultFactureShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultFactureShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the factureList where montant equals to UPDATED_MONTANT
        defaultFactureShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllFacturesByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where montant is not null
        defaultFactureShouldBeFound("montant.specified=true");

        // Get all the factureList where montant is null
        defaultFactureShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByPartenaireIsEqualToSomething() throws Exception {
        // Initialize the database
        Partenaire partenaire = PartenaireResourceIntTest.createEntity(em);
        em.persist(partenaire);
        em.flush();
        facture.setPartenaire(partenaire);
        factureRepository.saveAndFlush(facture);
        Long partenaireId = partenaire.getId();

        // Get all the factureList where partenaire equals to partenaireId
        defaultFactureShouldBeFound("partenaireId.equals=" + partenaireId);

        // Get all the factureList where partenaire equals to partenaireId + 1
        defaultFactureShouldNotBeFound("partenaireId.equals=" + (partenaireId + 1));
    }


    @Test
    @Transactional
    public void getAllFacturesByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        facture.setClient(client);
        factureRepository.saveAndFlush(facture);
        Long clientId = client.getId();

        // Get all the factureList where client equals to clientId
        defaultFactureShouldBeFound("clientId.equals=" + clientId);

        // Get all the factureList where client equals to clientId + 1
        defaultFactureShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }


    @Test
    @Transactional
    public void getAllFacturesByDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        Device device = DeviceResourceIntTest.createEntity(em);
        em.persist(device);
        em.flush();
        facture.setDevice(device);
        factureRepository.saveAndFlush(facture);
        Long deviceId = device.getId();

        // Get all the factureList where device equals to deviceId
        defaultFactureShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the factureList where device equals to deviceId + 1
        defaultFactureShouldNotBeFound("deviceId.equals=" + (deviceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFactureShouldBeFound(String filter) throws Exception {
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].dateReglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())))
            .andExpect(jsonPath("$.[*].dateEcheance").value(hasItem(DEFAULT_DATE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));

        // Check, that the count call also returns 1
        restFactureMockMvc.perform(get("/api/factures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFactureShouldNotBeFound(String filter) throws Exception {
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactureMockMvc.perform(get("/api/factures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).get();
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture
            .reference(UPDATED_REFERENCE)
            .libelle(UPDATED_LIBELLE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateEmission(UPDATED_DATE_EMISSION)
            .dateReglement(UPDATED_DATE_REGLEMENT)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .montant(UPDATED_MONTANT);
        FactureDTO factureDTO = factureMapper.toDto(updatedFacture);

        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testFacture.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testFacture.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testFacture.getDateEmission()).isEqualTo(UPDATED_DATE_EMISSION);
        assertThat(testFacture.getDateReglement()).isEqualTo(UPDATED_DATE_REGLEMENT);
        assertThat(testFacture.getDateEcheance()).isEqualTo(UPDATED_DATE_ECHEANCE);
        assertThat(testFacture.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void updateNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Get the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
        Facture facture1 = new Facture();
        facture1.setId(1L);
        Facture facture2 = new Facture();
        facture2.setId(facture1.getId());
        assertThat(facture1).isEqualTo(facture2);
        facture2.setId(2L);
        assertThat(facture1).isNotEqualTo(facture2);
        facture1.setId(null);
        assertThat(facture1).isNotEqualTo(facture2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactureDTO.class);
        FactureDTO factureDTO1 = new FactureDTO();
        factureDTO1.setId(1L);
        FactureDTO factureDTO2 = new FactureDTO();
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
        factureDTO2.setId(factureDTO1.getId());
        assertThat(factureDTO1).isEqualTo(factureDTO2);
        factureDTO2.setId(2L);
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
        factureDTO1.setId(null);
        assertThat(factureDTO1).isNotEqualTo(factureDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(factureMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(factureMapper.fromId(null)).isNull();
    }
}
