import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IOperation } from 'app/shared/model/operation.model';
import { OperationService } from './operation.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from 'app/entities/device';
import { IAgence } from 'app/shared/model/agence.model';
import { AgenceService } from 'app/entities/agence';

@Component({
    selector: 'jhi-operation-update',
    templateUrl: './operation-update.component.html'
})
export class OperationUpdateComponent implements OnInit {
    operation: IOperation;
    isSaving: boolean;

    clients: IClient[];

    devices: IDevice[];

    agences: IAgence[];
    dateOperation: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private operationService: OperationService,
        private clientService: ClientService,
        private deviceService: DeviceService,
        private agenceService: AgenceService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ operation }) => {
            this.operation = operation;
            this.dateOperation = this.operation.dateOperation != null ? this.operation.dateOperation.format(DATE_TIME_FORMAT) : null;
        });
        this.clientService.query().subscribe(
            (res: HttpResponse<IClient[]>) => {
                this.clients = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.deviceService.query().subscribe(
            (res: HttpResponse<IDevice[]>) => {
                this.devices = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.agenceService.query().subscribe(
            (res: HttpResponse<IAgence[]>) => {
                this.agences = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.operation.dateOperation = this.dateOperation != null ? moment(this.dateOperation, DATE_TIME_FORMAT) : null;
        if (this.operation.id !== undefined) {
            this.subscribeToSaveResponse(this.operationService.update(this.operation));
        } else {
            this.subscribeToSaveResponse(this.operationService.create(this.operation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOperation>>) {
        result.subscribe((res: HttpResponse<IOperation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackClientById(index: number, item: IClient) {
        return item.id;
    }

    trackDeviceById(index: number, item: IDevice) {
        return item.id;
    }

    trackAgenceById(index: number, item: IAgence) {
        return item.id;
    }
}
