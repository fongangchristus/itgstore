import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFacture } from 'app/shared/model/facture.model';

type EntityResponseType = HttpResponse<IFacture>;
type EntityArrayResponseType = HttpResponse<IFacture[]>;

@Injectable({ providedIn: 'root' })
export class FactureService {
    public resourceUrl = SERVER_API_URL + 'api/factures';

    constructor(private http: HttpClient) {}

    create(facture: IFacture): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(facture);
        return this.http
            .post<IFacture>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(facture: IFacture): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(facture);
        return this.http
            .put<IFacture>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IFacture>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IFacture[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(facture: IFacture): IFacture {
        const copy: IFacture = Object.assign({}, facture, {
            dateCreation: facture.dateCreation != null && facture.dateCreation.isValid() ? facture.dateCreation.toJSON() : null,
            dateEmission: facture.dateEmission != null && facture.dateEmission.isValid() ? facture.dateEmission.toJSON() : null,
            dateReglement: facture.dateReglement != null && facture.dateReglement.isValid() ? facture.dateReglement.toJSON() : null,
            dateEcheance: facture.dateEcheance != null && facture.dateEcheance.isValid() ? facture.dateEcheance.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateCreation = res.body.dateCreation != null ? moment(res.body.dateCreation) : null;
            res.body.dateEmission = res.body.dateEmission != null ? moment(res.body.dateEmission) : null;
            res.body.dateReglement = res.body.dateReglement != null ? moment(res.body.dateReglement) : null;
            res.body.dateEcheance = res.body.dateEcheance != null ? moment(res.body.dateEcheance) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((facture: IFacture) => {
                facture.dateCreation = facture.dateCreation != null ? moment(facture.dateCreation) : null;
                facture.dateEmission = facture.dateEmission != null ? moment(facture.dateEmission) : null;
                facture.dateReglement = facture.dateReglement != null ? moment(facture.dateReglement) : null;
                facture.dateEcheance = facture.dateEcheance != null ? moment(facture.dateEcheance) : null;
            });
        }
        return res;
    }
}
