package com.itgstore.wallet.web.rest;

import com.itgstore.wallet.WalletApp;

import com.itgstore.wallet.domain.Compte;
import com.itgstore.wallet.domain.Client;
import com.itgstore.wallet.domain.Ecriture;
import com.itgstore.wallet.repository.CompteRepository;
import com.itgstore.wallet.service.CompteService;
import com.itgstore.wallet.service.dto.CompteDTO;
import com.itgstore.wallet.service.mapper.CompteMapper;
import com.itgstore.wallet.web.rest.errors.ExceptionTranslator;
import com.itgstore.wallet.service.dto.CompteCriteria;
import com.itgstore.wallet.service.CompteQueryService;

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
 * Test class for the CompteResource REST controller.
 *
 * @see CompteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApp.class)
public class CompteResourceIntTest {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEBIT = false;
    private static final Boolean UPDATED_IS_DEBIT = true;

    private static final Boolean DEFAULT_IS_CREDIT = false;
    private static final Boolean UPDATED_IS_CREDIT = true;

    private static final Boolean DEFAULT_IS_DEBITEUR = false;
    private static final Boolean UPDATED_IS_DEBITEUR = true;

    private static final Boolean DEFAULT_IS_CREDITEUR = false;
    private static final Boolean UPDATED_IS_CREDITEUR = true;

    private static final Float DEFAULT_SOLDE_DEBIT = 1F;
    private static final Float UPDATED_SOLDE_DEBIT = 2F;

    private static final Float DEFAULT_SOLDE_CREDIT = 1F;
    private static final Float UPDATED_SOLDE_CREDIT = 2F;

    private static final Float DEFAULT_BALANCE = 1F;
    private static final Float UPDATED_BALANCE = 2F;

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private CompteMapper compteMapper;

    @Autowired
    private CompteService compteService;

    @Autowired
    private CompteQueryService compteQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompteMockMvc;

    private Compte compte;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompteResource compteResource = new CompteResource(compteService, compteQueryService);
        this.restCompteMockMvc = MockMvcBuilders.standaloneSetup(compteResource)
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
    public static Compte createEntity(EntityManager em) {
        Compte compte = new Compte()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .isDebit(DEFAULT_IS_DEBIT)
            .isCredit(DEFAULT_IS_CREDIT)
            .isDebiteur(DEFAULT_IS_DEBITEUR)
            .isCrediteur(DEFAULT_IS_CREDITEUR)
            .soldeDebit(DEFAULT_SOLDE_DEBIT)
            .soldeCredit(DEFAULT_SOLDE_CREDIT)
            .balance(DEFAULT_BALANCE);
        return compte;
    }

    @Before
    public void initTest() {
        compte = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompte() throws Exception {
        int databaseSizeBeforeCreate = compteRepository.findAll().size();

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);
        restCompteMockMvc.perform(post("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isCreated());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeCreate + 1);
        Compte testCompte = compteList.get(compteList.size() - 1);
        assertThat(testCompte.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCompte.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCompte.isIsDebit()).isEqualTo(DEFAULT_IS_DEBIT);
        assertThat(testCompte.isIsCredit()).isEqualTo(DEFAULT_IS_CREDIT);
        assertThat(testCompte.isIsDebiteur()).isEqualTo(DEFAULT_IS_DEBITEUR);
        assertThat(testCompte.isIsCrediteur()).isEqualTo(DEFAULT_IS_CREDITEUR);
        assertThat(testCompte.getSoldeDebit()).isEqualTo(DEFAULT_SOLDE_DEBIT);
        assertThat(testCompte.getSoldeCredit()).isEqualTo(DEFAULT_SOLDE_CREDIT);
        assertThat(testCompte.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createCompteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = compteRepository.findAll().size();

        // Create the Compte with an existing ID
        compte.setId(1L);
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteMockMvc.perform(post("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = compteRepository.findAll().size();
        // set the field null
        compte.setCode(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc.perform(post("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = compteRepository.findAll().size();
        // set the field null
        compte.setLibelle(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc.perform(post("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComptes() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList
        restCompteMockMvc.perform(get("/api/comptes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].isDebit").value(hasItem(DEFAULT_IS_DEBIT.booleanValue())))
            .andExpect(jsonPath("$.[*].isCredit").value(hasItem(DEFAULT_IS_CREDIT.booleanValue())))
            .andExpect(jsonPath("$.[*].isDebiteur").value(hasItem(DEFAULT_IS_DEBITEUR.booleanValue())))
            .andExpect(jsonPath("$.[*].isCrediteur").value(hasItem(DEFAULT_IS_CREDITEUR.booleanValue())))
            .andExpect(jsonPath("$.[*].soldeDebit").value(hasItem(DEFAULT_SOLDE_DEBIT.doubleValue())))
            .andExpect(jsonPath("$.[*].soldeCredit").value(hasItem(DEFAULT_SOLDE_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get the compte
        restCompteMockMvc.perform(get("/api/comptes/{id}", compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(compte.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.isDebit").value(DEFAULT_IS_DEBIT.booleanValue()))
            .andExpect(jsonPath("$.isCredit").value(DEFAULT_IS_CREDIT.booleanValue()))
            .andExpect(jsonPath("$.isDebiteur").value(DEFAULT_IS_DEBITEUR.booleanValue()))
            .andExpect(jsonPath("$.isCrediteur").value(DEFAULT_IS_CREDITEUR.booleanValue()))
            .andExpect(jsonPath("$.soldeDebit").value(DEFAULT_SOLDE_DEBIT.doubleValue()))
            .andExpect(jsonPath("$.soldeCredit").value(DEFAULT_SOLDE_CREDIT.doubleValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllComptesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where code equals to DEFAULT_CODE
        defaultCompteShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the compteList where code equals to UPDATED_CODE
        defaultCompteShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllComptesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCompteShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the compteList where code equals to UPDATED_CODE
        defaultCompteShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllComptesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where code is not null
        defaultCompteShouldBeFound("code.specified=true");

        // Get all the compteList where code is null
        defaultCompteShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where code greater than or equals to DEFAULT_CODE
        defaultCompteShouldBeFound("code.greaterOrEqualThan=" + DEFAULT_CODE);

        // Get all the compteList where code greater than or equals to UPDATED_CODE
        defaultCompteShouldNotBeFound("code.greaterOrEqualThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllComptesByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where code less than or equals to DEFAULT_CODE
        defaultCompteShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the compteList where code less than or equals to UPDATED_CODE
        defaultCompteShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllComptesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where libelle equals to DEFAULT_LIBELLE
        defaultCompteShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the compteList where libelle equals to UPDATED_LIBELLE
        defaultCompteShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllComptesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultCompteShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the compteList where libelle equals to UPDATED_LIBELLE
        defaultCompteShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllComptesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where libelle is not null
        defaultCompteShouldBeFound("libelle.specified=true");

        // Get all the compteList where libelle is null
        defaultCompteShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesByIsDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isDebit equals to DEFAULT_IS_DEBIT
        defaultCompteShouldBeFound("isDebit.equals=" + DEFAULT_IS_DEBIT);

        // Get all the compteList where isDebit equals to UPDATED_IS_DEBIT
        defaultCompteShouldNotBeFound("isDebit.equals=" + UPDATED_IS_DEBIT);
    }

    @Test
    @Transactional
    public void getAllComptesByIsDebitIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isDebit in DEFAULT_IS_DEBIT or UPDATED_IS_DEBIT
        defaultCompteShouldBeFound("isDebit.in=" + DEFAULT_IS_DEBIT + "," + UPDATED_IS_DEBIT);

        // Get all the compteList where isDebit equals to UPDATED_IS_DEBIT
        defaultCompteShouldNotBeFound("isDebit.in=" + UPDATED_IS_DEBIT);
    }

    @Test
    @Transactional
    public void getAllComptesByIsDebitIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isDebit is not null
        defaultCompteShouldBeFound("isDebit.specified=true");

        // Get all the compteList where isDebit is null
        defaultCompteShouldNotBeFound("isDebit.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesByIsCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isCredit equals to DEFAULT_IS_CREDIT
        defaultCompteShouldBeFound("isCredit.equals=" + DEFAULT_IS_CREDIT);

        // Get all the compteList where isCredit equals to UPDATED_IS_CREDIT
        defaultCompteShouldNotBeFound("isCredit.equals=" + UPDATED_IS_CREDIT);
    }

    @Test
    @Transactional
    public void getAllComptesByIsCreditIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isCredit in DEFAULT_IS_CREDIT or UPDATED_IS_CREDIT
        defaultCompteShouldBeFound("isCredit.in=" + DEFAULT_IS_CREDIT + "," + UPDATED_IS_CREDIT);

        // Get all the compteList where isCredit equals to UPDATED_IS_CREDIT
        defaultCompteShouldNotBeFound("isCredit.in=" + UPDATED_IS_CREDIT);
    }

    @Test
    @Transactional
    public void getAllComptesByIsCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isCredit is not null
        defaultCompteShouldBeFound("isCredit.specified=true");

        // Get all the compteList where isCredit is null
        defaultCompteShouldNotBeFound("isCredit.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesByIsDebiteurIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isDebiteur equals to DEFAULT_IS_DEBITEUR
        defaultCompteShouldBeFound("isDebiteur.equals=" + DEFAULT_IS_DEBITEUR);

        // Get all the compteList where isDebiteur equals to UPDATED_IS_DEBITEUR
        defaultCompteShouldNotBeFound("isDebiteur.equals=" + UPDATED_IS_DEBITEUR);
    }

    @Test
    @Transactional
    public void getAllComptesByIsDebiteurIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isDebiteur in DEFAULT_IS_DEBITEUR or UPDATED_IS_DEBITEUR
        defaultCompteShouldBeFound("isDebiteur.in=" + DEFAULT_IS_DEBITEUR + "," + UPDATED_IS_DEBITEUR);

        // Get all the compteList where isDebiteur equals to UPDATED_IS_DEBITEUR
        defaultCompteShouldNotBeFound("isDebiteur.in=" + UPDATED_IS_DEBITEUR);
    }

    @Test
    @Transactional
    public void getAllComptesByIsDebiteurIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isDebiteur is not null
        defaultCompteShouldBeFound("isDebiteur.specified=true");

        // Get all the compteList where isDebiteur is null
        defaultCompteShouldNotBeFound("isDebiteur.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesByIsCrediteurIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isCrediteur equals to DEFAULT_IS_CREDITEUR
        defaultCompteShouldBeFound("isCrediteur.equals=" + DEFAULT_IS_CREDITEUR);

        // Get all the compteList where isCrediteur equals to UPDATED_IS_CREDITEUR
        defaultCompteShouldNotBeFound("isCrediteur.equals=" + UPDATED_IS_CREDITEUR);
    }

    @Test
    @Transactional
    public void getAllComptesByIsCrediteurIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isCrediteur in DEFAULT_IS_CREDITEUR or UPDATED_IS_CREDITEUR
        defaultCompteShouldBeFound("isCrediteur.in=" + DEFAULT_IS_CREDITEUR + "," + UPDATED_IS_CREDITEUR);

        // Get all the compteList where isCrediteur equals to UPDATED_IS_CREDITEUR
        defaultCompteShouldNotBeFound("isCrediteur.in=" + UPDATED_IS_CREDITEUR);
    }

    @Test
    @Transactional
    public void getAllComptesByIsCrediteurIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where isCrediteur is not null
        defaultCompteShouldBeFound("isCrediteur.specified=true");

        // Get all the compteList where isCrediteur is null
        defaultCompteShouldNotBeFound("isCrediteur.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesBySoldeDebitIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where soldeDebit equals to DEFAULT_SOLDE_DEBIT
        defaultCompteShouldBeFound("soldeDebit.equals=" + DEFAULT_SOLDE_DEBIT);

        // Get all the compteList where soldeDebit equals to UPDATED_SOLDE_DEBIT
        defaultCompteShouldNotBeFound("soldeDebit.equals=" + UPDATED_SOLDE_DEBIT);
    }

    @Test
    @Transactional
    public void getAllComptesBySoldeDebitIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where soldeDebit in DEFAULT_SOLDE_DEBIT or UPDATED_SOLDE_DEBIT
        defaultCompteShouldBeFound("soldeDebit.in=" + DEFAULT_SOLDE_DEBIT + "," + UPDATED_SOLDE_DEBIT);

        // Get all the compteList where soldeDebit equals to UPDATED_SOLDE_DEBIT
        defaultCompteShouldNotBeFound("soldeDebit.in=" + UPDATED_SOLDE_DEBIT);
    }

    @Test
    @Transactional
    public void getAllComptesBySoldeDebitIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where soldeDebit is not null
        defaultCompteShouldBeFound("soldeDebit.specified=true");

        // Get all the compteList where soldeDebit is null
        defaultCompteShouldNotBeFound("soldeDebit.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesBySoldeCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where soldeCredit equals to DEFAULT_SOLDE_CREDIT
        defaultCompteShouldBeFound("soldeCredit.equals=" + DEFAULT_SOLDE_CREDIT);

        // Get all the compteList where soldeCredit equals to UPDATED_SOLDE_CREDIT
        defaultCompteShouldNotBeFound("soldeCredit.equals=" + UPDATED_SOLDE_CREDIT);
    }

    @Test
    @Transactional
    public void getAllComptesBySoldeCreditIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where soldeCredit in DEFAULT_SOLDE_CREDIT or UPDATED_SOLDE_CREDIT
        defaultCompteShouldBeFound("soldeCredit.in=" + DEFAULT_SOLDE_CREDIT + "," + UPDATED_SOLDE_CREDIT);

        // Get all the compteList where soldeCredit equals to UPDATED_SOLDE_CREDIT
        defaultCompteShouldNotBeFound("soldeCredit.in=" + UPDATED_SOLDE_CREDIT);
    }

    @Test
    @Transactional
    public void getAllComptesBySoldeCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where soldeCredit is not null
        defaultCompteShouldBeFound("soldeCredit.specified=true");

        // Get all the compteList where soldeCredit is null
        defaultCompteShouldNotBeFound("soldeCredit.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where balance equals to DEFAULT_BALANCE
        defaultCompteShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the compteList where balance equals to UPDATED_BALANCE
        defaultCompteShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllComptesByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultCompteShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the compteList where balance equals to UPDATED_BALANCE
        defaultCompteShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllComptesByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        // Get all the compteList where balance is not null
        defaultCompteShouldBeFound("balance.specified=true");

        // Get all the compteList where balance is null
        defaultCompteShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllComptesByCompteClientIsEqualToSomething() throws Exception {
        // Initialize the database
        Client compteClient = ClientResourceIntTest.createEntity(em);
        em.persist(compteClient);
        em.flush();
        compte.setCompteClient(compteClient);
        compteRepository.saveAndFlush(compte);
        Long compteClientId = compteClient.getId();

        // Get all the compteList where compteClient equals to compteClientId
        defaultCompteShouldBeFound("compteClientId.equals=" + compteClientId);

        // Get all the compteList where compteClient equals to compteClientId + 1
        defaultCompteShouldNotBeFound("compteClientId.equals=" + (compteClientId + 1));
    }


    @Test
    @Transactional
    public void getAllComptesByEcritureCompteIsEqualToSomething() throws Exception {
        // Initialize the database
        Ecriture ecritureCompte = EcritureResourceIntTest.createEntity(em);
        em.persist(ecritureCompte);
        em.flush();
        compte.addEcritureCompte(ecritureCompte);
        compteRepository.saveAndFlush(compte);
        Long ecritureCompteId = ecritureCompte.getId();

        // Get all the compteList where ecritureCompte equals to ecritureCompteId
        defaultCompteShouldBeFound("ecritureCompteId.equals=" + ecritureCompteId);

        // Get all the compteList where ecritureCompte equals to ecritureCompteId + 1
        defaultCompteShouldNotBeFound("ecritureCompteId.equals=" + (ecritureCompteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCompteShouldBeFound(String filter) throws Exception {
        restCompteMockMvc.perform(get("/api/comptes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].isDebit").value(hasItem(DEFAULT_IS_DEBIT.booleanValue())))
            .andExpect(jsonPath("$.[*].isCredit").value(hasItem(DEFAULT_IS_CREDIT.booleanValue())))
            .andExpect(jsonPath("$.[*].isDebiteur").value(hasItem(DEFAULT_IS_DEBITEUR.booleanValue())))
            .andExpect(jsonPath("$.[*].isCrediteur").value(hasItem(DEFAULT_IS_CREDITEUR.booleanValue())))
            .andExpect(jsonPath("$.[*].soldeDebit").value(hasItem(DEFAULT_SOLDE_DEBIT.doubleValue())))
            .andExpect(jsonPath("$.[*].soldeCredit").value(hasItem(DEFAULT_SOLDE_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restCompteMockMvc.perform(get("/api/comptes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCompteShouldNotBeFound(String filter) throws Exception {
        restCompteMockMvc.perform(get("/api/comptes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompteMockMvc.perform(get("/api/comptes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCompte() throws Exception {
        // Get the compte
        restCompteMockMvc.perform(get("/api/comptes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        int databaseSizeBeforeUpdate = compteRepository.findAll().size();

        // Update the compte
        Compte updatedCompte = compteRepository.findById(compte.getId()).get();
        // Disconnect from session so that the updates on updatedCompte are not directly saved in db
        em.detach(updatedCompte);
        updatedCompte
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .isDebit(UPDATED_IS_DEBIT)
            .isCredit(UPDATED_IS_CREDIT)
            .isDebiteur(UPDATED_IS_DEBITEUR)
            .isCrediteur(UPDATED_IS_CREDITEUR)
            .soldeDebit(UPDATED_SOLDE_DEBIT)
            .soldeCredit(UPDATED_SOLDE_CREDIT)
            .balance(UPDATED_BALANCE);
        CompteDTO compteDTO = compteMapper.toDto(updatedCompte);

        restCompteMockMvc.perform(put("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isOk());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
        Compte testCompte = compteList.get(compteList.size() - 1);
        assertThat(testCompte.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCompte.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCompte.isIsDebit()).isEqualTo(UPDATED_IS_DEBIT);
        assertThat(testCompte.isIsCredit()).isEqualTo(UPDATED_IS_CREDIT);
        assertThat(testCompte.isIsDebiteur()).isEqualTo(UPDATED_IS_DEBITEUR);
        assertThat(testCompte.isIsCrediteur()).isEqualTo(UPDATED_IS_CREDITEUR);
        assertThat(testCompte.getSoldeDebit()).isEqualTo(UPDATED_SOLDE_DEBIT);
        assertThat(testCompte.getSoldeCredit()).isEqualTo(UPDATED_SOLDE_CREDIT);
        assertThat(testCompte.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCompte() throws Exception {
        int databaseSizeBeforeUpdate = compteRepository.findAll().size();

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc.perform(put("/api/comptes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCompte() throws Exception {
        // Initialize the database
        compteRepository.saveAndFlush(compte);

        int databaseSizeBeforeDelete = compteRepository.findAll().size();

        // Get the compte
        restCompteMockMvc.perform(delete("/api/comptes/{id}", compte.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Compte> compteList = compteRepository.findAll();
        assertThat(compteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compte.class);
        Compte compte1 = new Compte();
        compte1.setId(1L);
        Compte compte2 = new Compte();
        compte2.setId(compte1.getId());
        assertThat(compte1).isEqualTo(compte2);
        compte2.setId(2L);
        assertThat(compte1).isNotEqualTo(compte2);
        compte1.setId(null);
        assertThat(compte1).isNotEqualTo(compte2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteDTO.class);
        CompteDTO compteDTO1 = new CompteDTO();
        compteDTO1.setId(1L);
        CompteDTO compteDTO2 = new CompteDTO();
        assertThat(compteDTO1).isNotEqualTo(compteDTO2);
        compteDTO2.setId(compteDTO1.getId());
        assertThat(compteDTO1).isEqualTo(compteDTO2);
        compteDTO2.setId(2L);
        assertThat(compteDTO1).isNotEqualTo(compteDTO2);
        compteDTO1.setId(null);
        assertThat(compteDTO1).isNotEqualTo(compteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(compteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(compteMapper.fromId(null)).isNull();
    }
}
