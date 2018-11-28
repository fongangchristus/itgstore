import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAgence } from 'app/shared/model/agence.model';
import { AgenceService } from './agence.service';

@Component({
    selector: 'jhi-agence-update',
    templateUrl: './agence-update.component.html'
})
export class AgenceUpdateComponent implements OnInit {
    agence: IAgence;
    isSaving: boolean;

    constructor(private agenceService: AgenceService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ agence }) => {
            this.agence = agence;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.agence.id !== undefined) {
            this.subscribeToSaveResponse(this.agenceService.update(this.agence));
        } else {
            this.subscribeToSaveResponse(this.agenceService.create(this.agence));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAgence>>) {
        result.subscribe((res: HttpResponse<IAgence>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
