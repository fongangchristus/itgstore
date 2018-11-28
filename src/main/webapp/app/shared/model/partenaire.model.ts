import { IFacture } from 'app/shared/model//facture.model';

export interface IPartenaire {
    id?: number;
    code?: number;
    libelle?: string;
    facturePartenaireEmetteurs?: IFacture[];
}

export class Partenaire implements IPartenaire {
    constructor(public id?: number, public code?: number, public libelle?: string, public facturePartenaireEmetteurs?: IFacture[]) {}
}
