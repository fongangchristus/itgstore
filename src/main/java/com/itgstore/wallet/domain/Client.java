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
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "cni", nullable = false)
    private String cni;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Facture> factureClientRecepteurs = new HashSet<>();
    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Device> devicePlientProprietaires = new HashSet<>();
    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Operation> operationClientEmetteurs = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Client name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Client phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Client email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCni() {
        return cni;
    }

    public Client cni(String cni) {
        this.cni = cni;
        return this;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public Set<Facture> getFactureClientRecepteurs() {
        return factureClientRecepteurs;
    }

    public Client factureClientRecepteurs(Set<Facture> factures) {
        this.factureClientRecepteurs = factures;
        return this;
    }

    public Client addFactureClientRecepteur(Facture facture) {
        this.factureClientRecepteurs.add(facture);
        facture.setClient(this);
        return this;
    }

    public Client removeFactureClientRecepteur(Facture facture) {
        this.factureClientRecepteurs.remove(facture);
        facture.setClient(null);
        return this;
    }

    public void setFactureClientRecepteurs(Set<Facture> factures) {
        this.factureClientRecepteurs = factures;
    }

    public Set<Device> getDevicePlientProprietaires() {
        return devicePlientProprietaires;
    }

    public Client devicePlientProprietaires(Set<Device> devices) {
        this.devicePlientProprietaires = devices;
        return this;
    }

    public Client addDevicePlientProprietaire(Device device) {
        this.devicePlientProprietaires.add(device);
        device.setClient(this);
        return this;
    }

    public Client removeDevicePlientProprietaire(Device device) {
        this.devicePlientProprietaires.remove(device);
        device.setClient(null);
        return this;
    }

    public void setDevicePlientProprietaires(Set<Device> devices) {
        this.devicePlientProprietaires = devices;
    }

    public Set<Operation> getOperationClientEmetteurs() {
        return operationClientEmetteurs;
    }

    public Client operationClientEmetteurs(Set<Operation> operations) {
        this.operationClientEmetteurs = operations;
        return this;
    }

    public Client addOperationClientEmetteur(Operation operation) {
        this.operationClientEmetteurs.add(operation);
        operation.setClient(this);
        return this;
    }

    public Client removeOperationClientEmetteur(Operation operation) {
        this.operationClientEmetteurs.remove(operation);
        operation.setClient(null);
        return this;
    }

    public void setOperationClientEmetteurs(Set<Operation> operations) {
        this.operationClientEmetteurs = operations;
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
        Client client = (Client) o;
        if (client.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", cni='" + getCni() + "'" +
            "}";
    }
}
