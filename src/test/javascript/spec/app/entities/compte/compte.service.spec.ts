/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { CompteService } from 'app/entities/compte/compte.service';
import { ICompte, Compte } from 'app/shared/model/compte.model';

describe('Service Tests', () => {
    describe('Compte Service', () => {
        let injector: TestBed;
        let service: CompteService;
        let httpMock: HttpTestingController;
        let elemDefault: ICompte;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(CompteService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new Compte(0, 0, 'AAAAAAA', false, false, false, false, 0, 0, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Compte', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new Compte(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Compte', async () => {
                const returnedFromService = Object.assign(
                    {
                        code: 1,
                        libelle: 'BBBBBB',
                        isDebit: true,
                        isCredit: true,
                        isDebiteur: true,
                        isCrediteur: true,
                        soldeDebit: 1,
                        soldeCredit: 1,
                        balance: 1
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Compte', async () => {
                const returnedFromService = Object.assign(
                    {
                        code: 1,
                        libelle: 'BBBBBB',
                        isDebit: true,
                        isCredit: true,
                        isDebiteur: true,
                        isCrediteur: true,
                        soldeDebit: 1,
                        soldeCredit: 1,
                        balance: 1
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
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

            it('should delete a Compte', async () => {
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
