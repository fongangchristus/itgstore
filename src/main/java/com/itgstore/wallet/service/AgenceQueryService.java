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

import com.itgstore.wallet.domain.Agence;
import com.itgstore.wallet.domain.*; // for static metamodels
import com.itgstore.wallet.repository.AgenceRepository;
import com.itgstore.wallet.service.dto.AgenceCriteria;
import com.itgstore.wallet.service.dto.AgenceDTO;
import com.itgstore.wallet.service.mapper.AgenceMapper;

/**
 * Service for executing complex queries for Agence entities in the database.
 * The main input is a {@link AgenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgenceDTO} or a {@link Page} of {@link AgenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgenceQueryService extends QueryService<Agence> {

    private final Logger log = LoggerFactory.getLogger(AgenceQueryService.class);

    private final AgenceRepository agenceRepository;

    private final AgenceMapper agenceMapper;

    public AgenceQueryService(AgenceRepository agenceRepository, AgenceMapper agenceMapper) {
        this.agenceRepository = agenceRepository;
        this.agenceMapper = agenceMapper;
    }

    /**
     * Return a {@link List} of {@link AgenceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgenceDTO> findByCriteria(AgenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Agence> specification = createSpecification(criteria);
        return agenceMapper.toDto(agenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AgenceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgenceDTO> findByCriteria(AgenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agence> specification = createSpecification(criteria);
        return agenceRepository.findAll(specification, page)
            .map(agenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Agence> specification = createSpecification(criteria);
        return agenceRepository.count(specification);
    }

    /**
     * Function to convert AgenceCriteria to a {@link Specification}
     */
    private Specification<Agence> createSpecification(AgenceCriteria criteria) {
        Specification<Agence> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Agence_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCode(), Agence_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Agence_.libelle));
            }
            if (criteria.getOperationAgenceEmeteurId() != null) {
                specification = specification.and(buildSpecification(criteria.getOperationAgenceEmeteurId(),
                    root -> root.join(Agence_.operationAgenceEmeteurs, JoinType.LEFT).get(Operation_.id)));
            }
            if (criteria.getOperationAgencePayeurId() != null) {
                specification = specification.and(buildSpecification(criteria.getOperationAgencePayeurId(),
                    root -> root.join(Agence_.operationAgencePayeurs, JoinType.LEFT).get(Operation_.id)));
            }
        }
        return specification;
    }
}
