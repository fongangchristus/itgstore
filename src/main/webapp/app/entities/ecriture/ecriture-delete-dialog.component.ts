import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEcriture } from 'app/shared/model/ecriture.model';
import { EcritureService } from './ecriture.service';

@Component({
    selector: 'jhi-ecriture-delete-dialog',
    templateUrl: './ecriture-delete-dialog.component.html'
})
export class EcritureDeleteDialogComponent {
    ecriture: IEcriture;

    constructor(private ecritureService: EcritureService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ecritureService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ecritureListModification',
                content: 'Deleted an ecriture'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ecriture-delete-popup',
    template: ''
})
export class EcritureDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ecriture }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(EcritureDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.ecriture = ecriture;
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
