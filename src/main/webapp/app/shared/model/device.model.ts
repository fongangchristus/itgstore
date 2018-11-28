import { IFacture } from 'app/shared/model//facture.model';
import { IOperation } from 'app/shared/model//operation.model';

export interface IDevice {
    id?: number;
    deviceMark?: string;
    deviceOs?: string;
    deviceNumber?: number;
    token?: string;
    factureDevicePayeurs?: IFacture[];
    operationDeviceEmetteurs?: IOperation[];
    clientName?: string;
    clientId?: number;
}

export class Device implements IDevice {
    constructor(
        public id?: number,
        public deviceMark?: string,
        public deviceOs?: string,
        public deviceNumber?: number,
        public token?: string,
        public factureDevicePayeurs?: IFacture[],
        public operationDeviceEmetteurs?: IOperation[],
        public clientName?: string,
        public clientId?: number
    ) {}
}
