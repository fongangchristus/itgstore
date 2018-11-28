package com.itgstore.wallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @NotNull
    @Column(name = "date_emission", nullable = false)
    private Instant dateEmission;

    @NotNull
    @Column(name = "date_reglement", nullable = false)
    private Instant dateReglement;

    @NotNull
    @Column(name = "date_echeance", nullable = false)
    private Instant dateEcheance;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Float montant;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("facturePartenaireEmetteurs")
    private Partenaire partenaire;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("factureClientRecepteurs")
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("factureDevicePayeurs")
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public Facture reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public Facture libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public Facture dateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateEmission() {
        return dateEmission;
    }

    public Facture dateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
        return this;
    }

    public void setDateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
    }

    public Instant getDateReglement() {
        return dateReglement;
    }

    public Facture dateReglement(Instant dateReglement) {
        this.dateReglement = dateReglement;
        return this;
    }

    public void setDateReglement(Instant dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Instant getDateEcheance() {
        return dateEcheance;
    }

    public Facture dateEcheance(Instant dateEcheance) {
        this.dateEcheance = dateEcheance;
        return this;
    }

    public void setDateEcheance(Instant dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Float getMontant() {
        return montant;
    }

    public Facture montant(Float montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public Facture partenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
        return this;
    }

    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }

    public Client getClient() {
        return client;
    }

    public Facture client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Device getDevice() {
        return device;
    }

    public Facture device(Device device) {
        this.device = device;
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
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
        Facture facture = (Facture) o;
        if (facture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), facture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateEmission='" + getDateEmission() + "'" +
            ", dateReglement='" + getDateReglement() + "'" +
            ", dateEcheance='" + getDateEcheance() + "'" +
            ", montant=" + getMontant() +
            "}";
    }
}
