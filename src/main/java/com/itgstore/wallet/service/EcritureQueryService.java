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

import com.itgstore.wallet.domain.Ecriture;
import com.itgstore.wallet.domain.*; // for static metamodels
import com.itgstore.wallet.repository.EcritureRepository;
import com.itgstore.wallet.service.dto.EcritureCriteria;
import com.itgstore.wallet.service.dto.EcritureDTO;
import com.itgstore.wallet.service.mapper.EcritureMapper;

/**
 * Service for executing complex queries for Ecriture entities in the database.
 * The main input is a {@link EcritureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EcritureDTO} or a {@link Page} of {@link EcritureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EcritureQueryService extends QueryService<Ecriture> {

    private final Logger log = LoggerFactory.getLogger(EcritureQueryService.class);

    private final EcritureRepository ecritureRepository;

    private final EcritureMapper ecritureMapper;

    public EcritureQueryService(EcritureRepository ecritureRepository, EcritureMapper ecritureMapper) {
        this.ecritureRepository = ecritureRepository;
        this.ecritureMapper = ecritureMapper;
    }

    /**
     * Return a {@link List} of {@link EcritureDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EcritureDTO> findByCriteria(EcritureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ecriture> specification = createSpecification(criteria);
        return ecritureMapper.toDto(ecritureRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EcritureDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EcritureDTO> findByCriteria(EcritureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ecriture> specification = createSpecification(criteria);
        return ecritureRepository.findAll(specification, page)
            .map(ecritureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EcritureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ecriture> specification = createSpecification(criteria);
        return ecritureRepository.count(specification);
    }

    /**
     * Function to convert EcritureCriteria to a {@link Specification}
     */
    private Specification<Ecriture> createSpecification(EcritureCriteria criteria) {
        Specification<Ecriture> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Ecriture_.id));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), Ecriture_.montant));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Ecriture_.libelle));
            }
            if (criteria.getContrePartie() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContrePartie(), Ecriture_.contrePartie));
            }
            if (criteria.getSensEcriture() != null) {
                specification = specification.and(buildSpecification(criteria.getSensEcriture(), Ecriture_.sensEcriture));
            }
            if (criteria.getCompteId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompteId(),
                    root -> root.join(Ecriture_.compte, JoinType.LEFT).get(Compte_.id)));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionId(),
                    root -> root.join(Ecriture_.transaction, JoinType.LEFT).get(Transaction_.id)));
            }
        }
        return specification;
    }
}
