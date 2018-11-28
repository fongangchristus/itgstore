package com.itgstore.wallet.service.mapper;

import com.itgstore.wallet.domain.*;
import com.itgstore.wallet.service.dto.OperationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Operation and its DTO OperationDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, DeviceMapper.class, AgenceMapper.class})
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "device.deviceMark", target = "deviceDeviceMark")
    @Mapping(source = "agencEmetteur.id", target = "agencEmetteurId")
    @Mapping(source = "agencEmetteur.code", target = "agencEmetteurCode")
    @Mapping(source = "agencePayeur.id", target = "agencePayeurId")
    @Mapping(source = "agencePayeur.code", target = "agencePayeurCode")
    OperationDTO toDto(Operation operation);

    @Mapping(target = "transactionOperations", ignore = true)
    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "deviceId", target = "device")
    @Mapping(source = "agencEmetteurId", target = "agencEmetteur")
    @Mapping(source = "agencePayeurId", target = "agencePayeur")
    Operation toEntity(OperationDTO operationDTO);

    default Operation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Operation operation = new Operation();
        operation.setId(id);
        return operation;
    }
}
