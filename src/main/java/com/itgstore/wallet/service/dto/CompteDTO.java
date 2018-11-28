package com.itgstore.wallet.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Compte entity.
 */
public class CompteDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer code;

    @NotNull
    private String libelle;

    private Boolean isDebit;

    private Boolean isCredit;

    private Boolean isDebiteur;

    private Boolean isCrediteur;

    private Float soldeDebit;

    private Float soldeCredit;

    private Float balance;

    private Long compteClientId;

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

    public Boolean isIsDebit() {
        return isDebit;
    }

    public void setIsDebit(Boolean isDebit) {
        this.isDebit = isDebit;
    }

    public Boolean isIsCredit() {
        return isCredit;
    }

    public void setIsCredit(Boolean isCredit) {
        this.isCredit = isCredit;
    }

    public Boolean isIsDebiteur() {
        return isDebiteur;
    }

    public void setIsDebiteur(Boolean isDebiteur) {
        this.isDebiteur = isDebiteur;
    }

    public Boolean isIsCrediteur() {
        return isCrediteur;
    }

    public void setIsCrediteur(Boolean isCrediteur) {
        this.isCrediteur = isCrediteur;
    }

    public Float getSoldeDebit() {
        return soldeDebit;
    }

    public void setSoldeDebit(Float soldeDebit) {
        this.soldeDebit = soldeDebit;
    }

    public Float getSoldeCredit() {
        return soldeCredit;
    }

    public void setSoldeCredit(Float soldeCredit) {
        this.soldeCredit = soldeCredit;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Long getCompteClientId() {
        return compteClientId;
    }

    public void setCompteClientId(Long clientId) {
        this.compteClientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompteDTO compteDTO = (CompteDTO) o;
        if (compteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), compteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompteDTO{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            ", isDebit='" + isIsDebit() + "'" +
            ", isCredit='" + isIsCredit() + "'" +
            ", isDebiteur='" + isIsDebiteur() + "'" +
            ", isCrediteur='" + isIsCrediteur() + "'" +
            ", soldeDebit=" + getSoldeDebit() +
            ", soldeCredit=" + getSoldeCredit() +
            ", balance=" + getBalance() +
            ", compteClient=" + getCompteClientId() +
            "}";
    }
}
