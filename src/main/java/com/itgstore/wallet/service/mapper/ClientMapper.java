package com.itgstore.wallet.service.mapper;

import com.itgstore.wallet.domain.*;
import com.itgstore.wallet.service.dto.ClientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Client and its DTO ClientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {


    @Mapping(target = "factureClientRecepteurs", ignore = true)
    @Mapping(target = "devicePlientProprietaires", ignore = true)
    @Mapping(target = "operationClientEmetteurs", ignore = true)
    Client toEntity(ClientDTO clientDTO);

    default Client fromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
