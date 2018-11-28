import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IFacture } from 'app/shared/model/facture.model';
import { FactureService } from './facture.service';
import { IPartenaire } from 'app/shared/model/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from 'app/entities/device';

@Component({
    selector: 'jhi-facture-update',
    templateUrl: './facture-update.component.html'
})
export class FactureUpdateComponent implements OnInit {
    facture: IFacture;
    isSaving: boolean;

    partenaires: IPartenaire[];

    clients: IClient[];

    devices: IDevice[];
    dateCreation: string;
    dateEmission: string;
    dateReglement: string;
    dateEcheance: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private factureService: FactureService,
        private partenaireService: PartenaireService,
        private clientService: ClientService,
        private deviceService: DeviceService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ facture }) => {
            this.facture = facture;
            this.dateCreation = this.facture.dateCreation != null ? this.facture.dateCreation.format(DATE_TIME_FORMAT) : null;
            this.dateEmission = this.facture.dateEmission != null ? this.facture.dateEmission.format(DATE_TIME_FORMAT) : null;
            this.dateReglement = this.facture.dateReglement != null ? this.facture.dateReglement.format(DATE_TIME_FORMAT) : null;
            this.dateEcheance = this.facture.dateEcheance != null ? this.facture.dateEcheance.format(DATE_TIME_FORMAT) : null;
        });
        this.partenaireService.query().subscribe(
            (res: HttpResponse<IPartenaire[]>) => {
                this.partenaires = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.facture.dateCreation = this.dateCreation != null ? moment(this.dateCreation, DATE_TIME_FORMAT) : null;
        this.facture.dateEmission = this.dateEmission != null ? moment(this.dateEmission, DATE_TIME_FORMAT) : null;
        this.facture.dateReglement = this.dateReglement != null ? moment(this.dateReglement, DATE_TIME_FORMAT) : null;
        this.facture.dateEcheance = this.dateEcheance != null ? moment(this.dateEcheance, DATE_TIME_FORMAT) : null;
        if (this.facture.id !== undefined) {
            this.subscribeToSaveResponse(this.factureService.update(this.facture));
        } else {
            this.subscribeToSaveResponse(this.factureService.create(this.facture));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFacture>>) {
        result.subscribe((res: HttpResponse<IFacture>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPartenaireById(index: number, item: IPartenaire) {
        return item.id;
    }

    trackClientById(index: number, item: IClient) {
        return item.id;
    }

    trackDeviceById(index: number, item: IDevice) {
        return item.id;
    }
}
