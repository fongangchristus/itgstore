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

import com.itgstore.wallet.domain.enumeration.OperationType;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @Column(name = "libelle")
    private String libelle;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Float montant;

    @NotNull
    @Column(name = "date_operation", nullable = false)
    private Instant dateOperation;

    @NotNull
    @Column(name = "message_retourt", nullable = false)
    private String messageRetourt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @OneToMany(mappedBy = "operation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transaction> transactionOperations = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("operationClientEmetteurs")
    private Client client;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("operationDeviceEmetteurs")
    private Device device;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("operationAgenceEmeteurs")
    private Agence agencEmetteur;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("operationAgencePayeurs")
    private Agence agencePayeur;

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

    public Operation code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Operation libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Float getMontant() {
        return montant;
    }

    public Operation montant(Float montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Instant getDateOperation() {
        return dateOperation;
    }

    public Operation dateOperation(Instant dateOperation) {
        this.dateOperation = dateOperation;
        return this;
    }

    public void setDateOperation(Instant dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getMessageRetourt() {
        return messageRetourt;
    }

    public Operation messageRetourt(String messageRetourt) {
        this.messageRetourt = messageRetourt;
        return this;
    }

    public void setMessageRetourt(String messageRetourt) {
        this.messageRetourt = messageRetourt;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Operation operationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Set<Transaction> getTransactionOperations() {
        return transactionOperations;
    }

    public Operation transactionOperations(Set<Transaction> transactions) {
        this.transactionOperations = transactions;
        return this;
    }

    public Operation addTransactionOperation(Transaction transaction) {
        this.transactionOperations.add(transaction);
        transaction.setOperation(this);
        return this;
    }

    public Operation removeTransactionOperation(Transaction transaction) {
        this.transactionOperations.remove(transaction);
        transaction.setOperation(null);
        return this;
    }

    public void setTransactionOperations(Set<Transaction> transactions) {
        this.transactionOperations = transactions;
    }

    public Client getClient() {
        return client;
    }

    public Operation client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Device getDevice() {
        return device;
    }

    public Operation device(Device device) {
        this.device = device;
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Agence getAgencEmetteur() {
        return agencEmetteur;
    }

    public Operation agencEmetteur(Agence agence) {
        this.agencEmetteur = agence;
        return this;
    }

    public void setAgencEmetteur(Agence agence) {
        this.agencEmetteur = agence;
    }

    public Agence getAgencePayeur() {
        return agencePayeur;
    }

    public Operation agencePayeur(Agence agence) {
        this.agencePayeur = agence;
        return this;
    }

    public void setAgencePayeur(Agence agence) {
        this.agencePayeur = agence;
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
        Operation operation = (Operation) o;
        if (operation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), operation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Operation{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            ", montant=" + getMontant() +
            ", dateOperation='" + getDateOperation() + "'" +
            ", messageRetourt='" + getMessageRetourt() + "'" +
            ", operationType='" + getOperationType() + "'" +
            "}";
    }
}
