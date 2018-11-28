import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Ecriture } from 'app/shared/model/ecriture.model';
import { EcritureService } from './ecriture.service';
import { EcritureComponent } from './ecriture.component';
import { EcritureDetailComponent } from './ecriture-detail.component';
import { EcritureUpdateComponent } from './ecriture-update.component';
import { EcritureDeletePopupComponent } from './ecriture-delete-dialog.component';
import { IEcriture } from 'app/shared/model/ecriture.model';

@Injectable({ providedIn: 'root' })
export class EcritureResolve implements Resolve<IEcriture> {
    constructor(private service: EcritureService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Ecriture> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Ecriture>) => response.ok),
                map((ecriture: HttpResponse<Ecriture>) => ecriture.body)
            );
        }
        return of(new Ecriture());
    }
}

export const ecritureRoute: Routes = [
    {
        path: 'ecriture',
        component: EcritureComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'walletApp.ecriture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ecriture/:id/view',
        component: EcritureDetailComponent,
        resolve: {
            ecriture: EcritureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.ecriture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ecriture/new',
        component: EcritureUpdateComponent,
        resolve: {
            ecriture: EcritureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.ecriture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ecriture/:id/edit',
        component: EcritureUpdateComponent,
        resolve: {
            ecriture: EcritureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.ecriture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ecriturePopupRoute: Routes = [
    {
        path: 'ecriture/:id/delete',
        component: EcritureDeletePopupComponent,
        resolve: {
            ecriture: EcritureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.ecriture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
