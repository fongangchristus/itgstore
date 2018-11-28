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
 * A Partenaire.
 */
@Entity
@Table(name = "partenaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Partenaire implements Serializable {

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

    @OneToMany(mappedBy = "partenaire")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Facture> facturePartenaireEmetteurs = new HashSet<>();
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

    public Partenaire code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Partenaire libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Facture> getFacturePartenaireEmetteurs() {
        return facturePartenaireEmetteurs;
    }

    public Partenaire facturePartenaireEmetteurs(Set<Facture> factures) {
        this.facturePartenaireEmetteurs = factures;
        return this;
    }

    public Partenaire addFacturePartenaireEmetteur(Facture facture) {
        this.facturePartenaireEmetteurs.add(facture);
        facture.setPartenaire(this);
        return this;
    }

    public Partenaire removeFacturePartenaireEmetteur(Facture facture) {
        this.facturePartenaireEmetteurs.remove(facture);
        facture.setPartenaire(null);
        return this;
    }

    public void setFacturePartenaireEmetteurs(Set<Facture> factures) {
        this.facturePartenaireEmetteurs = factures;
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
        Partenaire partenaire = (Partenaire) o;
        if (partenaire.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), partenaire.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Partenaire{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
