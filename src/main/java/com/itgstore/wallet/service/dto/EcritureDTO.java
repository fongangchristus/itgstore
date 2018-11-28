package com.itgstore.wallet.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.itgstore.wallet.domain.enumeration.SensEcriture;

/**
 * A DTO for the Ecriture entity.
 */
public class EcritureDTO implements Serializable {

    private Long id;

    @NotNull
    private Float montant;

    @NotNull
    private String libelle;

    private String contrePartie;

    @NotNull
    private SensEcriture sensEcriture;

    private Long compteId;

    private String compteCode;

    private Long transactionId;

    private String transactionCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getContrePartie() {
        return contrePartie;
    }

    public void setContrePartie(String contrePartie) {
        this.contrePartie = contrePartie;
    }

    public SensEcriture getSensEcriture() {
        return sensEcriture;
    }

    public void setSensEcriture(SensEcriture sensEcriture) {
        this.sensEcriture = sensEcriture;
    }

    public Long getCompteId() {
        return compteId;
    }

    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }

    public String getCompteCode() {
        return compteCode;
    }

    public void setCompteCode(String compteCode) {
        this.compteCode = compteCode;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EcritureDTO ecritureDTO = (EcritureDTO) o;
        if (ecritureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ecritureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EcritureDTO{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", libelle='" + getLibelle() + "'" +
            ", contrePartie='" + getContrePartie() + "'" +
            ", sensEcriture='" + getSensEcriture() + "'" +
            ", compte=" + getCompteId() +
            ", compte='" + getCompteCode() + "'" +
            ", transaction=" + getTransactionId() +
            ", transaction='" + getTransactionCode() + "'" +
            "}";
    }
}
