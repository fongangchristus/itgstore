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

import com.itgstore.wallet.domain.Compte;
import com.itgstore.wallet.domain.*; // for static metamodels
import com.itgstore.wallet.repository.CompteRepository;
import com.itgstore.wallet.service.dto.CompteCriteria;
import com.itgstore.wallet.service.dto.CompteDTO;
import com.itgstore.wallet.service.mapper.CompteMapper;

/**
 * Service for executing complex queries for Compte entities in the database.
 * The main input is a {@link CompteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompteDTO} or a {@link Page} of {@link CompteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompteQueryService extends QueryService<Compte> {

    private final Logger log = LoggerFactory.getLogger(CompteQueryService.class);

    private final CompteRepository compteRepository;

    private final CompteMapper compteMapper;

    public CompteQueryService(CompteRepository compteRepository, CompteMapper compteMapper) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
    }

    /**
     * Return a {@link List} of {@link CompteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompteDTO> findByCriteria(CompteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Compte> specification = createSpecification(criteria);
        return compteMapper.toDto(compteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CompteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompteDTO> findByCriteria(CompteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Compte> specification = createSpecification(criteria);
        return compteRepository.findAll(specification, page)
            .map(compteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Compte> specification = createSpecification(criteria);
        return compteRepository.count(specification);
    }

    /**
     * Function to convert CompteCriteria to a {@link Specification}
     */
    private Specification<Compte> createSpecification(CompteCriteria criteria) {
        Specification<Compte> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Compte_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCode(), Compte_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Compte_.libelle));
            }
            if (criteria.getIsDebit() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDebit(), Compte_.isDebit));
            }
            if (criteria.getIsCredit() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCredit(), Compte_.isCredit));
            }
            if (criteria.getIsDebiteur() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDebiteur(), Compte_.isDebiteur));
            }
            if (criteria.getIsCrediteur() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCrediteur(), Compte_.isCrediteur));
            }
            if (criteria.getSoldeDebit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSoldeDebit(), Compte_.soldeDebit));
            }
            if (criteria.getSoldeCredit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSoldeCredit(), Compte_.soldeCredit));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), Compte_.balance));
            }
            if (criteria.getCompteClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompteClientId(),
                    root -> root.join(Compte_.compteClient, JoinType.LEFT).get(Client_.id)));
            }
            if (criteria.getEcritureCompteId() != null) {
                specification = specification.and(buildSpecification(criteria.getEcritureCompteId(),
                    root -> root.join(Compte_.ecritureComptes, JoinType.LEFT).get(Ecriture_.id)));
            }
        }
        return specification;
    }
}
