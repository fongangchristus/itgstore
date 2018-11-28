package com.itgstore.wallet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.itgstore.wallet.domain.enumeration.OperationType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Operation entity. This class is used in OperationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /operations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OperationCriteria implements Serializable {
    /**
     * Class for filtering OperationType
     */
    public static class OperationTypeFilter extends Filter<OperationType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter code;

    private StringFilter libelle;

    private FloatFilter montant;

    private InstantFilter dateOperation;

    private StringFilter messageRetourt;

    private OperationTypeFilter operationType;

    private LongFilter transactionOperationId;

    private LongFilter clientId;

    private LongFilter deviceId;

    private LongFilter agencEmetteurId;

    private LongFilter agencePayeurId;

    public OperationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCode() {
        return code;
    }

    public void setCode(IntegerFilter code) {
        this.code = code;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public FloatFilter getMontant() {
        return montant;
    }

    public void setMontant(FloatFilter montant) {
        this.montant = montant;
    }

    public InstantFilter getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(InstantFilter dateOperation) {
        this.dateOperation = dateOperation;
    }

    public StringFilter getMessageRetourt() {
        return messageRetourt;
    }

    public void setMessageRetourt(StringFilter messageRetourt) {
        this.messageRetourt = messageRetourt;
    }

    public OperationTypeFilter getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypeFilter operationType) {
        this.operationType = operationType;
    }

    public LongFilter getTransactionOperationId() {
        return transactionOperationId;
    }

    public void setTransactionOperationId(LongFilter transactionOperationId) {
        this.transactionOperationId = transactionOperationId;
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

    public LongFilter getAgencEmetteurId() {
        return agencEmetteurId;
    }

    public void setAgencEmetteurId(LongFilter agencEmetteurId) {
        this.agencEmetteurId = agencEmetteurId;
    }

    public LongFilter getAgencePayeurId() {
        return agencePayeurId;
    }

    public void setAgencePayeurId(LongFilter agencePayeurId) {
        this.agencePayeurId = agencePayeurId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OperationCriteria that = (OperationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(dateOperation, that.dateOperation) &&
            Objects.equals(messageRetourt, that.messageRetourt) &&
            Objects.equals(operationType, that.operationType) &&
            Objects.equals(transactionOperationId, that.transactionOperationId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(agencEmetteurId, that.agencEmetteurId) &&
            Objects.equals(agencePayeurId, that.agencePayeurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        code,
        libelle,
        montant,
        dateOperation,
        messageRetourt,
        operationType,
        transactionOperationId,
        clientId,
        deviceId,
        agencEmetteurId,
        agencePayeurId
        );
    }

    @Override
    public String toString() {
        return "OperationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (montant != null ? "montant=" + montant + ", " : "") +
                (dateOperation != null ? "dateOperation=" + dateOperation + ", " : "") +
                (messageRetourt != null ? "messageRetourt=" + messageRetourt + ", " : "") +
                (operationType != null ? "operationType=" + operationType + ", " : "") +
                (transactionOperationId != null ? "transactionOperationId=" + transactionOperationId + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
                (agencEmetteurId != null ? "agencEmetteurId=" + agencEmetteurId + ", " : "") +
                (agencePayeurId != null ? "agencePayeurId=" + agencePayeurId + ", " : "") +
            "}";
    }

}
