package com.itgstore.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgstore.wallet.service.FactureService;
import com.itgstore.wallet.web.rest.errors.BadRequestAlertException;
import com.itgstore.wallet.web.rest.util.HeaderUtil;
import com.itgstore.wallet.web.rest.util.PaginationUtil;
import com.itgstore.wallet.service.dto.FactureDTO;
import com.itgstore.wallet.service.dto.FactureCriteria;
import com.itgstore.wallet.service.FactureQueryService;
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
 * REST controller for managing Facture.
 */
@RestController
@RequestMapping("/api")
public class FactureResource {

    private final Logger log = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    private final FactureService factureService;

    private final FactureQueryService factureQueryService;

    public FactureResource(FactureService factureService, FactureQueryService factureQueryService) {
        this.factureService = factureService;
        this.factureQueryService = factureQueryService;
    }

    /**
     * POST  /factures : Create a new facture.
     *
     * @param factureDTO the factureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new factureDTO, or with status 400 (Bad Request) if the facture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/factures")
    @Timed
    public ResponseEntity<FactureDTO> createFacture(@Valid @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        log.debug("REST request to save Facture : {}", factureDTO);
        if (factureDTO.getId() != null) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactureDTO result = factureService.save(factureDTO);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /factures : Updates an existing facture.
     *
     * @param factureDTO the factureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated factureDTO,
     * or with status 400 (Bad Request) if the factureDTO is not valid,
     * or with status 500 (Internal Server Error) if the factureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/factures")
    @Timed
    public ResponseEntity<FactureDTO> updateFacture(@Valid @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        log.debug("REST request to update Facture : {}", factureDTO);
        if (factureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FactureDTO result = factureService.save(factureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, factureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /factures : get all the factures.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of factures in body
     */
    @GetMapping("/factures")
    @Timed
    public ResponseEntity<List<FactureDTO>> getAllFactures(FactureCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Factures by criteria: {}", criteria);
        Page<FactureDTO> page = factureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/factures");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /factures/count : count all the factures.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/factures/count")
    @Timed
    public ResponseEntity<Long> countFactures(FactureCriteria criteria) {
        log.debug("REST request to count Factures by criteria: {}", criteria);
        return ResponseEntity.ok().body(factureQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /factures/:id : get the "id" facture.
     *
     * @param id the id of the factureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the factureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/factures/{id}")
    @Timed
    public ResponseEntity<FactureDTO> getFacture(@PathVariable Long id) {
        log.debug("REST request to get Facture : {}", id);
        Optional<FactureDTO> factureDTO = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factureDTO);
    }

    /**
     * DELETE  /factures/:id : delete the "id" facture.
     *
     * @param id the id of the factureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/factures/{id}")
    @Timed
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        log.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
