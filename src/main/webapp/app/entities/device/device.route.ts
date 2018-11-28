import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Device } from 'app/shared/model/device.model';
import { DeviceService } from './device.service';
import { DeviceComponent } from './device.component';
import { DeviceDetailComponent } from './device-detail.component';
import { DeviceUpdateComponent } from './device-update.component';
import { DeviceDeletePopupComponent } from './device-delete-dialog.component';
import { IDevice } from 'app/shared/model/device.model';

@Injectable({ providedIn: 'root' })
export class DeviceResolve implements Resolve<IDevice> {
    constructor(private service: DeviceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Device> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Device>) => response.ok),
                map((device: HttpResponse<Device>) => device.body)
            );
        }
        return of(new Device());
    }
}

export const deviceRoute: Routes = [
    {
        path: 'device',
        component: DeviceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'walletApp.device.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'device/:id/view',
        component: DeviceDetailComponent,
        resolve: {
            device: DeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.device.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'device/new',
        component: DeviceUpdateComponent,
        resolve: {
            device: DeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.device.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'device/:id/edit',
        component: DeviceUpdateComponent,
        resolve: {
            device: DeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.device.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const devicePopupRoute: Routes = [
    {
        path: 'device/:id/delete',
        component: DeviceDeletePopupComponent,
        resolve: {
            device: DeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'walletApp.device.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
