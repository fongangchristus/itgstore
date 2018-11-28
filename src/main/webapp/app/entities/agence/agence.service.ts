import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAgence } from 'app/shared/model/agence.model';

type EntityResponseType = HttpResponse<IAgence>;
type EntityArrayResponseType = HttpResponse<IAgence[]>;

@Injectable({ providedIn: 'root' })
export class AgenceService {
    public resourceUrl = SERVER_API_URL + 'api/agences';

    constructor(private http: HttpClient) {}

    create(agence: IAgence): Observable<EntityResponseType> {
        return this.http.post<IAgence>(this.resourceUrl, agence, { observe: 'response' });
    }

    update(agence: IAgence): Observable<EntityResponseType> {
        return this.http.put<IAgence>(this.resourceUrl, agence, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAgence>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAgence[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
