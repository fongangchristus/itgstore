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
 * Criteria class for the Device entity. This class is used in DeviceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /devices?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DeviceCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deviceMark;

    private StringFilter deviceOs;

    private LongFilter deviceNumber;

    private StringFilter token;

    private LongFilter factureDevicePayeurId;

    private LongFilter operationDeviceEmetteurId;

    private LongFilter clientId;

    public DeviceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDeviceMark() {
        return deviceMark;
    }

    public void setDeviceMark(StringFilter deviceMark) {
        this.deviceMark = deviceMark;
    }

    public StringFilter getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(StringFilter deviceOs) {
        this.deviceOs = deviceOs;
    }

    public LongFilter getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(LongFilter deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public StringFilter getToken() {
        return token;
    }

    public void setToken(StringFilter token) {
        this.token = token;
    }

    public LongFilter getFactureDevicePayeurId() {
        return factureDevicePayeurId;
    }

    public void setFactureDevicePayeurId(LongFilter factureDevicePayeurId) {
        this.factureDevicePayeurId = factureDevicePayeurId;
    }

    public LongFilter getOperationDeviceEmetteurId() {
        return operationDeviceEmetteurId;
    }

    public void setOperationDeviceEmetteurId(LongFilter operationDeviceEmetteurId) {
        this.operationDeviceEmetteurId = operationDeviceEmetteurId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeviceCriteria that = (DeviceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(deviceMark, that.deviceMark) &&
            Objects.equals(deviceOs, that.deviceOs) &&
            Objects.equals(deviceNumber, that.deviceNumber) &&
            Objects.equals(token, that.token) &&
            Objects.equals(factureDevicePayeurId, that.factureDevicePayeurId) &&
            Objects.equals(operationDeviceEmetteurId, that.operationDeviceEmetteurId) &&
            Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        deviceMark,
        deviceOs,
        deviceNumber,
        token,
        factureDevicePayeurId,
        operationDeviceEmetteurId,
        clientId
        );
    }

    @Override
    public String toString() {
        return "DeviceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (deviceMark != null ? "deviceMark=" + deviceMark + ", " : "") +
                (deviceOs != null ? "deviceOs=" + deviceOs + ", " : "") +
                (deviceNumber != null ? "deviceNumber=" + deviceNumber + ", " : "") +
                (token != null ? "token=" + token + ", " : "") +
                (factureDevicePayeurId != null ? "factureDevicePayeurId=" + factureDevicePayeurId + ", " : "") +
                (operationDeviceEmetteurId != null ? "operationDeviceEmetteurId=" + operationDeviceEmetteurId + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
            "}";
    }

}
