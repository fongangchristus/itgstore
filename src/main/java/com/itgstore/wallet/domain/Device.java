package com.itgstore.wallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_mark")
    private String deviceMark;

    @Column(name = "device_os")
    private String deviceOs;

    @Column(name = "device_number")
    private Long deviceNumber;

    @NotNull
    @Column(name = "token", nullable = false)
    private String token;

    @OneToMany(mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Facture> factureDevicePayeurs = new HashSet<>();
    @OneToMany(mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Operation> operationDeviceEmetteurs = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("devicePlientProprietaires")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceMark() {
        return deviceMark;
    }

    public Device deviceMark(String deviceMark) {
        this.deviceMark = deviceMark;
        return this;
    }

    public void setDeviceMark(String deviceMark) {
        this.deviceMark = deviceMark;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public Device deviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
        return this;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public Long getDeviceNumber() {
        return deviceNumber;
    }

    public Device deviceNumber(Long deviceNumber) {
        this.deviceNumber = deviceNumber;
        return this;
    }

    public void setDeviceNumber(Long deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getToken() {
        return token;
    }

    public Device token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Facture> getFactureDevicePayeurs() {
        return factureDevicePayeurs;
    }

    public Device factureDevicePayeurs(Set<Facture> factures) {
        this.factureDevicePayeurs = factures;
        return this;
    }

    public Device addFactureDevicePayeur(Facture facture) {
        this.factureDevicePayeurs.add(facture);
        facture.setDevice(this);
        return this;
    }

    public Device removeFactureDevicePayeur(Facture facture) {
        this.factureDevicePayeurs.remove(facture);
        facture.setDevice(null);
        return this;
    }

    public void setFactureDevicePayeurs(Set<Facture> factures) {
        this.factureDevicePayeurs = factures;
    }

    public Set<Operation> getOperationDeviceEmetteurs() {
        return operationDeviceEmetteurs;
    }

    public Device operationDeviceEmetteurs(Set<Operation> operations) {
        this.operationDeviceEmetteurs = operations;
        return this;
    }

    public Device addOperationDeviceEmetteur(Operation operation) {
        this.operationDeviceEmetteurs.add(operation);
        operation.setDevice(this);
        return this;
    }

    public Device removeOperationDeviceEmetteur(Operation operation) {
        this.operationDeviceEmetteurs.remove(operation);
        operation.setDevice(null);
        return this;
    }

    public void setOperationDeviceEmetteurs(Set<Operation> operations) {
        this.operationDeviceEmetteurs = operations;
    }

    public Client getClient() {
        return client;
    }

    public Device client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
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
        Device device = (Device) o;
        if (device.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), device.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", deviceMark='" + getDeviceMark() + "'" +
            ", deviceOs='" + getDeviceOs() + "'" +
            ", deviceNumber=" + getDeviceNumber() +
            ", token='" + getToken() + "'" +
            "}";
    }
}
