import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPartenaire } from 'app/shared/model/partenaire.model';
import { PartenaireService } from './partenaire.service';

@Component({
    selector: 'jhi-partenaire-delete-dialog',
    templateUrl: './partenaire-delete-dialog.component.html'
})
export class PartenaireDeleteDialogComponent {
    partenaire: IPartenaire;

    constructor(private partenaireService: PartenaireService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.partenaireService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'partenaireListModification',
                content: 'Deleted an partenaire'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-partenaire-delete-popup',
    template: ''
})
export class PartenaireDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ partenaire }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PartenaireDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.partenaire = partenaire;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
