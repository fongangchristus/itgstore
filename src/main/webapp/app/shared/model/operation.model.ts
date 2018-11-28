import { Moment } from 'moment';
import { ITransaction } from 'app/shared/model//transaction.model';

export const enum OperationType {
    RETRAIT = 'RETRAIT',
    RECHARGE_OM = 'RECHARGE_OM',
    RECHARGE_VISA = 'RECHARGE_VISA',
    RECHARGE_MOMO = 'RECHARGE_MOMO',
    TRANSFERT_COMPTE_A_COMPTE = 'TRANSFERT_COMPTE_A_COMPTE',
    TRANSFERT_VERS_SANS_COMPTE = 'TRANSFERT_VERS_SANS_COMPTE',
    DEPOT = 'DEPOT'
}

export interface IOperation {
    id?: number;
    code?: number;
    libelle?: string;
    montant?: number;
    dateOperation?: Moment;
    messageRetourt?: string;
    operationType?: OperationType;
    transactionOperations?: ITransaction[];
    clientName?: string;
    clientId?: number;
    deviceDeviceMark?: string;
    deviceId?: number;
    agencEmetteurCode?: string;
    agencEmetteurId?: number;
    agencePayeurCode?: string;
    agencePayeurId?: number;
}

export class Operation implements IOperation {
    constructor(
        public id?: number,
        public code?: number,
        public libelle?: string,
        public montant?: number,
        public dateOperation?: Moment,
        public messageRetourt?: string,
        public operationType?: OperationType,
        public transactionOperations?: ITransaction[],
        public clientName?: string,
        public clientId?: number,
        public deviceDeviceMark?: string,
        public deviceId?: number,
        public agencEmetteurCode?: string,
        public agencEmetteurId?: number,
        public agencePayeurCode?: string,
        public agencePayeurId?: number
    ) {}
}
