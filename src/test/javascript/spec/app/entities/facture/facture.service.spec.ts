/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { FactureService } from 'app/entities/facture/facture.service';
import { IFacture, Facture } from 'app/shared/model/facture.model';

describe('Service Tests', () => {
    describe('Facture Service', () => {
        let injector: TestBed;
        let service: FactureService;
        let httpMock: HttpTestingController;
        let elemDefault: IFacture;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(FactureService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Facture(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, currentDate, currentDate, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        dateCreation: currentDate.format(DATE_TIME_FORMAT),
                        dateEmission: currentDate.format(DATE_TIME_FORMAT),
                        dateReglement: currentDate.format(DATE_TIME_FORMAT),
                        dateEcheance: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Facture', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        dateCreation: currentDate.format(DATE_TIME_FORMAT),
                        dateEmission: currentDate.format(DATE_TIME_FORMAT),
                        dateReglement: currentDate.format(DATE_TIME_FORMAT),
                        dateEcheance: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateCreation: currentDate,
                        dateEmission: currentDate,
                        dateReglement: currentDate,
                        dateEcheance: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Facture(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Facture', async () => {
                const returnedFromService = Object.assign(
                    {
                        reference: 'BBBBBB',
                        libelle: 'BBBBBB',
                        dateCreation: currentDate.format(DATE_TIME_FORMAT),
                        dateEmission: currentDate.format(DATE_TIME_FORMAT),
                        dateReglement: currentDate.format(DATE_TIME_FORMAT),
                        dateEcheance: currentDate.format(DATE_TIME_FORMAT),
                        montant: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        dateCreation: currentDate,
                        dateEmission: currentDate,
                        dateReglement: currentDate,
                        dateEcheance: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Facture', async () => {
                const returnedFromService = Object.assign(
                    {
                        reference: 'BBBBBB',
                        libelle: 'BBBBBB',
                        dateCreation: currentDate.format(DATE_TIME_FORMAT),
                        dateEmission: currentDate.format(DATE_TIME_FORMAT),
                        dateReglement: currentDate.format(DATE_TIME_FORMAT),
                        dateEcheance: currentDate.format(DATE_TIME_FORMAT),
                        montant: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dateCreation: currentDate,
                        dateEmission: currentDate,
                        dateReglement: currentDate,
                        dateEcheance: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Facture', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
