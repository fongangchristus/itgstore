import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Partenaire } from 'app/shared/model/partenaire.model';
import { PartenaireService } from './partenaire.service';
import { PartenaireComponent } from './partenaire.component';
import { PartenaireDetailComponent } from './partenaire-detail.component';
import { PartenaireUpdateComponent } from './partenaire-update.component';
import { PartenaireDeletePopupComponent } from './partenaire-delete-dialog.component';
import { IPartenaire } from 'app/shared/model/partenaire.model';

@Injectable({ providedIn: 'root' })
export class PartenaireResolve implements Resolve<IPartenaire> {
    constructor(private service: PartenaireService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Partenaire> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Partenaire>) => response.ok),
                map((partenaire: HttpResponse<Partenaire>) => partenaire.body)
            );
        }
        return of(new Partenaire());
    }
}

export const partenaireRoute: Routes = [
    {
        path: 'partenaire',
        component: PartenaireComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'walletApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'partenaire/:id/view',
        component: PartenaireDetailComponent,
        resolve: {
            partenaire: PartenaireResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'partenaire/new',
        component: PartenaireUpdateComponent,
        resolve: {
            partenaire: PartenaireResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'partenaire/:id/edit',
        component: PartenaireUpdateComponent,
        resolve: {
            partenaire: PartenaireResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const partenairePopupRoute: Routes = [
    {
        path: 'partenaire/:id/delete',
        component: PartenaireDeletePopupComponent,
        resolve: {
            partenaire: PartenaireResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
