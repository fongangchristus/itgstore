package com.itgstore.wallet.service.mapper;

import com.itgstore.wallet.domain.*;
import com.itgstore.wallet.service.dto.EcritureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Ecriture and its DTO EcritureDTO.
 */
@Mapper(componentModel = "spring", uses = {CompteMapper.class, TransactionMapper.class})
public interface EcritureMapper extends EntityMapper<EcritureDTO, Ecriture> {

    @Mapping(source = "compte.id", target = "compteId")
    @Mapping(source = "compte.code", target = "compteCode")
    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "transaction.code", target = "transactionCode")
    EcritureDTO toDto(Ecriture ecriture);

    @Mapping(source = "compteId", target = "compte")
    @Mapping(source = "transactionId", target = "transaction")
    Ecriture toEntity(EcritureDTO ecritureDTO);

    default Ecriture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ecriture ecriture = new Ecriture();
        ecriture.setId(id);
        return ecriture;
    }
}
