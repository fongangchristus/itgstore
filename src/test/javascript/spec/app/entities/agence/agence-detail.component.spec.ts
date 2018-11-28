/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WalletTestModule } from '../../../test.module';
import { AgenceDetailComponent } from 'app/entities/agence/agence-detail.component';
import { Agence } from 'app/shared/model/agence.model';

describe('Component Tests', () => {
    describe('Agence Management Detail Component', () => {
        let comp: AgenceDetailComponent;
        let fixture: ComponentFixture<AgenceDetailComponent>;
        const route = ({ data: of({ agence: new Agence(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WalletTestModule],
                declarations: [AgenceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AgenceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AgenceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.agence).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
