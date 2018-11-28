package com.itgstore.wallet.service;

import com.itgstore.wallet.domain.Partenaire;
import com.itgstore.wallet.repository.PartenaireRepository;
import com.itgstore.wallet.service.dto.PartenaireDTO;
import com.itgstore.wallet.service.mapper.PartenaireMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Partenaire.
 */
@Service
@Transactional
public class PartenaireService {

    private final Logger log = LoggerFactory.getLogger(PartenaireService.class);

    private final PartenaireRepository partenaireRepository;

    private final PartenaireMapper partenaireMapper;

    public PartenaireService(PartenaireRepository partenaireRepository, PartenaireMapper partenaireMapper) {
        this.partenaireRepository = partenaireRepository;
        this.partenaireMapper = partenaireMapper;
    }

    /**
     * Save a partenaire.
     *
     * @param partenaireDTO the entity to save
     * @return the persisted entity
     */
    public PartenaireDTO save(PartenaireDTO partenaireDTO) {
        log.debug("Request to save Partenaire : {}", partenaireDTO);

        Partenaire partenaire = partenaireMapper.toEntity(partenaireDTO);
        partenaire = partenaireRepository.save(partenaire);
        return partenaireMapper.toDto(partenaire);
    }

    /**
     * Get all the partenaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PartenaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Partenaires");
        return partenaireRepository.findAll(pageable)
            .map(partenaireMapper::toDto);
    }


    /**
     * Get one partenaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PartenaireDTO> findOne(Long id) {
        log.debug("Request to get Partenaire : {}", id);
        return partenaireRepository.findById(id)
            .map(partenaireMapper::toDto);
    }

    /**
     * Delete the partenaire by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Partenaire : {}", id);
        partenaireRepository.deleteById(id);
    }
}
