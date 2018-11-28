package com.itgstore.wallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Compte.
 */
@Entity
@Table(name = "compte")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "is_debit")
    private Boolean isDebit;

    @Column(name = "is_credit")
    private Boolean isCredit;

    @Column(name = "is_debiteur")
    private Boolean isDebiteur;

    @Column(name = "is_crediteur")
    private Boolean isCrediteur;

    @Column(name = "solde_debit")
    private Float soldeDebit;

    @Column(name = "solde_credit")
    private Float soldeCredit;

    @Column(name = "balance")
    private Float balance;

    @OneToOne    @JoinColumn(unique = true)
    private Client compteClient;

    @OneToMany(mappedBy = "compte")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ecriture> ecritureComptes = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public Compte code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Compte libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean isIsDebit() {
        return isDebit;
    }

    public Compte isDebit(Boolean isDebit) {
        this.isDebit = isDebit;
        return this;
    }

    public void setIsDebit(Boolean isDebit) {
        this.isDebit = isDebit;
    }

    public Boolean isIsCredit() {
        return isCredit;
    }

    public Compte isCredit(Boolean isCredit) {
        this.isCredit = isCredit;
        return this;
    }

    public void setIsCredit(Boolean isCredit) {
        this.isCredit = isCredit;
    }

    public Boolean isIsDebiteur() {
        return isDebiteur;
    }

    public Compte isDebiteur(Boolean isDebiteur) {
        this.isDebiteur = isDebiteur;
        return this;
    }

    public void setIsDebiteur(Boolean isDebiteur) {
        this.isDebiteur = isDebiteur;
    }

    public Boolean isIsCrediteur() {
        return isCrediteur;
    }

    public Compte isCrediteur(Boolean isCrediteur) {
        this.isCrediteur = isCrediteur;
        return this;
    }

    public void setIsCrediteur(Boolean isCrediteur) {
        this.isCrediteur = isCrediteur;
    }

    public Float getSoldeDebit() {
        return soldeDebit;
    }

    public Compte soldeDebit(Float soldeDebit) {
        this.soldeDebit = soldeDebit;
        return this;
    }

    public void setSoldeDebit(Float soldeDebit) {
        this.soldeDebit = soldeDebit;
    }

    public Float getSoldeCredit() {
        return soldeCredit;
    }

    public Compte soldeCredit(Float soldeCredit) {
        this.soldeCredit = soldeCredit;
        return this;
    }

    public void setSoldeCredit(Float soldeCredit) {
        this.soldeCredit = soldeCredit;
    }

    public Float getBalance() {
        return balance;
    }

    public Compte balance(Float balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Client getCompteClient() {
        return compteClient;
    }

    public Compte compteClient(Client client) {
        this.compteClient = client;
        return this;
    }

    public void setCompteClient(Client client) {
        this.compteClient = client;
    }

    public Set<Ecriture> getEcritureComptes() {
        return ecritureComptes;
    }

    public Compte ecritureComptes(Set<Ecriture> ecritures) {
        this.ecritureComptes = ecritures;
        return this;
    }

    public Compte addEcritureCompte(Ecriture ecriture) {
        this.ecritureComptes.add(ecriture);
        ecriture.setCompte(this);
        return this;
    }

    public Compte removeEcritureCompte(Ecriture ecriture) {
        this.ecritureComptes.remove(ecriture);
        ecriture.setCompte(null);
        return this;
    }

    public void setEcritureComptes(Set<Ecriture> ecritures) {
        this.ecritureComptes = ecritures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Compte compte = (Compte) o;
        if (compte.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), compte.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Compte{" +
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
            "}";
    }
}
