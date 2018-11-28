import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IEcriture } from 'app/shared/model/ecriture.model';
import { EcritureService } from './ecriture.service';
import { ICompte } from 'app/shared/model/compte.model';
import { CompteService } from 'app/entities/compte';
import { ITransaction } from 'app/shared/model/transaction.model';
import { TransactionService } from 'app/entities/transaction';

@Component({
    selector: 'jhi-ecriture-update',
    templateUrl: './ecriture-update.component.html'
})
export class EcritureUpdateComponent implements OnInit {
    ecriture: IEcriture;
    isSaving: boolean;

    comptes: ICompte[];

    transactions: ITransaction[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private ecritureService: EcritureService,
        private compteService: CompteService,
        private transactionService: TransactionService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ ecriture }) => {
            this.ecriture = ecriture;
        });
        this.compteService.query().subscribe(
            (res: HttpResponse<ICompte[]>) => {
                this.comptes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.transactionService.query().subscribe(
            (res: HttpResponse<ITransaction[]>) => {
                this.transactions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.ecriture.id !== undefined) {
            this.subscribeToSaveResponse(this.ecritureService.update(this.ecriture));
        } else {
            this.subscribeToSaveResponse(this.ecritureService.create(this.ecriture));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEcriture>>) {
        result.subscribe((res: HttpResponse<IEcriture>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCompteById(index: number, item: ICompte) {
        return item.id;
    }

    trackTransactionById(index: number, item: ITransaction) {
        return item.id;
    }
}
