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

/**
 * Criteria class for the Client entity. This class is used in ClientResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /clients?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClientCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter phoneNumber;

    private StringFilter email;

    private StringFilter cni;

    private LongFilter factureClientRecepteurId;

    private LongFilter devicePlientProprietaireId;

    private LongFilter operationClientEmetteurId;

    public ClientCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getCni() {
        return cni;
    }

    public void setCni(StringFilter cni) {
        this.cni = cni;
    }

    public LongFilter getFactureClientRecepteurId() {
        return factureClientRecepteurId;
    }

    public void setFactureClientRecepteurId(LongFilter factureClientRecepteurId) {
        this.factureClientRecepteurId = factureClientRecepteurId;
    }

    public LongFilter getDevicePlientProprietaireId() {
        return devicePlientProprietaireId;
    }

    public void setDevicePlientProprietaireId(LongFilter devicePlientProprietaireId) {
        this.devicePlientProprietaireId = devicePlientProprietaireId;
    }

    public LongFilter getOperationClientEmetteurId() {
        return operationClientEmetteurId;
    }

    public void setOperationClientEmetteurId(LongFilter operationClientEmetteurId) {
        this.operationClientEmetteurId = operationClientEmetteurId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClientCriteria that = (ClientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(cni, that.cni) &&
            Objects.equals(factureClientRecepteurId, that.factureClientRecepteurId) &&
            Objects.equals(devicePlientProprietaireId, that.devicePlientProprietaireId) &&
            Objects.equals(operationClientEmetteurId, that.operationClientEmetteurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        phoneNumber,
        email,
        cni,
        factureClientRecepteurId,
        devicePlientProprietaireId,
        operationClientEmetteurId
        );
    }

    @Override
    public String toString() {
        return "ClientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (cni != null ? "cni=" + cni + ", " : "") +
                (factureClientRecepteurId != null ? "factureClientRecepteurId=" + factureClientRecepteurId + ", " : "") +
                (devicePlientProprietaireId != null ? "devicePlientProprietaireId=" + devicePlientProprietaireId + ", " : "") +
                (operationClientEmetteurId != null ? "operationClientEmetteurId=" + operationClientEmetteurId + ", " : "") +
            "}";
    }

}
