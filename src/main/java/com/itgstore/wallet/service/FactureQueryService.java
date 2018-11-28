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

import com.itgstore.wallet.domain.Facture;
import com.itgstore.wallet.domain.*; // for static metamodels
import com.itgstore.wallet.repository.FactureRepository;
import com.itgstore.wallet.service.dto.FactureCriteria;
import com.itgstore.wallet.service.dto.FactureDTO;
import com.itgstore.wallet.service.mapper.FactureMapper;

/**
 * Service for executing complex queries for Facture entities in the database.
 * The main input is a {@link FactureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FactureDTO} or a {@link Page} of {@link FactureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactureQueryService extends QueryService<Facture> {

    private final Logger log = LoggerFactory.getLogger(FactureQueryService.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    public FactureQueryService(FactureRepository factureRepository, FactureMapper factureMapper) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
    }

    /**
     * Return a {@link List} of {@link FactureDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FactureDTO> findByCriteria(FactureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureMapper.toDto(factureRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FactureDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FactureDTO> findByCriteria(FactureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureRepository.findAll(specification, page)
            .map(factureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureRepository.count(specification);
    }

    /**
     * Function to convert FactureCriteria to a {@link Specification}
     */
    private Specification<Facture> createSpecification(FactureCriteria criteria) {
        Specification<Facture> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Facture_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Facture_.reference));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Facture_.libelle));
            }
            if (criteria.getDateCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreation(), Facture_.dateCreation));
            }
            if (criteria.getDateEmission() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEmission(), Facture_.dateEmission));
            }
            if (criteria.getDateReglement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateReglement(), Facture_.dateReglement));
            }
            if (criteria.getDateEcheance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEcheance(), Facture_.dateEcheance));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), Facture_.montant));
            }
            if (criteria.getPartenaireId() != null) {
                specification = specification.and(buildSpecification(criteria.getPartenaireId(),
                    root -> root.join(Facture_.partenaire, JoinType.LEFT).get(Partenaire_.id)));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(Facture_.client, JoinType.LEFT).get(Client_.id)));
            }
            if (criteria.getDeviceId() != null) {
                specification = specification.and(buildSpecification(criteria.getDeviceId(),
                    root -> root.join(Facture_.device, JoinType.LEFT).get(Device_.id)));
            }
        }
        return specification;
    }
}
