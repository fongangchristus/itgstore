import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompte } from 'app/shared/model/compte.model';

@Component({
    selector: 'jhi-compte-detail',
    templateUrl: './compte-detail.component.html'
})
export class CompteDetailComponent implements OnInit {
    compte: ICompte;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ compte }) => {
            this.compte = compte;
        });
    }

    previousState() {
        window.history.back();
    }
}
