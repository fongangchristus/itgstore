import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFacture } from 'app/shared/model/facture.model';
import { FactureService } from './facture.service';

@Component({
    selector: 'jhi-facture-delete-dialog',
    templateUrl: './facture-delete-dialog.component.html'
})
export class FactureDeleteDialogComponent {
    facture: IFacture;

    constructor(private factureService: FactureService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.factureService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'factureListModification',
                content: 'Deleted an facture'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-facture-delete-popup',
    template: ''
})
export class FactureDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ facture }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FactureDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.facture = facture;
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
