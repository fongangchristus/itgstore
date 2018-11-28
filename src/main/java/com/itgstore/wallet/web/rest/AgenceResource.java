package com.itgstore.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgstore.wallet.service.AgenceService;
import com.itgstore.wallet.web.rest.errors.BadRequestAlertException;
import com.itgstore.wallet.web.rest.util.HeaderUtil;
import com.itgstore.wallet.web.rest.util.PaginationUtil;
import com.itgstore.wallet.service.dto.AgenceDTO;
import com.itgstore.wallet.service.dto.AgenceCriteria;
import com.itgstore.wallet.service.AgenceQueryService;
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
 * REST controller for managing Agence.
 */
@RestController
@RequestMapping("/api")
public class AgenceResource {

    private final Logger log = LoggerFactory.getLogger(AgenceResource.class);

    private static final String ENTITY_NAME = "agence";

    private final AgenceService agenceService;

    private final AgenceQueryService agenceQueryService;

    public AgenceResource(AgenceService agenceService, AgenceQueryService agenceQueryService) {
        this.agenceService = agenceService;
        this.agenceQueryService = agenceQueryService;
    }

    /**
     * POST  /agences : Create a new agence.
     *
     * @param agenceDTO the agenceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agenceDTO, or with status 400 (Bad Request) if the agence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agences")
    @Timed
    public ResponseEntity<AgenceDTO> createAgence(@Valid @RequestBody AgenceDTO agenceDTO) throws URISyntaxException {
        log.debug("REST request to save Agence : {}", agenceDTO);
        if (agenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new agence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgenceDTO result = agenceService.save(agenceDTO);
        return ResponseEntity.created(new URI("/api/agences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agences : Updates an existing agence.
     *
     * @param agenceDTO the agenceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agenceDTO,
     * or with status 400 (Bad Request) if the agenceDTO is not valid,
     * or with status 500 (Internal Server Error) if the agenceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agences")
    @Timed
    public ResponseEntity<AgenceDTO> updateAgence(@Valid @RequestBody AgenceDTO agenceDTO) throws URISyntaxException {
        log.debug("REST request to update Agence : {}", agenceDTO);
        if (agenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgenceDTO result = agenceService.save(agenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agences : get all the agences.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of agences in body
     */
    @GetMapping("/agences")
    @Timed
    public ResponseEntity<List<AgenceDTO>> getAllAgences(AgenceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Agences by criteria: {}", criteria);
        Page<AgenceDTO> page = agenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agences");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /agences/count : count all the agences.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/agences/count")
    @Timed
    public ResponseEntity<Long> countAgences(AgenceCriteria criteria) {
        log.debug("REST request to count Agences by criteria: {}", criteria);
        return ResponseEntity.ok().body(agenceQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /agences/:id : get the "id" agence.
     *
     * @param id the id of the agenceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agenceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/agences/{id}")
    @Timed
    public ResponseEntity<AgenceDTO> getAgence(@PathVariable Long id) {
        log.debug("REST request to get Agence : {}", id);
        Optional<AgenceDTO> agenceDTO = agenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agenceDTO);
    }

    /**
     * DELETE  /agences/:id : delete the "id" agence.
     *
     * @param id the id of the agenceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agences/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgence(@PathVariable Long id) {
        log.debug("REST request to delete Agence : {}", id);
        agenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
