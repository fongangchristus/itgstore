import { IFacture } from 'app/shared/model//facture.model';
import { IDevice } from 'app/shared/model//device.model';
import { IOperation } from 'app/shared/model//operation.model';

export interface IClient {
    id?: number;
    name?: string;
    phoneNumber?: string;
    email?: string;
    cni?: string;
    factureClientRecepteurs?: IFacture[];
    devicePlientProprietaires?: IDevice[];
    operationClientEmetteurs?: IOperation[];
}

export class Client implements IClient {
    constructor(
        public id?: number,
        public name?: string,
        public phoneNumber?: string,
        public email?: string,
        public cni?: string,
        public factureClientRecepteurs?: IFacture[],
        public devicePlientProprietaires?: IDevice[],
        public operationClientEmetteurs?: IOperation[]
    ) {}
}
