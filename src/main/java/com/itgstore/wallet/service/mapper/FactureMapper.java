package com.itgstore.wallet.service.mapper;

import com.itgstore.wallet.domain.*;
import com.itgstore.wallet.service.dto.FactureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Facture and its DTO FactureDTO.
 */
@Mapper(componentModel = "spring", uses = {PartenaireMapper.class, ClientMapper.class, DeviceMapper.class})
public interface FactureMapper extends EntityMapper<FactureDTO, Facture> {

    @Mapping(source = "partenaire.id", target = "partenaireId")
    @Mapping(source = "partenaire.code", target = "partenaireCode")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "device.deviceMark", target = "deviceDeviceMark")
    FactureDTO toDto(Facture facture);

    @Mapping(source = "partenaireId", target = "partenaire")
    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "deviceId", target = "device")
    Facture toEntity(FactureDTO factureDTO);

    default Facture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Facture facture = new Facture();
        facture.setId(id);
        return facture;
    }
}
