import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICompte } from 'app/shared/model/compte.model';
import { CompteService } from './compte.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client';

@Component({
    selector: 'jhi-compte-update',
    templateUrl: './compte-update.component.html'
})
export class CompteUpdateComponent implements OnInit {
    compte: ICompte;
    isSaving: boolean;

    compteclients: IClient[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private compteService: CompteService,
        private clientService: ClientService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ compte }) => {
            this.compte = compte;
        });
        this.clientService.query({ 'compteId.specified': 'false' }).subscribe(
            (res: HttpResponse<IClient[]>) => {
                if (!this.compte.compteClientId) {
                    this.compteclients = res.body;
                } else {
                    this.clientService.find(this.compte.compteClientId).subscribe(
                        (subRes: HttpResponse<IClient>) => {
                            this.compteclients = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.compte.id !== undefined) {
            this.subscribeToSaveResponse(this.compteService.update(this.compte));
        } else {
            this.subscribeToSaveResponse(this.compteService.create(this.compte));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICompte>>) {
        result.subscribe((res: HttpResponse<ICompte>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
