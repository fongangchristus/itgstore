import { IOperation } from 'app/shared/model//operation.model';

export interface IAgence {
    id?: number;
    code?: number;
    libelle?: string;
    operationAgenceEmeteurs?: IOperation[];
    operationAgencePayeurs?: IOperation[];
}

export class Agence implements IAgence {
    constructor(
        public id?: number,
        public code?: number,
        public libelle?: string,
        public operationAgenceEmeteurs?: IOperation[],
        public operationAgencePayeurs?: IOperation[]
    ) {}
}
