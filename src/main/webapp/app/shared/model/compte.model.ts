import { IEcriture } from 'app/shared/model//ecriture.model';

export interface ICompte {
    id?: number;
    code?: number;
    libelle?: string;
    isDebit?: boolean;
    isCredit?: boolean;
    isDebiteur?: boolean;
    isCrediteur?: boolean;
    soldeDebit?: number;
    soldeCredit?: number;
    balance?: number;
    compteClientId?: number;
    ecritureComptes?: IEcriture[];
}

export class Compte implements ICompte {
    constructor(
        public id?: number,
        public code?: number,
        public libelle?: string,
        public isDebit?: boolean,
        public isCredit?: boolean,
        public isDebiteur?: boolean,
        public isCrediteur?: boolean,
        public soldeDebit?: number,
        public soldeCredit?: number,
        public balance?: number,
        public compteClientId?: number,
        public ecritureComptes?: IEcriture[]
    ) {
        this.isDebit = this.isDebit || false;
        this.isCredit = this.isCredit || false;
        this.isDebiteur = this.isDebiteur || false;
        this.isCrediteur = this.isCrediteur || false;
    }
}
