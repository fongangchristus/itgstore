/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WalletTestModule } from '../../../test.module';
import { PartenaireUpdateComponent } from 'app/entities/partenaire/partenaire-update.component';
import { PartenaireService } from 'app/entities/partenaire/partenaire.service';
import { Partenaire } from 'app/shared/model/partenaire.model';

describe('Component Tests', () => {
    describe('Partenaire Management Update Component', () => {
        let comp: PartenaireUpdateComponent;
        let fixture: ComponentFixture<PartenaireUpdateComponent>;
        let service: PartenaireService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WalletTestModule],
                declarations: [PartenaireUpdateComponent]
            })
                .overrideTemplate(PartenaireUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PartenaireUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PartenaireService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Partenaire(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.partenaire = entity;
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
                    const entity = new Partenaire();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.partenaire = entity;
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
