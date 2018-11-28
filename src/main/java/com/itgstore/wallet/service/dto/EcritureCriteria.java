package com.itgstore.wallet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.itgstore.wallet.domain.enumeration.SensEcriture;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Ecriture entity. This class is used in EcritureResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /ecritures?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EcritureCriteria implements Serializable {
    /**
     * Class for filtering SensEcriture
     */
    public static class SensEcritureFilter extends Filter<SensEcriture> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter montant;

    private StringFilter libelle;

    private StringFilter contrePartie;

    private SensEcritureFilter sensEcriture;

    private LongFilter compteId;

    private LongFilter transactionId;

    public EcritureCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getMontant() {
        return montant;
    }

    public void setMontant(FloatFilter montant) {
        this.montant = montant;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public StringFilter getContrePartie() {
        return contrePartie;
    }

    public void setContrePartie(StringFilter contrePartie) {
        this.contrePartie = contrePartie;
    }

    public SensEcritureFilter getSensEcriture() {
        return sensEcriture;
    }

    public void setSensEcriture(SensEcritureFilter sensEcriture) {
        this.sensEcriture = sensEcriture;
    }

    public LongFilter getCompteId() {
        return compteId;
    }

    public void setCompteId(LongFilter compteId) {
        this.compteId = compteId;
    }

    public LongFilter getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(LongFilter transactionId) {
        this.transactionId = transactionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EcritureCriteria that = (EcritureCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(contrePartie, that.contrePartie) &&
            Objects.equals(sensEcriture, that.sensEcriture) &&
            Objects.equals(compteId, that.compteId) &&
            Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        montant,
        libelle,
        contrePartie,
        sensEcriture,
        compteId,
        transactionId
        );
    }

    @Override
    public String toString() {
        return "EcritureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (montant != null ? "montant=" + montant + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (contrePartie != null ? "contrePartie=" + contrePartie + ", " : "") +
                (sensEcriture != null ? "sensEcriture=" + sensEcriture + ", " : "") +
                (compteId != null ? "compteId=" + compteId + ", " : "") +
                (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            "}";
    }

}
