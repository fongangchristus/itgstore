/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WalletTestModule } from '../../../test.module';
import { EcritureUpdateComponent } from 'app/entities/ecriture/ecriture-update.component';
import { EcritureService } from 'app/entities/ecriture/ecriture.service';
import { Ecriture } from 'app/shared/model/ecriture.model';

describe('Component Tests', () => {
    describe('Ecriture Management Update Component', () => {
        let comp: EcritureUpdateComponent;
        let fixture: ComponentFixture<EcritureUpdateComponent>;
        let service: EcritureService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WalletTestModule],
                declarations: [EcritureUpdateComponent]
            })
                .overrideTemplate(EcritureUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EcritureUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EcritureService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Ecriture(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.ecriture = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Ecriture();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.ecriture = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
