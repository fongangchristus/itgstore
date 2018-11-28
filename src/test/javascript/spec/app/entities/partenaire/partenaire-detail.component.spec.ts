/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WalletTestModule } from '../../../test.module';
import { PartenaireDetailComponent } from 'app/entities/partenaire/partenaire-detail.component';
import { Partenaire } from 'app/shared/model/partenaire.model';

describe('Component Tests', () => {
    describe('Partenaire Management Detail Component', () => {
        let comp: PartenaireDetailComponent;
        let fixture: ComponentFixture<PartenaireDetailComponent>;
        const route = ({ data: of({ partenaire: new Partenaire(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WalletTestModule],
                declarations: [PartenaireDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PartenaireDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PartenaireDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.partenaire).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
