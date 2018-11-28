/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WalletTestModule } from '../../../test.module';
import { EcritureDeleteDialogComponent } from 'app/entities/ecriture/ecriture-delete-dialog.component';
import { EcritureService } from 'app/entities/ecriture/ecriture.service';

describe('Component Tests', () => {
    describe('Ecriture Management Delete Component', () => {
        let comp: EcritureDeleteDialogComponent;
        let fixture: ComponentFixture<EcritureDeleteDialogComponent>;
        let service: EcritureService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WalletTestModule],
                declarations: [EcritureDeleteDialogComponent]
            })
                .overrideTemplate(EcritureDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EcritureDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EcritureService);
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
