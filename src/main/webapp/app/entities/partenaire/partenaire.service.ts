import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPartenaire } from 'app/shared/model/partenaire.model';

type EntityResponseType = HttpResponse<IPartenaire>;
type EntityArrayResponseType = HttpResponse<IPartenaire[]>;

@Injectable({ providedIn: 'root' })
export class PartenaireService {
    public resourceUrl = SERVER_API_URL + 'api/partenaires';

    constructor(private http: HttpClient) {}

    create(partenaire: IPartenaire): Observable<EntityResponseType> {
        return this.http.post<IPartenaire>(this.resourceUrl, partenaire, { observe: 'response' });
    }

    update(partenaire: IPartenaire): Observable<EntityResponseType> {
        return this.http.put<IPartenaire>(this.resourceUrl, partenaire, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPartenaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPartenaire[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
