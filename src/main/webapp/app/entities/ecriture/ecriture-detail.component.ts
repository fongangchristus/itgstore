import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEcriture } from 'app/shared/model/ecriture.model';

@Component({
    selector: 'jhi-ecriture-detail',
    templateUrl: './ecriture-detail.component.html'
})
export class EcritureDetailComponent implements OnInit {
    ecriture: IEcriture;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ecriture }) => {
            this.ecriture = ecriture;
        });
    }

    previousState() {
        window.history.back();
    }
}
