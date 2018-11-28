package com.itgstore.wallet.service.mapper;

import com.itgstore.wallet.domain.*;
import com.itgstore.wallet.service.dto.CompteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Compte and its DTO CompteDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface CompteMapper extends EntityMapper<CompteDTO, Compte> {

    @Mapping(source = "compteClient.id", target = "compteClientId")
    CompteDTO toDto(Compte compte);

    @Mapping(source = "compteClientId", target = "compteClient")
    @Mapping(target = "ecritureComptes", ignore = true)
    Compte toEntity(CompteDTO compteDTO);

    default Compte fromId(Long id) {
        if (id == null) {
            return null;
        }
        Compte compte = new Compte();
        compte.setId(id);
        return compte;
    }
}
