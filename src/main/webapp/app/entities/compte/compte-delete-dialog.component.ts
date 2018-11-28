import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICompte } from 'app/shared/model/compte.model';
import { CompteService } from './compte.service';

@Component({
    selector: 'jhi-compte-delete-dialog',
    templateUrl: './compte-delete-dialog.component.html'
})
export class CompteDeleteDialogComponent {
    compte: ICompte;

    constructor(private compteService: CompteService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.compteService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'compteListModification',
                content: 'Deleted an compte'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-compte-delete-popup',
    template: ''
})
export class CompteDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ compte }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CompteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.compte = compte;
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
