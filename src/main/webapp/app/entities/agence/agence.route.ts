import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Agence } from 'app/shared/model/agence.model';
import { AgenceService } from './agence.service';
import { AgenceComponent } from './agence.component';
import { AgenceDetailComponent } from './agence-detail.component';
import { AgenceUpdateComponent } from './agence-update.component';
import { AgenceDeletePopupComponent } from './agence-delete-dialog.component';
import { IAgence } from 'app/shared/model/agence.model';

@Injectable({ providedIn: 'root' })
export class AgenceResolve implements Resolve<IAgence> {
    constructor(private service: AgenceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Agence> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Agence>) => response.ok),
                map((agence: HttpResponse<Agence>) => agence.body)
            );
        }
        return of(new Agence());
    }
}

export const agenceRoute: Routes = [
    {
        path: 'agence',
        component: AgenceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'walletApp.agence.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agence/:id/view',
        component: AgenceDetailComponent,
        resolve: {
            agence: AgenceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.agence.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agence/new',
        component: AgenceUpdateComponent,
        resolve: {
            agence: AgenceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.agence.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agence/:id/edit',
        component: AgenceUpdateComponent,
        resolve: {
            agence: AgenceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.agence.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const agencePopupRoute: Routes = [
    {
        path: 'agence/:id/delete',
        component: AgenceDeletePopupComponent,
        resolve: {
            agence: AgenceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.agence.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
