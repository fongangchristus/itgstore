package com.itgstore.wallet.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Transaction entity.
 */
public class TransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer code;

    @NotNull
    private String libelle;

    @NotNull
    private Instant dateTx;

    private Long operationId;

    private String operationCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Instant getDateTx() {
        return dateTx;
    }

    public void setDateTx(Instant dateTx) {
        this.dateTx = dateTx;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (transactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            ", dateTx='" + getDateTx() + "'" +
            ", operation=" + getOperationId() +
            ", operation='" + getOperationCode() + "'" +
            "}";
    }
}
