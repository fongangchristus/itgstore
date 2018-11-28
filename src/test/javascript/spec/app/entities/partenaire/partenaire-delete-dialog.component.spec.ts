/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WalletTestModule } from '../../../test.module';
import { PartenaireDeleteDialogComponent } from 'app/entities/partenaire/partenaire-delete-dialog.component';
import { PartenaireService } from 'app/entities/partenaire/partenaire.service';

describe('Component Tests', () => {
    describe('Partenaire Management Delete Component', () => {
        let comp: PartenaireDeleteDialogComponent;
        let fixture: ComponentFixture<PartenaireDeleteDialogComponent>;
        let service: PartenaireService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WalletTestModule],
                declarations: [PartenaireDeleteDialogComponent]
            })
                .overrideTemplate(PartenaireDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PartenaireDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PartenaireService);
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
