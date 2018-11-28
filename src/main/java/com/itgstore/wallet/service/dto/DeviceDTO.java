package com.itgstore.wallet.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Device entity.
 */
public class DeviceDTO implements Serializable {

    private Long id;

    private String deviceMark;

    private String deviceOs;

    private Long deviceNumber;

    @NotNull
    private String token;

    private Long clientId;

    private String clientName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceMark() {
        return deviceMark;
    }

    public void setDeviceMark(String deviceMark) {
        this.deviceMark = deviceMark;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public Long getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(Long deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (deviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id=" + getId() +
            ", deviceMark='" + getDeviceMark() + "'" +
            ", deviceOs='" + getDeviceOs() + "'" +
            ", deviceNumber=" + getDeviceNumber() +
            ", token='" + getToken() + "'" +
            ", client=" + getClientId() +
            ", client='" + getClientName() + "'" +
            "}";
    }
}
