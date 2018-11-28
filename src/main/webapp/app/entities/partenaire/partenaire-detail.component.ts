import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartenaire } from 'app/shared/model/partenaire.model';

@Component({
    selector: 'jhi-partenaire-detail',
    templateUrl: './partenaire-detail.component.html'
})
export class PartenaireDetailComponent implements OnInit {
    partenaire: IPartenaire;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ partenaire }) => {
            this.partenaire = partenaire;
        });
    }

    previousState() {
        window.history.back();
    }
}
