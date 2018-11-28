package com.itgstore.wallet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Facture entity. This class is used in FactureResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /factures?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FactureCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reference;

    private StringFilter libelle;

    private InstantFilter dateCreation;

    private InstantFilter dateEmission;

    private InstantFilter dateReglement;

    private InstantFilter dateEcheance;

    private FloatFilter montant;

    private LongFilter partenaireId;

    private LongFilter clientId;

    private LongFilter deviceId;

    public FactureCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReference() {
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public InstantFilter getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(InstantFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public InstantFilter getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(InstantFilter dateEmission) {
        this.dateEmission = dateEmission;
    }

    public InstantFilter getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(InstantFilter dateReglement) {
        this.dateReglement = dateReglement;
    }

    public InstantFilter getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(InstantFilter dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public FloatFilter getMontant() {
        return montant;
    }

    public void setMontant(FloatFilter montant) {
        this.montant = montant;
    }

    public LongFilter getPartenaireId() {
        return partenaireId;
    }

    public void setPartenaireId(LongFilter partenaireId) {
        this.partenaireId = partenaireId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(LongFilter deviceId) {
        this.deviceId = deviceId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FactureCriteria that = (FactureCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(dateEmission, that.dateEmission) &&
            Objects.equals(dateReglement, that.dateReglement) &&
            Objects.equals(dateEcheance, that.dateEcheance) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(partenaireId, that.partenaireId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        reference,
        libelle,
        dateCreation,
        dateEmission,
        dateReglement,
        dateEcheance,
        montant,
        partenaireId,
        clientId,
        deviceId
        );
    }

    @Override
    public String toString() {
        return "FactureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reference != null ? "reference=" + reference + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
                (dateEmission != null ? "dateEmission=" + dateEmission + ", " : "") +
                (dateReglement != null ? "dateReglement=" + dateReglement + ", " : "") +
                (dateEcheance != null ? "dateEcheance=" + dateEcheance + ", " : "") +
                (montant != null ? "montant=" + montant + ", " : "") +
                (partenaireId != null ? "partenaireId=" + partenaireId + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            "}";
    }

}
