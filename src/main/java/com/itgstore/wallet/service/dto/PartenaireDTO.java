package com.itgstore.wallet.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Partenaire entity.
 */
public class PartenaireDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer code;

    @NotNull
    private String libelle;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PartenaireDTO partenaireDTO = (PartenaireDTO) o;
        if (partenaireDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), partenaireDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PartenaireDTO{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
