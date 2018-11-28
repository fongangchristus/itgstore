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
 * A Agence.
 */
@Entity
@Table(name = "agence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Agence implements Serializable {

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

    @OneToMany(mappedBy = "agencEmetteur")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Operation> operationAgenceEmeteurs = new HashSet<>();
    @OneToMany(mappedBy = "agencePayeur")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Operation> operationAgencePayeurs = new HashSet<>();
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

    public Agence code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Agence libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Operation> getOperationAgenceEmeteurs() {
        return operationAgenceEmeteurs;
    }

    public Agence operationAgenceEmeteurs(Set<Operation> operations) {
        this.operationAgenceEmeteurs = operations;
        return this;
    }

    public Agence addOperationAgenceEmeteur(Operation operation) {
        this.operationAgenceEmeteurs.add(operation);
        operation.setAgencEmetteur(this);
        return this;
    }

    public Agence removeOperationAgenceEmeteur(Operation operation) {
        this.operationAgenceEmeteurs.remove(operation);
        operation.setAgencEmetteur(null);
        return this;
    }

    public void setOperationAgenceEmeteurs(Set<Operation> operations) {
        this.operationAgenceEmeteurs = operations;
    }

    public Set<Operation> getOperationAgencePayeurs() {
        return operationAgencePayeurs;
    }

    public Agence operationAgencePayeurs(Set<Operation> operations) {
        this.operationAgencePayeurs = operations;
        return this;
    }

    public Agence addOperationAgencePayeur(Operation operation) {
        this.operationAgencePayeurs.add(operation);
        operation.setAgencePayeur(this);
        return this;
    }

    public Agence removeOperationAgencePayeur(Operation operation) {
        this.operationAgencePayeurs.remove(operation);
        operation.setAgencePayeur(null);
        return this;
    }

    public void setOperationAgencePayeurs(Set<Operation> operations) {
        this.operationAgencePayeurs = operations;
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
        Agence agence = (Agence) o;
        if (agence.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agence.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Agence{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
