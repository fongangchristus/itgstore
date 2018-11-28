package com.itgstore.wallet.service.mapper;

import com.itgstore.wallet.domain.*;
import com.itgstore.wallet.service.dto.AgenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Agence and its DTO AgenceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgenceMapper extends EntityMapper<AgenceDTO, Agence> {


    @Mapping(target = "operationAgenceEmeteurs", ignore = true)
    @Mapping(target = "operationAgencePayeurs", ignore = true)
    Agence toEntity(AgenceDTO agenceDTO);

    default Agence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Agence agence = new Agence();
        agence.setId(id);
        return agence;
    }
}
