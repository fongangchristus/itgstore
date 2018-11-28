import { Moment } from 'moment';

export interface IFacture {
    id?: number;
    reference?: string;
    libelle?: string;
    dateCreation?: Moment;
    dateEmission?: Moment;
    dateReglement?: Moment;
    dateEcheance?: Moment;
    montant?: number;
    partenaireCode?: string;
    partenaireId?: number;
    clientName?: string;
    clientId?: number;
    deviceDeviceMark?: string;
    deviceId?: number;
}

export class Facture implements IFacture {
    constructor(
        public id?: number,
        public reference?: string,
        public libelle?: string,
        public dateCreation?: Moment,
        public dateEmission?: Moment,
        public dateReglement?: Moment,
        public dateEcheance?: Moment,
        public montant?: number,
        public partenaireCode?: string,
        public partenaireId?: number,
        public clientName?: string,
        public clientId?: number,
        public deviceDeviceMark?: string,
        public deviceId?: number
    ) {}
}
