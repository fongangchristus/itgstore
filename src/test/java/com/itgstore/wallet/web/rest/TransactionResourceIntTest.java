package com.itgstore.wallet.web.rest;

import com.itgstore.wallet.WalletApp;

import com.itgstore.wallet.domain.Transaction;
import com.itgstore.wallet.domain.Ecriture;
import com.itgstore.wallet.domain.Operation;
import com.itgstore.wallet.repository.TransactionRepository;
import com.itgstore.wallet.service.TransactionService;
import com.itgstore.wallet.service.dto.TransactionDTO;
import com.itgstore.wallet.service.mapper.TransactionMapper;
import com.itgstore.wallet.web.rest.errors.ExceptionTranslator;
import com.itgstore.wallet.service.dto.TransactionCriteria;
import com.itgstore.wallet.service.TransactionQueryService;

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
 * Test class for the TransactionResource REST controller.
 *
 * @see TransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApp.class)
public class TransactionResourceIntTest {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_TX = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TX = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionQueryService transactionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionResource transactionResource = new TransactionResource(transactionService, transactionQueryService);
        this.restTransactionMockMvc = MockMvcBuilders.standaloneSetup(transactionResource)
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
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .dateTx(DEFAULT_DATE_TX);
        // Add required entity
        Operation operation = OperationResourceIntTest.createEntity(em);
        em.persist(operation);
        em.flush();
        transaction.setOperation(operation);
        return transaction;
    }

    @Before
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTransaction.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTransaction.getDateTx()).isEqualTo(DEFAULT_DATE_TX);
    }

    @Test
    @Transactional
    public void createTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction with an existing ID
        transaction.setId(1L);
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setCode(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setLibelle(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateTxIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setDateTx(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].dateTx").value(hasItem(DEFAULT_DATE_TX.toString())));
    }
    
    @Test
    @Transactional
    public void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.dateTx").value(DEFAULT_DATE_TX.toString()));
    }

    @Test
    @Transactional
    public void getAllTransactionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where code equals to DEFAULT_CODE
        defaultTransactionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the transactionList where code equals to UPDATED_CODE
        defaultTransactionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultTransactionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the transactionList where code equals to UPDATED_CODE
        defaultTransactionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where code is not null
        defaultTransactionShouldBeFound("code.specified=true");

        // Get all the transactionList where code is null
        defaultTransactionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where code greater than or equals to DEFAULT_CODE
        defaultTransactionShouldBeFound("code.greaterOrEqualThan=" + DEFAULT_CODE);

        // Get all the transactionList where code greater than or equals to UPDATED_CODE
        defaultTransactionShouldNotBeFound("code.greaterOrEqualThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where code less than or equals to DEFAULT_CODE
        defaultTransactionShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the transactionList where code less than or equals to UPDATED_CODE
        defaultTransactionShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllTransactionsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where libelle equals to DEFAULT_LIBELLE
        defaultTransactionShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the transactionList where libelle equals to UPDATED_LIBELLE
        defaultTransactionShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultTransactionShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the transactionList where libelle equals to UPDATED_LIBELLE
        defaultTransactionShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where libelle is not null
        defaultTransactionShouldBeFound("libelle.specified=true");

        // Get all the transactionList where libelle is null
        defaultTransactionShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByDateTxIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateTx equals to DEFAULT_DATE_TX
        defaultTransactionShouldBeFound("dateTx.equals=" + DEFAULT_DATE_TX);

        // Get all the transactionList where dateTx equals to UPDATED_DATE_TX
        defaultTransactionShouldNotBeFound("dateTx.equals=" + UPDATED_DATE_TX);
    }

    @Test
    @Transactional
    public void getAllTransactionsByDateTxIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateTx in DEFAULT_DATE_TX or UPDATED_DATE_TX
        defaultTransactionShouldBeFound("dateTx.in=" + DEFAULT_DATE_TX + "," + UPDATED_DATE_TX);

        // Get all the transactionList where dateTx equals to UPDATED_DATE_TX
        defaultTransactionShouldNotBeFound("dateTx.in=" + UPDATED_DATE_TX);
    }

    @Test
    @Transactional
    public void getAllTransactionsByDateTxIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateTx is not null
        defaultTransactionShouldBeFound("dateTx.specified=true");

        // Get all the transactionList where dateTx is null
        defaultTransactionShouldNotBeFound("dateTx.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByEcritureTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        Ecriture ecritureTransaction = EcritureResourceIntTest.createEntity(em);
        em.persist(ecritureTransaction);
        em.flush();
        transaction.addEcritureTransaction(ecritureTransaction);
        transactionRepository.saveAndFlush(transaction);
        Long ecritureTransactionId = ecritureTransaction.getId();

        // Get all the transactionList where ecritureTransaction equals to ecritureTransactionId
        defaultTransactionShouldBeFound("ecritureTransactionId.equals=" + ecritureTransactionId);

        // Get all the transactionList where ecritureTransaction equals to ecritureTransactionId + 1
        defaultTransactionShouldNotBeFound("ecritureTransactionId.equals=" + (ecritureTransactionId + 1));
    }


    @Test
    @Transactional
    public void getAllTransactionsByOperationIsEqualToSomething() throws Exception {
        // Initialize the database
        Operation operation = OperationResourceIntTest.createEntity(em);
        em.persist(operation);
        em.flush();
        transaction.setOperation(operation);
        transactionRepository.saveAndFlush(transaction);
        Long operationId = operation.getId();

        // Get all the transactionList where operation equals to operationId
        defaultTransactionShouldBeFound("operationId.equals=" + operationId);

        // Get all the transactionList where operation equals to operationId + 1
        defaultTransactionShouldNotBeFound("operationId.equals=" + (operationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTransactionShouldBeFound(String filter) throws Exception {
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].dateTx").value(hasItem(DEFAULT_DATE_TX.toString())));

        // Check, that the count call also returns 1
        restTransactionMockMvc.perform(get("/api/transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTransactionShouldNotBeFound(String filter) throws Exception {
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionMockMvc.perform(get("/api/transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .dateTx(UPDATED_DATE_TX);
        TransactionDTO transactionDTO = transactionMapper.toDto(updatedTransaction);

        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTransaction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTransaction.getDateTx()).isEqualTo(UPDATED_DATE_TX);
    }

    @Test
    @Transactional
    public void updateNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Get the transaction
        restTransactionMockMvc.perform(delete("/api/transactions/{id}", transaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        Transaction transaction2 = new Transaction();
        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);
        transaction2.setId(2L);
        assertThat(transaction1).isNotEqualTo(transaction2);
        transaction1.setId(null);
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionDTO.class);
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setId(1L);
        TransactionDTO transactionDTO2 = new TransactionDTO();
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
        transactionDTO2.setId(transactionDTO1.getId());
        assertThat(transactionDTO1).isEqualTo(transactionDTO2);
        transactionDTO2.setId(2L);
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
        transactionDTO1.setId(null);
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionMapper.fromId(null)).isNull();
    }
}
