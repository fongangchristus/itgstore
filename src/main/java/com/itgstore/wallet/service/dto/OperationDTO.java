package com.itgstore.wallet.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.itgstore.wallet.domain.enumeration.OperationType;

/**
 * A DTO for the Operation entity.
 */
public class OperationDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer code;

    private String libelle;

    @NotNull
    private Float montant;

    @NotNull
    private Instant dateOperation;

    @NotNull
    private String messageRetourt;

    @NotNull
    private OperationType operationType;

    private Long clientId;

    private String clientName;

    private Long deviceId;

    private String deviceDeviceMark;

    private Long agencEmetteurId;

    private String agencEmetteurCode;

    private Long agencePayeurId;

    private String agencePayeurCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Instant getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(Instant dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getMessageRetourt() {
        return messageRetourt;
    }

    public void setMessageRetourt(String messageRetourt) {
        this.messageRetourt = messageRetourt;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceDeviceMark() {
        return deviceDeviceMark;
    }

    public void setDeviceDeviceMark(String deviceDeviceMark) {
        this.deviceDeviceMark = deviceDeviceMark;
    }

    public Long getAgencEmetteurId() {
        return agencEmetteurId;
    }

    public void setAgencEmetteurId(Long agenceId) {
        this.agencEmetteurId = agenceId;
    }

    public String getAgencEmetteurCode() {
        return agencEmetteurCode;
    }

    public void setAgencEmetteurCode(String agenceCode) {
        this.agencEmetteurCode = agenceCode;
    }

    public Long getAgencePayeurId() {
        return agencePayeurId;
    }

    public void setAgencePayeurId(Long agenceId) {
        this.agencePayeurId = agenceId;
    }

    public String getAgencePayeurCode() {
        return agencePayeurCode;
    }

    public void setAgencePayeurCode(String agenceCode) {
        this.agencePayeurCode = agenceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OperationDTO operationDTO = (OperationDTO) o;
        if (operationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), operationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OperationDTO{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            ", montant=" + getMontant() +
            ", dateOperation='" + getDateOperation() + "'" +
            ", messageRetourt='" + getMessageRetourt() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", client=" + getClientId() +
            ", client='" + getClientName() + "'" +
            ", device=" + getDeviceId() +
            ", device='" + getDeviceDeviceMark() + "'" +
            ", agencEmetteur=" + getAgencEmetteurId() +
            ", agencEmetteur='" + getAgencEmetteurCode() + "'" +
            ", agencePayeur=" + getAgencePayeurId() +
            ", agencePayeur='" + getAgencePayeurCode() + "'" +
            "}";
    }
}
