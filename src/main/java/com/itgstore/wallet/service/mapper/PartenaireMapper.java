package com.itgstore.wallet.service.mapper;

import com.itgstore.wallet.domain.*;
import com.itgstore.wallet.service.dto.PartenaireDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Partenaire and its DTO PartenaireDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PartenaireMapper extends EntityMapper<PartenaireDTO, Partenaire> {


    @Mapping(target = "facturePartenaireEmetteurs", ignore = true)
    Partenaire toEntity(PartenaireDTO partenaireDTO);

    default Partenaire fromId(Long id) {
        if (id == null) {
            return null;
        }
        Partenaire partenaire = new Partenaire();
        partenaire.setId(id);
        return partenaire;
    }
}
