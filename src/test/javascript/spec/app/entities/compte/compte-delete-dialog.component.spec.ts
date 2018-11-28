/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WalletTestModule } from '../../../test.module';
import { CompteDeleteDialogComponent } from 'app/entities/compte/compte-delete-dialog.component';
import { CompteService } from 'app/entities/compte/compte.service';

describe('Component Tests', () => {
    describe('Compte Management Delete Component', () => {
        let comp: CompteDeleteDialogComponent;
        let fixture: ComponentFixture<CompteDeleteDialogComponent>;
        let service: CompteService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WalletTestModule],
                declarations: [CompteDeleteDialogComponent]
            })
                .overrideTemplate(CompteDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CompteDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompteService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
