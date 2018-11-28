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

import com.itgstore.wallet.domain.Operation;
import com.itgstore.wallet.domain.*; // for static metamodels
import com.itgstore.wallet.repository.OperationRepository;
import com.itgstore.wallet.service.dto.OperationCriteria;
import com.itgstore.wallet.service.dto.OperationDTO;
import com.itgstore.wallet.service.mapper.OperationMapper;

/**
 * Service for executing complex queries for Operation entities in the database.
 * The main input is a {@link OperationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OperationDTO} or a {@link Page} of {@link OperationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OperationQueryService extends QueryService<Operation> {

    private final Logger log = LoggerFactory.getLogger(OperationQueryService.class);

    private final OperationRepository operationRepository;

    private final OperationMapper operationMapper;

    public OperationQueryService(OperationRepository operationRepository, OperationMapper operationMapper) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
    }

    /**
     * Return a {@link List} of {@link OperationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OperationDTO> findByCriteria(OperationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Operation> specification = createSpecification(criteria);
        return operationMapper.toDto(operationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OperationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OperationDTO> findByCriteria(OperationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Operation> specification = createSpecification(criteria);
        return operationRepository.findAll(specification, page)
            .map(operationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OperationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Operation> specification = createSpecification(criteria);
        return operationRepository.count(specification);
    }

    /**
     * Function to convert OperationCriteria to a {@link Specification}
     */
    private Specification<Operation> createSpecification(OperationCriteria criteria) {
        Specification<Operation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Operation_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCode(), Operation_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Operation_.libelle));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), Operation_.montant));
            }
            if (criteria.getDateOperation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOperation(), Operation_.dateOperation));
            }
            if (criteria.getMessageRetourt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessageRetourt(), Operation_.messageRetourt));
            }
            if (criteria.getOperationType() != null) {
                specification = specification.and(buildSpecification(criteria.getOperationType(), Operation_.operationType));
            }
            if (criteria.getTransactionOperationId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionOperationId(),
                    root -> root.join(Operation_.transactionOperations, JoinType.LEFT).get(Transaction_.id)));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(Operation_.client, JoinType.LEFT).get(Client_.id)));
            }
            if (criteria.getDeviceId() != null) {
                specification = specification.and(buildSpecification(criteria.getDeviceId(),
                    root -> root.join(Operation_.device, JoinType.LEFT).get(Device_.id)));
            }
            if (criteria.getAgencEmetteurId() != null) {
                specification = specification.and(buildSpecification(criteria.getAgencEmetteurId(),
                    root -> root.join(Operation_.agencEmetteur, JoinType.LEFT).get(Agence_.id)));
            }
            if (criteria.getAgencePayeurId() != null) {
                specification = specification.and(buildSpecification(criteria.getAgencePayeurId(),
                    root -> root.join(Operation_.agencePayeur, JoinType.LEFT).get(Agence_.id)));
            }
        }
        return specification;
    }
}
