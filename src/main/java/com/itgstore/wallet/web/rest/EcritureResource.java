package com.itgstore.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgstore.wallet.service.EcritureService;
import com.itgstore.wallet.web.rest.errors.BadRequestAlertException;
import com.itgstore.wallet.web.rest.util.HeaderUtil;
import com.itgstore.wallet.web.rest.util.PaginationUtil;
import com.itgstore.wallet.service.dto.EcritureDTO;
import com.itgstore.wallet.service.dto.EcritureCriteria;
import com.itgstore.wallet.service.EcritureQueryService;
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
 * REST controller for managing Ecriture.
 */
@RestController
@RequestMapping("/api")
public class EcritureResource {

    private final Logger log = LoggerFactory.getLogger(EcritureResource.class);

    private static final String ENTITY_NAME = "ecriture";

    private final EcritureService ecritureService;

    private final EcritureQueryService ecritureQueryService;

    public EcritureResource(EcritureService ecritureService, EcritureQueryService ecritureQueryService) {
        this.ecritureService = ecritureService;
        this.ecritureQueryService = ecritureQueryService;
    }

    /**
     * POST  /ecritures : Create a new ecriture.
     *
     * @param ecritureDTO the ecritureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ecritureDTO, or with status 400 (Bad Request) if the ecriture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ecritures")
    @Timed
    public ResponseEntity<EcritureDTO> createEcriture(@Valid @RequestBody EcritureDTO ecritureDTO) throws URISyntaxException {
        log.debug("REST request to save Ecriture : {}", ecritureDTO);
        if (ecritureDTO.getId() != null) {
            throw new BadRequestAlertException("A new ecriture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EcritureDTO result = ecritureService.save(ecritureDTO);
        return ResponseEntity.created(new URI("/api/ecritures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ecritures : Updates an existing ecriture.
     *
     * @param ecritureDTO the ecritureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ecritureDTO,
     * or with status 400 (Bad Request) if the ecritureDTO is not valid,
     * or with status 500 (Internal Server Error) if the ecritureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ecritures")
    @Timed
    public ResponseEntity<EcritureDTO> updateEcriture(@Valid @RequestBody EcritureDTO ecritureDTO) throws URISyntaxException {
        log.debug("REST request to update Ecriture : {}", ecritureDTO);
        if (ecritureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EcritureDTO result = ecritureService.save(ecritureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ecritureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ecritures : get all the ecritures.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of ecritures in body
     */
    @GetMapping("/ecritures")
    @Timed
    public ResponseEntity<List<EcritureDTO>> getAllEcritures(EcritureCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ecritures by criteria: {}", criteria);
        Page<EcritureDTO> page = ecritureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ecritures");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /ecritures/count : count all the ecritures.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/ecritures/count")
    @Timed
    public ResponseEntity<Long> countEcritures(EcritureCriteria criteria) {
        log.debug("REST request to count Ecritures by criteria: {}", criteria);
        return ResponseEntity.ok().body(ecritureQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /ecritures/:id : get the "id" ecriture.
     *
     * @param id the id of the ecritureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ecritureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ecritures/{id}")
    @Timed
    public ResponseEntity<EcritureDTO> getEcriture(@PathVariable Long id) {
        log.debug("REST request to get Ecriture : {}", id);
        Optional<EcritureDTO> ecritureDTO = ecritureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ecritureDTO);
    }

    /**
     * DELETE  /ecritures/:id : delete the "id" ecriture.
     *
     * @param id the id of the ecritureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ecritures/{id}")
    @Timed
    public ResponseEntity<Void> deleteEcriture(@PathVariable Long id) {
        log.debug("REST request to delete Ecriture : {}", id);
        ecritureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
