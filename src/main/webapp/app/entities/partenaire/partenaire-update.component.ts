import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPartenaire } from 'app/shared/model/partenaire.model';
import { PartenaireService } from './partenaire.service';

@Component({
    selector: 'jhi-partenaire-update',
    templateUrl: './partenaire-update.component.html'
})
export class PartenaireUpdateComponent implements OnInit {
    partenaire: IPartenaire;
    isSaving: boolean;

    constructor(private partenaireService: PartenaireService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ partenaire }) => {
            this.partenaire = partenaire;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.partenaire.id !== undefined) {
            this.subscribeToSaveResponse(this.partenaireService.update(this.partenaire));
        } else {
            this.subscribeToSaveResponse(this.partenaireService.create(this.partenaire));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPartenaire>>) {
        result.subscribe((res: HttpResponse<IPartenaire>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
