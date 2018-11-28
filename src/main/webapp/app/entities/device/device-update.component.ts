import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client';

@Component({
    selector: 'jhi-device-update',
    templateUrl: './device-update.component.html'
})
export class DeviceUpdateComponent implements OnInit {
    device: IDevice;
    isSaving: boolean;

    clients: IClient[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private deviceService: DeviceService,
        private clientService: ClientService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ device }) => {
            this.device = device;
        });
        this.clientService.query().subscribe(
            (res: HttpResponse<IClient[]>) => {
                this.clients = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.device.id !== undefined) {
            this.subscribeToSaveResponse(this.deviceService.update(this.device));
        } else {
            this.subscribeToSaveResponse(this.deviceService.create(this.device));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>) {
        result.subscribe((res: HttpResponse<IDevice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
