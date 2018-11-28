package com.itgstore.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgstore.wallet.service.DeviceService;
import com.itgstore.wallet.web.rest.errors.BadRequestAlertException;
import com.itgstore.wallet.web.rest.util.HeaderUtil;
import com.itgstore.wallet.web.rest.util.PaginationUtil;
import com.itgstore.wallet.service.dto.DeviceDTO;
import com.itgstore.wallet.service.dto.DeviceCriteria;
import com.itgstore.wallet.service.DeviceQueryService;
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
 * REST controller for managing Device.
 */
@RestController
@RequestMapping("/api")
public class DeviceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    private static final String ENTITY_NAME = "device";

    private final DeviceService deviceService;

    private final DeviceQueryService deviceQueryService;

    public DeviceResource(DeviceService deviceService, DeviceQueryService deviceQueryService) {
        this.deviceService = deviceService;
        this.deviceQueryService = deviceQueryService;
    }

    /**
     * POST  /devices : Create a new device.
     *
     * @param deviceDTO the deviceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deviceDTO, or with status 400 (Bad Request) if the device has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/devices")
    @Timed
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody DeviceDTO deviceDTO) throws URISyntaxException {
        log.debug("REST request to save Device : {}", deviceDTO);
        if (deviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceDTO result = deviceService.save(deviceDTO);
        return ResponseEntity.created(new URI("/api/devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /devices : Updates an existing device.
     *
     * @param deviceDTO the deviceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deviceDTO,
     * or with status 400 (Bad Request) if the deviceDTO is not valid,
     * or with status 500 (Internal Server Error) if the deviceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/devices")
    @Timed
    public ResponseEntity<DeviceDTO> updateDevice(@Valid @RequestBody DeviceDTO deviceDTO) throws URISyntaxException {
        log.debug("REST request to update Device : {}", deviceDTO);
        if (deviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeviceDTO result = deviceService.save(deviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /devices : get all the devices.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of devices in body
     */
    @GetMapping("/devices")
    @Timed
    public ResponseEntity<List<DeviceDTO>> getAllDevices(DeviceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Devices by criteria: {}", criteria);
        Page<DeviceDTO> page = deviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/devices");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /devices/count : count all the devices.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/devices/count")
    @Timed
    public ResponseEntity<Long> countDevices(DeviceCriteria criteria) {
        log.debug("REST request to count Devices by criteria: {}", criteria);
        return ResponseEntity.ok().body(deviceQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /devices/:id : get the "id" device.
     *
     * @param id the id of the deviceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deviceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/devices/{id}")
    @Timed
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable Long id) {
        log.debug("REST request to get Device : {}", id);
        Optional<DeviceDTO> deviceDTO = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceDTO);
    }

    /**
     * DELETE  /devices/:id : delete the "id" device.
     *
     * @param id the id of the deviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/devices/{id}")
    @Timed
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.debug("REST request to delete Device : {}", id);
        deviceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
