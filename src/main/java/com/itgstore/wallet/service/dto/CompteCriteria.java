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
 * Criteria class for the Compte entity. This class is used in CompteResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /comptes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompteCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter code;

    private StringFilter libelle;

    private BooleanFilter isDebit;

    private BooleanFilter isCredit;

    private BooleanFilter isDebiteur;

    private BooleanFilter isCrediteur;

    private FloatFilter soldeDebit;

    private FloatFilter soldeCredit;

    private FloatFilter balance;

    private LongFilter compteClientId;

    private LongFilter ecritureCompteId;

    public CompteCriteria() {
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

    public BooleanFilter getIsDebit() {
        return isDebit;
    }

    public void setIsDebit(BooleanFilter isDebit) {
        this.isDebit = isDebit;
    }

    public BooleanFilter getIsCredit() {
        return isCredit;
    }

    public void setIsCredit(BooleanFilter isCredit) {
        this.isCredit = isCredit;
    }

    public BooleanFilter getIsDebiteur() {
        return isDebiteur;
    }

    public void setIsDebiteur(BooleanFilter isDebiteur) {
        this.isDebiteur = isDebiteur;
    }

    public BooleanFilter getIsCrediteur() {
        return isCrediteur;
    }

    public void setIsCrediteur(BooleanFilter isCrediteur) {
        this.isCrediteur = isCrediteur;
    }

    public FloatFilter getSoldeDebit() {
        return soldeDebit;
    }

    public void setSoldeDebit(FloatFilter soldeDebit) {
        this.soldeDebit = soldeDebit;
    }

    public FloatFilter getSoldeCredit() {
        return soldeCredit;
    }

    public void setSoldeCredit(FloatFilter soldeCredit) {
        this.soldeCredit = soldeCredit;
    }

    public FloatFilter getBalance() {
        return balance;
    }

    public void setBalance(FloatFilter balance) {
        this.balance = balance;
    }

    public LongFilter getCompteClientId() {
        return compteClientId;
    }

    public void setCompteClientId(LongFilter compteClientId) {
        this.compteClientId = compteClientId;
    }

    public LongFilter getEcritureCompteId() {
        return ecritureCompteId;
    }

    public void setEcritureCompteId(LongFilter ecritureCompteId) {
        this.ecritureCompteId = ecritureCompteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompteCriteria that = (CompteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(isDebit, that.isDebit) &&
            Objects.equals(isCredit, that.isCredit) &&
            Objects.equals(isDebiteur, that.isDebiteur) &&
            Objects.equals(isCrediteur, that.isCrediteur) &&
            Objects.equals(soldeDebit, that.soldeDebit) &&
            Objects.equals(soldeCredit, that.soldeCredit) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(compteClientId, that.compteClientId) &&
            Objects.equals(ecritureCompteId, that.ecritureCompteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        code,
        libelle,
        isDebit,
        isCredit,
        isDebiteur,
        isCrediteur,
        soldeDebit,
        soldeCredit,
        balance,
        compteClientId,
        ecritureCompteId
        );
    }

    @Override
    public String toString() {
        return "CompteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (isDebit != null ? "isDebit=" + isDebit + ", " : "") +
                (isCredit != null ? "isCredit=" + isCredit + ", " : "") +
                (isDebiteur != null ? "isDebiteur=" + isDebiteur + ", " : "") +
                (isCrediteur != null ? "isCrediteur=" + isCrediteur + ", " : "") +
                (soldeDebit != null ? "soldeDebit=" + soldeDebit + ", " : "") +
                (soldeCredit != null ? "soldeCredit=" + soldeCredit + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (compteClientId != null ? "compteClientId=" + compteClientId + ", " : "") +
                (ecritureCompteId != null ? "ecritureCompteId=" + ecritureCompteId + ", " : "") +
            "}";
    }

}
