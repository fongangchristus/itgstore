package com.itgstore.wallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaction implements Serializable {

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

    @NotNull
    @Column(name = "date_tx", nullable = false)
    private Instant dateTx;

    @OneToMany(mappedBy = "transaction")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ecriture> ecritureTransactions = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("transactionOperations")
    private Operation operation;

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

    public Transaction code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Transaction libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Instant getDateTx() {
        return dateTx;
    }

    public Transaction dateTx(Instant dateTx) {
        this.dateTx = dateTx;
        return this;
    }

    public void setDateTx(Instant dateTx) {
        this.dateTx = dateTx;
    }

    public Set<Ecriture> getEcritureTransactions() {
        return ecritureTransactions;
    }

    public Transaction ecritureTransactions(Set<Ecriture> ecritures) {
        this.ecritureTransactions = ecritures;
        return this;
    }

    public Transaction addEcritureTransaction(Ecriture ecriture) {
        this.ecritureTransactions.add(ecriture);
        ecriture.setTransaction(this);
        return this;
    }

    public Transaction removeEcritureTransaction(Ecriture ecriture) {
        this.ecritureTransactions.remove(ecriture);
        ecriture.setTransaction(null);
        return this;
    }

    public void setEcritureTransactions(Set<Ecriture> ecritures) {
        this.ecritureTransactions = ecritures;
    }

    public Operation getOperation() {
        return operation;
    }

    public Transaction operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
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
        Transaction transaction = (Transaction) o;
        if (transaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            ", dateTx='" + getDateTx() + "'" +
            "}";
    }
}
