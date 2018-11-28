package com.itgstore.wallet.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Facture entity.
 */
public class FactureDTO implements Serializable {

    private Long id;

    @NotNull
    private String reference;

    @NotNull
    private String libelle;

    @NotNull
    private Instant dateCreation;

    @NotNull
    private Instant dateEmission;

    @NotNull
    private Instant dateReglement;

    @NotNull
    private Instant dateEcheance;

    @NotNull
    private Float montant;

    private Long partenaireId;

    private String partenaireCode;

    private Long clientId;

    private String clientName;

    private Long deviceId;

    private String deviceDeviceMark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
    }

    public Instant getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(Instant dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Instant getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Instant dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Long getPartenaireId() {
        return partenaireId;
    }

    public void setPartenaireId(Long partenaireId) {
        this.partenaireId = partenaireId;
    }

    public String getPartenaireCode() {
        return partenaireCode;
    }

    public void setPartenaireCode(String partenaireCode) {
        this.partenaireCode = partenaireCode;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FactureDTO factureDTO = (FactureDTO) o;
        if (factureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), factureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FactureDTO{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateEmission='" + getDateEmission() + "'" +
            ", dateReglement='" + getDateReglement() + "'" +
            ", dateEcheance='" + getDateEcheance() + "'" +
            ", montant=" + getMontant() +
            ", partenaire=" + getPartenaireId() +
            ", partenaire='" + getPartenaireCode() + "'" +
            ", client=" + getClientId() +
            ", client='" + getClientName() + "'" +
            ", device=" + getDeviceId() +
            ", device='" + getDeviceDeviceMark() + "'" +
            "}";
    }
}
