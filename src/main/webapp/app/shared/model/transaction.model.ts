import { Moment } from 'moment';
import { IEcriture } from 'app/shared/model//ecriture.model';

export interface ITransaction {
    id?: number;
    code?: number;
    libelle?: string;
    dateTx?: Moment;
    ecritureTransactions?: IEcriture[];
    operationCode?: string;
    operationId?: number;
}

export class Transaction implements ITransaction {
    constructor(
        public id?: number,
        public code?: number,
        public libelle?: string,
        public dateTx?: Moment,
        public ecritureTransactions?: IEcriture[],
        public operationCode?: string,
        public operationId?: number
    ) {}
}
