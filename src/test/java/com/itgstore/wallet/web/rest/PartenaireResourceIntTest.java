package com.itgstore.wallet.web.rest;

import com.itgstore.wallet.WalletApp;

import com.itgstore.wallet.domain.Partenaire;
import com.itgstore.wallet.domain.Facture;
import com.itgstore.wallet.repository.PartenaireRepository;
import com.itgstore.wallet.service.PartenaireService;
import com.itgstore.wallet.service.dto.PartenaireDTO;
import com.itgstore.wallet.service.mapper.PartenaireMapper;
import com.itgstore.wallet.web.rest.errors.ExceptionTranslator;
import com.itgstore.wallet.service.dto.PartenaireCriteria;
import com.itgstore.wallet.service.PartenaireQueryService;

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
 * Test class for the PartenaireResource REST controller.
 *
 * @see PartenaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApp.class)
public class PartenaireResourceIntTest {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private PartenaireRepository partenaireRepository;

    @Autowired
    private PartenaireMapper partenaireMapper;

    @Autowired
    private PartenaireService partenaireService;

    @Autowired
    private PartenaireQueryService partenaireQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPartenaireMockMvc;

    private Partenaire partenaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PartenaireResource partenaireResource = new PartenaireResource(partenaireService, partenaireQueryService);
        this.restPartenaireMockMvc = MockMvcBuilders.standaloneSetup(partenaireResource)
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
    public static Partenaire createEntity(EntityManager em) {
        Partenaire partenaire = new Partenaire()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE);
        return partenaire;
    }

    @Before
    public void initTest() {
        partenaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartenaire() throws Exception {
        int databaseSizeBeforeCreate = partenaireRepository.findAll().size();

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);
        restPartenaireMockMvc.perform(post("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaireDTO)))
            .andExpect(status().isCreated());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeCreate + 1);
        Partenaire testPartenaire = partenaireList.get(partenaireList.size() - 1);
        assertThat(testPartenaire.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPartenaire.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createPartenaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partenaireRepository.findAll().size();

        // Create the Partenaire with an existing ID
        partenaire.setId(1L);
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartenaireMockMvc.perform(post("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setCode(null);

        // Create the Partenaire, which fails.
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        restPartenaireMockMvc.perform(post("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaireDTO)))
            .andExpect(status().isBadRequest());

        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setLibelle(null);

        // Create the Partenaire, which fails.
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        restPartenaireMockMvc.perform(post("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaireDTO)))
            .andExpect(status().isBadRequest());

        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPartenaires() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList
        restPartenaireMockMvc.perform(get("/api/partenaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
    
    @Test
    @Transactional
    public void getPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get the partenaire
        restPartenaireMockMvc.perform(get("/api/partenaires/{id}", partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(partenaire.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getAllPartenairesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where code equals to DEFAULT_CODE
        defaultPartenaireShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the partenaireList where code equals to UPDATED_CODE
        defaultPartenaireShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPartenairesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPartenaireShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the partenaireList where code equals to UPDATED_CODE
        defaultPartenaireShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPartenairesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where code is not null
        defaultPartenaireShouldBeFound("code.specified=true");

        // Get all the partenaireList where code is null
        defaultPartenaireShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllPartenairesByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where code greater than or equals to DEFAULT_CODE
        defaultPartenaireShouldBeFound("code.greaterOrEqualThan=" + DEFAULT_CODE);

        // Get all the partenaireList where code greater than or equals to UPDATED_CODE
        defaultPartenaireShouldNotBeFound("code.greaterOrEqualThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPartenairesByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where code less than or equals to DEFAULT_CODE
        defaultPartenaireShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the partenaireList where code less than or equals to UPDATED_CODE
        defaultPartenaireShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllPartenairesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where libelle equals to DEFAULT_LIBELLE
        defaultPartenaireShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the partenaireList where libelle equals to UPDATED_LIBELLE
        defaultPartenaireShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllPartenairesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultPartenaireShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the partenaireList where libelle equals to UPDATED_LIBELLE
        defaultPartenaireShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllPartenairesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where libelle is not null
        defaultPartenaireShouldBeFound("libelle.specified=true");

        // Get all the partenaireList where libelle is null
        defaultPartenaireShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    public void getAllPartenairesByFacturePartenaireEmetteurIsEqualToSomething() throws Exception {
        // Initialize the database
        Facture facturePartenaireEmetteur = FactureResourceIntTest.createEntity(em);
        em.persist(facturePartenaireEmetteur);
        em.flush();
        partenaire.addFacturePartenaireEmetteur(facturePartenaireEmetteur);
        partenaireRepository.saveAndFlush(partenaire);
        Long facturePartenaireEmetteurId = facturePartenaireEmetteur.getId();

        // Get all the partenaireList where facturePartenaireEmetteur equals to facturePartenaireEmetteurId
        defaultPartenaireShouldBeFound("facturePartenaireEmetteurId.equals=" + facturePartenaireEmetteurId);

        // Get all the partenaireList where facturePartenaireEmetteur equals to facturePartenaireEmetteurId + 1
        defaultPartenaireShouldNotBeFound("facturePartenaireEmetteurId.equals=" + (facturePartenaireEmetteurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPartenaireShouldBeFound(String filter) throws Exception {
        restPartenaireMockMvc.perform(get("/api/partenaires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));

        // Check, that the count call also returns 1
        restPartenaireMockMvc.perform(get("/api/partenaires/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPartenaireShouldNotBeFound(String filter) throws Exception {
        restPartenaireMockMvc.perform(get("/api/partenaires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartenaireMockMvc.perform(get("/api/partenaires/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPartenaire() throws Exception {
        // Get the partenaire
        restPartenaireMockMvc.perform(get("/api/partenaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Update the partenaire
        Partenaire updatedPartenaire = partenaireRepository.findById(partenaire.getId()).get();
        // Disconnect from session so that the updates on updatedPartenaire are not directly saved in db
        em.detach(updatedPartenaire);
        updatedPartenaire
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(updatedPartenaire);

        restPartenaireMockMvc.perform(put("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaireDTO)))
            .andExpect(status().isOk());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
        Partenaire testPartenaire = partenaireList.get(partenaireList.size() - 1);
        assertThat(testPartenaire.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPartenaire.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingPartenaire() throws Exception {
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenaireMockMvc.perform(put("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        int databaseSizeBeforeDelete = partenaireRepository.findAll().size();

        // Get the partenaire
        restPartenaireMockMvc.perform(delete("/api/partenaires/{id}", partenaire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Partenaire.class);
        Partenaire partenaire1 = new Partenaire();
        partenaire1.setId(1L);
        Partenaire partenaire2 = new Partenaire();
        partenaire2.setId(partenaire1.getId());
        assertThat(partenaire1).isEqualTo(partenaire2);
        partenaire2.setId(2L);
        assertThat(partenaire1).isNotEqualTo(partenaire2);
        partenaire1.setId(null);
        assertThat(partenaire1).isNotEqualTo(partenaire2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartenaireDTO.class);
        PartenaireDTO partenaireDTO1 = new PartenaireDTO();
        partenaireDTO1.setId(1L);
        PartenaireDTO partenaireDTO2 = new PartenaireDTO();
        assertThat(partenaireDTO1).isNotEqualTo(partenaireDTO2);
        partenaireDTO2.setId(partenaireDTO1.getId());
        assertThat(partenaireDTO1).isEqualTo(partenaireDTO2);
        partenaireDTO2.setId(2L);
        assertThat(partenaireDTO1).isNotEqualTo(partenaireDTO2);
        partenaireDTO1.setId(null);
        assertThat(partenaireDTO1).isNotEqualTo(partenaireDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(partenaireMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(partenaireMapper.fromId(null)).isNull();
    }
}
