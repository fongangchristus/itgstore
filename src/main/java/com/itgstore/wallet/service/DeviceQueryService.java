package com.itgstore.wallet.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.itgstore.wallet.domain.Device;
import com.itgstore.wallet.domain.*; // for static metamodels
import com.itgstore.wallet.repository.DeviceRepository;
import com.itgstore.wallet.service.dto.DeviceCriteria;
import com.itgstore.wallet.service.dto.DeviceDTO;
import com.itgstore.wallet.service.mapper.DeviceMapper;

/**
 * Service for executing complex queries for Device entities in the database.
 * The main input is a {@link DeviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeviceDTO} or a {@link Page} of {@link DeviceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeviceQueryService extends QueryService<Device> {

    private final Logger log = LoggerFactory.getLogger(DeviceQueryService.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    public DeviceQueryService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    /**
     * Return a {@link List} of {@link DeviceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeviceDTO> findByCriteria(DeviceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Device> specification = createSpecification(criteria);
        return deviceMapper.toDto(deviceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeviceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceDTO> findByCriteria(DeviceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Device> specification = createSpecification(criteria);
        return deviceRepository.findAll(specification, page)
            .map(deviceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeviceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Device> specification = createSpecification(criteria);
        return deviceRepository.count(specification);
    }

    /**
     * Function to convert DeviceCriteria to a {@link Specification}
     */
    private Specification<Device> createSpecification(DeviceCriteria criteria) {
        Specification<Device> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Device_.id));
            }
            if (criteria.getDeviceMark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceMark(), Device_.deviceMark));
            }
            if (criteria.getDeviceOs() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceOs(), Device_.deviceOs));
            }
            if (criteria.getDeviceNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeviceNumber(), Device_.deviceNumber));
            }
            if (criteria.getToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getToken(), Device_.token));
            }
            if (criteria.getFactureDevicePayeurId() != null) {
                specification = specification.and(buildSpecification(criteria.getFactureDevicePayeurId(),
                    root -> root.join(Device_.factureDevicePayeurs, JoinType.LEFT).get(Facture_.id)));
            }
            if (criteria.getOperationDeviceEmetteurId() != null) {
                specification = specification.and(buildSpecification(criteria.getOperationDeviceEmetteurId(),
                    root -> root.join(Device_.operationDeviceEmetteurs, JoinType.LEFT).get(Operation_.id)));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(Device_.client, JoinType.LEFT).get(Client_.id)));
            }
        }
        return specification;
    }
}
