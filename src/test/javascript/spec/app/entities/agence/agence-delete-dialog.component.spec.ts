/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WalletTestModule } from '../../../test.module';
import { AgenceDeleteDialogComponent } from 'app/entities/agence/agence-delete-dialog.component';
import { AgenceService } from 'app/entities/agence/agence.service';

describe('Component Tests', () => {
    describe('Agence Management Delete Component', () => {
        let comp: AgenceDeleteDialogComponent;
        let fixture: ComponentFixture<AgenceDeleteDialogComponent>;
        let service: AgenceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WalletTestModule],
                declarations: [AgenceDeleteDialogComponent]
            })
                .overrideTemplate(AgenceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AgenceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AgenceService);
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
