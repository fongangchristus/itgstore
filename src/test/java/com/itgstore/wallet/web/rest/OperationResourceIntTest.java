package com.itgstore.wallet.web.rest;

import com.itgstore.wallet.WalletApp;

import com.itgstore.wallet.domain.Operation;
import com.itgstore.wallet.domain.Transaction;
import com.itgstore.wallet.domain.Client;
import com.itgstore.wallet.domain.Device;
import com.itgstore.wallet.domain.Agence;
import com.itgstore.wallet.domain.Agence;
import com.itgstore.wallet.repository.OperationRepository;
import com.itgstore.wallet.service.OperationService;
import com.itgstore.wallet.service.dto.OperationDTO;
import com.itgstore.wallet.service.mapper.OperationMapper;
import com.itgstore.wallet.web.rest.errors.ExceptionTranslator;
import com.itgstore.wallet.service.dto.OperationCriteria;
import com.itgstore.wallet.service.OperationQueryService;

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

import com.itgstore.wallet.domain.enumeration.OperationType;
/**
 * Test class for the OperationResource REST controller.
 *
 * @see OperationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApp.class)
public class OperationResourceIntTest {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    private static final Instant DEFAULT_DATE_OPERATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OPERATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MESSAGE_RETOURT = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_RETOURT = "BBBBBBBBBB";

    private static final OperationType DEFAULT_OPERATION_TYPE = OperationType.RETRAIT;
    private static final OperationType UPDATED_OPERATION_TYPE = OperationType.RECHARGE_OM;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationQueryService operationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOperationMockMvc;

    private Operation operation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OperationResource operationResource = new OperationResource(operationService, operationQueryService);
        this.restOperationMockMvc = MockMvcBuilders.standaloneSetup(operationResource)
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
    public static Operation createEntity(EntityManager em) {
        Operation operation = new Operation()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .montant(DEFAULT_MONTANT)
            .dateOperation(DEFAULT_DATE_OPERATION)
            .messageRetourt(DEFAULT_MESSAGE_RETOURT)
            .operationType(DEFAULT_OPERATION_TYPE);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        operation.setClient(client);
        // Add required entity
        Device device = DeviceResourceIntTest.createEntity(em);
        em.persist(device);
        em.flush();
        operation.setDevice(device);
        // Add required entity
        Agence agence = AgenceResourceIntTest.createEntity(em);
        em.persist(agence);
        em.flush();
        operation.setAgencEmetteur(agence);
        // Add required entity
        operation.setAgencePayeur(agence);
        return operation;
    }

    @Before
    public void initTest() {
        operation = createEntity(em);
    }

    @Test
    @Transactional
    public void createOperation() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);
        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isCreated());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate + 1);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOperation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testOperation.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testOperation.getDateOperation()).isEqualTo(DEFAULT_DATE_OPERATION);
        assertThat(testOperation.getMessageRetourt()).isEqualTo(DEFAULT_MESSAGE_RETOURT);
        assertThat(testOperation.getOperationType()).isEqualTo(DEFAULT_OPERATION_TYPE);
    }

    @Test
    @Transactional
    public void createOperationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // Create the Operation with an existing ID
        operation.setId(1L);
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setCode(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setMontant(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setDateOperation(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageRetourtIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setMessageRetourt(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setOperationType(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOperations() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList
        restOperationMockMvc.perform(get("/api/operations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateOperation").value(hasItem(DEFAULT_DATE_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].messageRetourt").value(hasItem(DEFAULT_MESSAGE_RETOURT.toString())))
            .andExpect(jsonPath("$.[*].operationType").value(hasItem(DEFAULT_OPERATION_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get the operation
        restOperationMockMvc.perform(get("/api/operations/{id}", operation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(operation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.dateOperation").value(DEFAULT_DATE_OPERATION.toString()))
            .andExpect(jsonPath("$.messageRetourt").value(DEFAULT_MESSAGE_RETOURT.toString()))
            .andExpect(jsonPath("$.operationType").value(DEFAULT_OPERATION_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllOperationsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where code equals to DEFAULT_CODE
        defaultOperationShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the operationList where code equals to UPDATED_CODE
        defaultOperationShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOperationsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOperationShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the operationList where code equals to UPDATED_CODE
        defaultOperationShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOperationsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where code is not null
        defaultOperationShouldBeFound("code.specified=true");

        // Get all the operationList where code is null
        defaultOperationShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperationsByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where code greater than or equals to DEFAULT_CODE
        defaultOperationShouldBeFound("code.greaterOrEqualThan=" + DEFAULT_CODE);

        // Get all the operationList where code greater than or equals to UPDATED_CODE
        defaultOperationShouldNotBeFound("code.greaterOrEqualThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllOperationsByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where code less than or equals to DEFAULT_CODE
        defaultOperationShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the operationList where code less than or equals to UPDATED_CODE
        defaultOperationShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllOperationsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where libelle equals to DEFAULT_LIBELLE
        defaultOperationShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the operationList where libelle equals to UPDATED_LIBELLE
        defaultOperationShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllOperationsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultOperationShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the operationList where libelle equals to UPDATED_LIBELLE
        defaultOperationShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllOperationsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where libelle is not null
        defaultOperationShouldBeFound("libelle.specified=true");

        // Get all the operationList where libelle is null
        defaultOperationShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperationsByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where montant equals to DEFAULT_MONTANT
        defaultOperationShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the operationList where montant equals to UPDATED_MONTANT
        defaultOperationShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllOperationsByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultOperationShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the operationList where montant equals to UPDATED_MONTANT
        defaultOperationShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllOperationsByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where montant is not null
        defaultOperationShouldBeFound("montant.specified=true");

        // Get all the operationList where montant is null
        defaultOperationShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperationsByDateOperationIsEqualToSomething() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where dateOperation equals to DEFAULT_DATE_OPERATION
        defaultOperationShouldBeFound("dateOperation.equals=" + DEFAULT_DATE_OPERATION);

        // Get all the operationList where dateOperation equals to UPDATED_DATE_OPERATION
        defaultOperationShouldNotBeFound("dateOperation.equals=" + UPDATED_DATE_OPERATION);
    }

    @Test
    @Transactional
    public void getAllOperationsByDateOperationIsInShouldWork() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where dateOperation in DEFAULT_DATE_OPERATION or UPDATED_DATE_OPERATION
        defaultOperationShouldBeFound("dateOperation.in=" + DEFAULT_DATE_OPERATION + "," + UPDATED_DATE_OPERATION);

        // Get all the operationList where dateOperation equals to UPDATED_DATE_OPERATION
        defaultOperationShouldNotBeFound("dateOperation.in=" + UPDATED_DATE_OPERATION);
    }

    @Test
    @Transactional
    public void getAllOperationsByDateOperationIsNullOrNotNull() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where dateOperation is not null
        defaultOperationShouldBeFound("dateOperation.specified=true");

        // Get all the operationList where dateOperation is null
        defaultOperationShouldNotBeFound("dateOperation.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperationsByMessageRetourtIsEqualToSomething() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where messageRetourt equals to DEFAULT_MESSAGE_RETOURT
        defaultOperationShouldBeFound("messageRetourt.equals=" + DEFAULT_MESSAGE_RETOURT);

        // Get all the operationList where messageRetourt equals to UPDATED_MESSAGE_RETOURT
        defaultOperationShouldNotBeFound("messageRetourt.equals=" + UPDATED_MESSAGE_RETOURT);
    }

    @Test
    @Transactional
    public void getAllOperationsByMessageRetourtIsInShouldWork() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where messageRetourt in DEFAULT_MESSAGE_RETOURT or UPDATED_MESSAGE_RETOURT
        defaultOperationShouldBeFound("messageRetourt.in=" + DEFAULT_MESSAGE_RETOURT + "," + UPDATED_MESSAGE_RETOURT);

        // Get all the operationList where messageRetourt equals to UPDATED_MESSAGE_RETOURT
        defaultOperationShouldNotBeFound("messageRetourt.in=" + UPDATED_MESSAGE_RETOURT);
    }

    @Test
    @Transactional
    public void getAllOperationsByMessageRetourtIsNullOrNotNull() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where messageRetourt is not null
        defaultOperationShouldBeFound("messageRetourt.specified=true");

        // Get all the operationList where messageRetourt is null
        defaultOperationShouldNotBeFound("messageRetourt.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperationsByOperationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where operationType equals to DEFAULT_OPERATION_TYPE
        defaultOperationShouldBeFound("operationType.equals=" + DEFAULT_OPERATION_TYPE);

        // Get all the operationList where operationType equals to UPDATED_OPERATION_TYPE
        defaultOperationShouldNotBeFound("operationType.equals=" + UPDATED_OPERATION_TYPE);
    }

    @Test
    @Transactional
    public void getAllOperationsByOperationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where operationType in DEFAULT_OPERATION_TYPE or UPDATED_OPERATION_TYPE
        defaultOperationShouldBeFound("operationType.in=" + DEFAULT_OPERATION_TYPE + "," + UPDATED_OPERATION_TYPE);

        // Get all the operationList where operationType equals to UPDATED_OPERATION_TYPE
        defaultOperationShouldNotBeFound("operationType.in=" + UPDATED_OPERATION_TYPE);
    }

    @Test
    @Transactional
    public void getAllOperationsByOperationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList where operationType is not null
        defaultOperationShouldBeFound("operationType.specified=true");

        // Get all the operationList where operationType is null
        defaultOperationShouldNotBeFound("operationType.specified=false");
    }

    @Test
    @Transactional
    public void getAllOperationsByTransactionOperationIsEqualToSomething() throws Exception {
        // Initialize the database
        Transaction transactionOperation = TransactionResourceIntTest.createEntity(em);
        em.persist(transactionOperation);
        em.flush();
        operation.addTransactionOperation(transactionOperation);
        operationRepository.saveAndFlush(operation);
        Long transactionOperationId = transactionOperation.getId();

        // Get all the operationList where transactionOperation equals to transactionOperationId
        defaultOperationShouldBeFound("transactionOperationId.equals=" + transactionOperationId);

        // Get all the operationList where transactionOperation equals to transactionOperationId + 1
        defaultOperationShouldNotBeFound("transactionOperationId.equals=" + (transactionOperationId + 1));
    }


    @Test
    @Transactional
    public void getAllOperationsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        operation.setClient(client);
        operationRepository.saveAndFlush(operation);
        Long clientId = client.getId();

        // Get all the operationList where client equals to clientId
        defaultOperationShouldBeFound("clientId.equals=" + clientId);

        // Get all the operationList where client equals to clientId + 1
        defaultOperationShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }


    @Test
    @Transactional
    public void getAllOperationsByDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        Device device = DeviceResourceIntTest.createEntity(em);
        em.persist(device);
        em.flush();
        operation.setDevice(device);
        operationRepository.saveAndFlush(operation);
        Long deviceId = device.getId();

        // Get all the operationList where device equals to deviceId
        defaultOperationShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the operationList where device equals to deviceId + 1
        defaultOperationShouldNotBeFound("deviceId.equals=" + (deviceId + 1));
    }


    @Test
    @Transactional
    public void getAllOperationsByAgencEmetteurIsEqualToSomething() throws Exception {
        // Initialize the database
        Agence agencEmetteur = AgenceResourceIntTest.createEntity(em);
        em.persist(agencEmetteur);
        em.flush();
        operation.setAgencEmetteur(agencEmetteur);
        operationRepository.saveAndFlush(operation);
        Long agencEmetteurId = agencEmetteur.getId();

        // Get all the operationList where agencEmetteur equals to agencEmetteurId
        defaultOperationShouldBeFound("agencEmetteurId.equals=" + agencEmetteurId);

        // Get all the operationList where agencEmetteur equals to agencEmetteurId + 1
        defaultOperationShouldNotBeFound("agencEmetteurId.equals=" + (agencEmetteurId + 1));
    }


    @Test
    @Transactional
    public void getAllOperationsByAgencePayeurIsEqualToSomething() throws Exception {
        // Initialize the database
        Agence agencePayeur = AgenceResourceIntTest.createEntity(em);
        em.persist(agencePayeur);
        em.flush();
        operation.setAgencePayeur(agencePayeur);
        operationRepository.saveAndFlush(operation);
        Long agencePayeurId = agencePayeur.getId();

        // Get all the operationList where agencePayeur equals to agencePayeurId
        defaultOperationShouldBeFound("agencePayeurId.equals=" + agencePayeurId);

        // Get all the operationList where agencePayeur equals to agencePayeurId + 1
        defaultOperationShouldNotBeFound("agencePayeurId.equals=" + (agencePayeurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOperationShouldBeFound(String filter) throws Exception {
        restOperationMockMvc.perform(get("/api/operations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateOperation").value(hasItem(DEFAULT_DATE_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].messageRetourt").value(hasItem(DEFAULT_MESSAGE_RETOURT.toString())))
            .andExpect(jsonPath("$.[*].operationType").value(hasItem(DEFAULT_OPERATION_TYPE.toString())));

        // Check, that the count call also returns 1
        restOperationMockMvc.perform(get("/api/operations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOperationShouldNotBeFound(String filter) throws Exception {
        restOperationMockMvc.perform(get("/api/operations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperationMockMvc.perform(get("/api/operations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOperation() throws Exception {
        // Get the operation
        restOperationMockMvc.perform(get("/api/operations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation
        Operation updatedOperation = operationRepository.findById(operation.getId()).get();
        // Disconnect from session so that the updates on updatedOperation are not directly saved in db
        em.detach(updatedOperation);
        updatedOperation
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .montant(UPDATED_MONTANT)
            .dateOperation(UPDATED_DATE_OPERATION)
            .messageRetourt(UPDATED_MESSAGE_RETOURT)
            .operationType(UPDATED_OPERATION_TYPE);
        OperationDTO operationDTO = operationMapper.toDto(updatedOperation);

        restOperationMockMvc.perform(put("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOperation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOperation.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testOperation.getDateOperation()).isEqualTo(UPDATED_DATE_OPERATION);
        assertThat(testOperation.getMessageRetourt()).isEqualTo(UPDATED_MESSAGE_RETOURT);
        assertThat(testOperation.getOperationType()).isEqualTo(UPDATED_OPERATION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationMockMvc.perform(put("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        int databaseSizeBeforeDelete = operationRepository.findAll().size();

        // Get the operation
        restOperationMockMvc.perform(delete("/api/operations/{id}", operation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operation.class);
        Operation operation1 = new Operation();
        operation1.setId(1L);
        Operation operation2 = new Operation();
        operation2.setId(operation1.getId());
        assertThat(operation1).isEqualTo(operation2);
        operation2.setId(2L);
        assertThat(operation1).isNotEqualTo(operation2);
        operation1.setId(null);
        assertThat(operation1).isNotEqualTo(operation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationDTO.class);
        OperationDTO operationDTO1 = new OperationDTO();
        operationDTO1.setId(1L);
        OperationDTO operationDTO2 = new OperationDTO();
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
        operationDTO2.setId(operationDTO1.getId());
        assertThat(operationDTO1).isEqualTo(operationDTO2);
        operationDTO2.setId(2L);
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
        operationDTO1.setId(null);
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(operationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(operationMapper.fromId(null)).isNull();
    }
}
