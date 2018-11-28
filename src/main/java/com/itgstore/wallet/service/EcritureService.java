package com.itgstore.wallet.service;

import com.itgstore.wallet.domain.Ecriture;
import com.itgstore.wallet.repository.EcritureRepository;
import com.itgstore.wallet.service.dto.EcritureDTO;
import com.itgstore.wallet.service.mapper.EcritureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Ecriture.
 */
@Service
@Transactional
public class EcritureService {

    private final Logger log = LoggerFactory.getLogger(EcritureService.class);

    private final EcritureRepository ecritureRepository;

    private final EcritureMapper ecritureMapper;

    public EcritureService(EcritureRepository ecritureRepository, EcritureMapper ecritureMapper) {
        this.ecritureRepository = ecritureRepository;
        this.ecritureMapper = ecritureMapper;
    }

    /**
     * Save a ecriture.
     *
     * @param ecritureDTO the entity to save
     * @return the persisted entity
     */
    public EcritureDTO save(EcritureDTO ecritureDTO) {
        log.debug("Request to save Ecriture : {}", ecritureDTO);

        Ecriture ecriture = ecritureMapper.toEntity(ecritureDTO);
        ecriture = ecritureRepository.save(ecriture);
        return ecritureMapper.toDto(ecriture);
    }

    /**
     * Get all the ecritures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EcritureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ecritures");
        return ecritureRepository.findAll(pageable)
            .map(ecritureMapper::toDto);
    }


    /**
     * Get one ecriture by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<EcritureDTO> findOne(Long id) {
        log.debug("Request to get Ecriture : {}", id);
        return ecritureRepository.findById(id)
            .map(ecritureMapper::toDto);
    }

    /**
     * Delete the ecriture by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ecriture : {}", id);
        ecritureRepository.deleteById(id);
    }
}
