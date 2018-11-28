package com.itgstore.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgstore.wallet.service.PartenaireService;
import com.itgstore.wallet.web.rest.errors.BadRequestAlertException;
import com.itgstore.wallet.web.rest.util.HeaderUtil;
import com.itgstore.wallet.web.rest.util.PaginationUtil;
import com.itgstore.wallet.service.dto.PartenaireDTO;
import com.itgstore.wallet.service.dto.PartenaireCriteria;
import com.itgstore.wallet.service.PartenaireQueryService;
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
 * REST controller for managing Partenaire.
 */
@RestController
@RequestMapping("/api")
public class PartenaireResource {

    private final Logger log = LoggerFactory.getLogger(PartenaireResource.class);

    private static final String ENTITY_NAME = "partenaire";

    private final PartenaireService partenaireService;

    private final PartenaireQueryService partenaireQueryService;

    public PartenaireResource(PartenaireService partenaireService, PartenaireQueryService partenaireQueryService) {
        this.partenaireService = partenaireService;
        this.partenaireQueryService = partenaireQueryService;
    }

    /**
     * POST  /partenaires : Create a new partenaire.
     *
     * @param partenaireDTO the partenaireDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new partenaireDTO, or with status 400 (Bad Request) if the partenaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/partenaires")
    @Timed
    public ResponseEntity<PartenaireDTO> createPartenaire(@Valid @RequestBody PartenaireDTO partenaireDTO) throws URISyntaxException {
        log.debug("REST request to save Partenaire : {}", partenaireDTO);
        if (partenaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new partenaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartenaireDTO result = partenaireService.save(partenaireDTO);
        return ResponseEntity.created(new URI("/api/partenaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /partenaires : Updates an existing partenaire.
     *
     * @param partenaireDTO the partenaireDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated partenaireDTO,
     * or with status 400 (Bad Request) if the partenaireDTO is not valid,
     * or with status 500 (Internal Server Error) if the partenaireDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/partenaires")
    @Timed
    public ResponseEntity<PartenaireDTO> updatePartenaire(@Valid @RequestBody PartenaireDTO partenaireDTO) throws URISyntaxException {
        log.debug("REST request to update Partenaire : {}", partenaireDTO);
        if (partenaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PartenaireDTO result = partenaireService.save(partenaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, partenaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /partenaires : get all the partenaires.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of partenaires in body
     */
    @GetMapping("/partenaires")
    @Timed
    public ResponseEntity<List<PartenaireDTO>> getAllPartenaires(PartenaireCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Partenaires by criteria: {}", criteria);
        Page<PartenaireDTO> page = partenaireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/partenaires");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /partenaires/count : count all the partenaires.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/partenaires/count")
    @Timed
    public ResponseEntity<Long> countPartenaires(PartenaireCriteria criteria) {
        log.debug("REST request to count Partenaires by criteria: {}", criteria);
        return ResponseEntity.ok().body(partenaireQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /partenaires/:id : get the "id" partenaire.
     *
     * @param id the id of the partenaireDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the partenaireDTO, or with status 404 (Not Found)
     */
    @GetMapping("/partenaires/{id}")
    @Timed
    public ResponseEntity<PartenaireDTO> getPartenaire(@PathVariable Long id) {
        log.debug("REST request to get Partenaire : {}", id);
        Optional<PartenaireDTO> partenaireDTO = partenaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partenaireDTO);
    }

    /**
     * DELETE  /partenaires/:id : delete the "id" partenaire.
     *
     * @param id the id of the partenaireDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/partenaires/{id}")
    @Timed
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        log.debug("REST request to delete Partenaire : {}", id);
        partenaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
