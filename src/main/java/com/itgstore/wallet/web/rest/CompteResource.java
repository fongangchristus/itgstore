package com.itgstore.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgstore.wallet.service.CompteService;
import com.itgstore.wallet.web.rest.errors.BadRequestAlertException;
import com.itgstore.wallet.web.rest.util.HeaderUtil;
import com.itgstore.wallet.web.rest.util.PaginationUtil;
import com.itgstore.wallet.service.dto.CompteDTO;
import com.itgstore.wallet.service.dto.CompteCriteria;
import com.itgstore.wallet.service.CompteQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Compte.
 */
@RestController
@RequestMapping("/api")
public class CompteResource {

    private final Logger log = LoggerFactory.getLogger(CompteResource.class);

    private static final String ENTITY_NAME = "compte";

    private final CompteService compteService;

    private final CompteQueryService compteQueryService;

    public CompteResource(CompteService compteService, CompteQueryService compteQueryService) {
        this.compteService = compteService;
        this.compteQueryService = compteQueryService;
    }

    /**
     * POST  /comptes : Create a new compte.
     *
     * @param compteDTO the compteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compteDTO, or with status 400 (Bad Request) if the compte has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comptes")
    @Timed
    public ResponseEntity<CompteDTO> createCompte(@Valid @RequestBody CompteDTO compteDTO) throws URISyntaxException {
        log.debug("REST request to save Compte : {}", compteDTO);
        if (compteDTO.getId() != null) {
            throw new BadRequestAlertException("A new compte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompteDTO result = compteService.save(compteDTO);
        return ResponseEntity.created(new URI("/api/comptes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comptes : Updates an existing compte.
     *
     * @param compteDTO the compteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated compteDTO,
     * or with status 400 (Bad Request) if the compteDTO is not valid,
     * or with status 500 (Internal Server Error) if the compteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comptes")
    @Timed
    public ResponseEntity<CompteDTO> updateCompte(@Valid @RequestBody CompteDTO compteDTO) throws URISyntaxException {
        log.debug("REST request to update Compte : {}", compteDTO);
        if (compteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompteDTO result = compteService.save(compteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, compteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comptes : get all the comptes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of comptes in body
     */
    @GetMapping("/comptes")
    @Timed
    public ResponseEntity<List<CompteDTO>> getAllComptes(CompteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Comptes by criteria: {}", criteria);
        Page<CompteDTO> page = compteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/comptes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /comptes/count : count all the comptes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/comptes/count")
    @Timed
    public ResponseEntity<Long> countComptes(CompteCriteria criteria) {
        log.debug("REST request to count Comptes by criteria: {}", criteria);
        return ResponseEntity.ok().body(compteQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /comptes/:id : get the "id" compte.
     *
     * @param id the id of the compteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the compteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comptes/{id}")
    @Timed
    public ResponseEntity<CompteDTO> getCompte(@PathVariable Long id) {
        log.debug("REST request to get Compte : {}", id);
        Optional<CompteDTO> compteDTO = compteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compteDTO);
    }

    /**
     * DELETE  /comptes/:id : delete the "id" compte.
     *
     * @param id the id of the compteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comptes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompte(@PathVariable Long id) {
        log.debug("REST request to delete Compte : {}", id);
        compteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
