package com.itgstore.wallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.itgstore.wallet.domain.enumeration.SensEcriture;

/**
 * Ecriture entity.
 * @author King83.
 */
@ApiModel(description = "Ecriture entity. @author King83.")
@Entity
@Table(name = "ecriture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ecriture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Float montant;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "contre_partie")
    private String contrePartie;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sens_ecriture", nullable = false)
    private SensEcriture sensEcriture;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ecritureComptes")
    private Compte compte;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ecritureTransactions")
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMontant() {
        return montant;
    }

    public Ecriture montant(Float montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public String getLibelle() {
        return libelle;
    }

    public Ecriture libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getContrePartie() {
        return contrePartie;
    }

    public Ecriture contrePartie(String contrePartie) {
        this.contrePartie = contrePartie;
        return this;
    }

    public void setContrePartie(String contrePartie) {
        this.contrePartie = contrePartie;
    }

    public SensEcriture getSensEcriture() {
        return sensEcriture;
    }

    public Ecriture sensEcriture(SensEcriture sensEcriture) {
        this.sensEcriture = sensEcriture;
        return this;
    }

    public void setSensEcriture(SensEcriture sensEcriture) {
        this.sensEcriture = sensEcriture;
    }

    public Compte getCompte() {
        return compte;
    }

    public Ecriture compte(Compte compte) {
        this.compte = compte;
        return this;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Ecriture transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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
        Ecriture ecriture = (Ecriture) o;
        if (ecriture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ecriture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ecriture{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", libelle='" + getLibelle() + "'" +
            ", contrePartie='" + getContrePartie() + "'" +
            ", sensEcriture='" + getSensEcriture() + "'" +
            "}";
    }
}
