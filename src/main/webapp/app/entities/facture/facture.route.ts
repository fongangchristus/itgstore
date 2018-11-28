import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Facture } from 'app/shared/model/facture.model';
import { FactureService } from './facture.service';
import { FactureComponent } from './facture.component';
import { FactureDetailComponent } from './facture-detail.component';
import { FactureUpdateComponent } from './facture-update.component';
import { FactureDeletePopupComponent } from './facture-delete-dialog.component';
import { IFacture } from 'app/shared/model/facture.model';

@Injectable({ providedIn: 'root' })
export class FactureResolve implements Resolve<IFacture> {
    constructor(private service: FactureService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Facture> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Facture>) => response.ok),
                map((facture: HttpResponse<Facture>) => facture.body)
            );
        }
        return of(new Facture());
    }
}

export const factureRoute: Routes = [
    {
        path: 'facture',
        component: FactureComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'walletApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'facture/:id/view',
        component: FactureDetailComponent,
        resolve: {
            facture: FactureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'facture/new',
        component: FactureUpdateComponent,
        resolve: {
            facture: FactureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'facture/:id/edit',
        component: FactureUpdateComponent,
        resolve: {
            facture: FactureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const facturePopupRoute: Routes = [
    {
        path: 'facture/:id/delete',
        component: FactureDeletePopupComponent,
        resolve: {
            facture: FactureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
