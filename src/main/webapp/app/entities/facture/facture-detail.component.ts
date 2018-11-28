import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFacture } from 'app/shared/model/facture.model';

@Component({
    selector: 'jhi-facture-detail',
    templateUrl: './facture-detail.component.html'
})
export class FactureDetailComponent implements OnInit {
    facture: IFacture;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ facture }) => {
            this.facture = facture;
        });
    }

    previousState() {
        window.history.back();
    }
}
