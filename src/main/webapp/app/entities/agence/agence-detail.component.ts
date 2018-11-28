import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgence } from 'app/shared/model/agence.model';

@Component({
    selector: 'jhi-agence-detail',
    templateUrl: './agence-detail.component.html'
})
export class AgenceDetailComponent implements OnInit {
    agence: IAgence;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ agence }) => {
            this.agence = agence;
        });
    }

    previousState() {
        window.history.back();
    }
}
