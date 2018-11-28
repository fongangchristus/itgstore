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
 * Criteria class for the Agence entity. This class is used in AgenceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /agences?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AgenceCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter code;

    private StringFilter libelle;

    private LongFilter operationAgenceEmeteurId;

    private LongFilter operationAgencePayeurId;

    public AgenceCriteria() {
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

    public LongFilter getOperationAgenceEmeteurId() {
        return operationAgenceEmeteurId;
    }

    public void setOperationAgenceEmeteurId(LongFilter operationAgenceEmeteurId) {
        this.operationAgenceEmeteurId = operationAgenceEmeteurId;
    }

    public LongFilter getOperationAgencePayeurId() {
        return operationAgencePayeurId;
    }

    public void setOperationAgencePayeurId(LongFilter operationAgencePayeurId) {
        this.operationAgencePayeurId = operationAgencePayeurId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AgenceCriteria that = (AgenceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(operationAgenceEmeteurId, that.operationAgenceEmeteurId) &&
            Objects.equals(operationAgencePayeurId, that.operationAgencePayeurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        code,
        libelle,
        operationAgenceEmeteurId,
        operationAgencePayeurId
        );
    }

    @Override
    public String toString() {
        return "AgenceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (operationAgenceEmeteurId != null ? "operationAgenceEmeteurId=" + operationAgenceEmeteurId + ", " : "") +
                (operationAgencePayeurId != null ? "operationAgencePayeurId=" + operationAgencePayeurId + ", " : "") +
            "}";
    }

}
