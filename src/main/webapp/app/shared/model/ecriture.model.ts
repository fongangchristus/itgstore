export const enum SensEcriture {
    DEBIT = 'DEBIT',
    CREDIT = 'CREDIT'
}

export interface IEcriture {
    id?: number;
    montant?: number;
    libelle?: string;
    contrePartie?: string;
    sensEcriture?: SensEcriture;
    compteCode?: string;
    compteId?: number;
    transactionCode?: string;
    transactionId?: number;
}

export class Ecriture implements IEcriture {
    constructor(
        public id?: number,
        public montant?: number,
        public libelle?: string,
        public contrePartie?: string,
        public sensEcriture?: SensEcriture,
        public compteCode?: string,
        public compteId?: number,
        public transactionCode?: string,
        public transactionId?: number
    ) {}
}
