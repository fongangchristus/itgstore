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

import com.itgstore.wallet.domain.Partenaire;
import com.itgstore.wallet.domain.*; // for static metamodels
import com.itgstore.wallet.repository.PartenaireRepository;
import com.itgstore.wallet.service.dto.PartenaireCriteria;
import com.itgstore.wallet.service.dto.PartenaireDTO;
import com.itgstore.wallet.service.mapper.PartenaireMapper;

/**
 * Service for executing complex queries for Partenaire entities in the database.
 * The main input is a {@link PartenaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartenaireDTO} or a {@link Page} of {@link PartenaireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartenaireQueryService extends QueryService<Partenaire> {

    private final Logger log = LoggerFactory.getLogger(PartenaireQueryService.class);

    private final PartenaireRepository partenaireRepository;

    private final PartenaireMapper partenaireMapper;

    public PartenaireQueryService(PartenaireRepository partenaireRepository, PartenaireMapper partenaireMapper) {
        this.partenaireRepository = partenaireRepository;
        this.partenaireMapper = partenaireMapper;
    }

    /**
     * Return a {@link List} of {@link PartenaireDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartenaireDTO> findByCriteria(PartenaireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Partenaire> specification = createSpecification(criteria);
        return partenaireMapper.toDto(partenaireRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PartenaireDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartenaireDTO> findByCriteria(PartenaireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Partenaire> specification = createSpecification(criteria);
        return partenaireRepository.findAll(specification, page)
            .map(partenaireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartenaireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Partenaire> specification = createSpecification(criteria);
        return partenaireRepository.count(specification);
    }

    /**
     * Function to convert PartenaireCriteria to a {@link Specification}
     */
    private Specification<Partenaire> createSpecification(PartenaireCriteria criteria) {
        Specification<Partenaire> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Partenaire_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCode(), Partenaire_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Partenaire_.libelle));
            }
            if (criteria.getFacturePartenaireEmetteurId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacturePartenaireEmetteurId(),
                    root -> root.join(Partenaire_.facturePartenaireEmetteurs, JoinType.LEFT).get(Facture_.id)));
            }
        }
        return specification;
    }
}
