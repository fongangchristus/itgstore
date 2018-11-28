import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEcriture } from 'app/shared/model/ecriture.model';

type EntityResponseType = HttpResponse<IEcriture>;
type EntityArrayResponseType = HttpResponse<IEcriture[]>;

@Injectable({ providedIn: 'root' })
export class EcritureService {
    public resourceUrl = SERVER_API_URL + 'api/ecritures';

    constructor(private http: HttpClient) {}

    create(ecriture: IEcriture): Observable<EntityResponseType> {
        return this.http.post<IEcriture>(this.resourceUrl, ecriture, { observe: 'response' });
    }

    update(ecriture: IEcriture): Observable<EntityResponseType> {
        return this.http.put<IEcriture>(this.resourceUrl, ecriture, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IEcriture>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEcriture[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
