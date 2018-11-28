import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAgence } from 'app/shared/model/agence.model';
import { AgenceService } from './agence.service';

@Component({
    selector: 'jhi-agence-delete-dialog',
    templateUrl: './agence-delete-dialog.component.html'
})
export class AgenceDeleteDialogComponent {
    agence: IAgence;

    constructor(private agenceService: AgenceService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.agenceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'agenceListModification',
                content: 'Deleted an agence'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-agence-delete-popup',
    template: ''
})
export class AgenceDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ agence }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AgenceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.agence = agence;
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
