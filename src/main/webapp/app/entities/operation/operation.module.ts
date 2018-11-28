import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WalletSharedModule } from 'app/shared';
import {
    OperationComponent,
    OperationDetailComponent,
    OperationUpdateComponent,
    OperationDeletePopupComponent,
    OperationDeleteDialogComponent,
    operationRoute,
    operationPopupRoute
} from './';

const ENTITY_STATES = [...operationRoute, ...operationPopupRoute];

@NgModule({
    imports: [WalletSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OperationComponent,
        OperationDetailComponent,
        OperationUpdateComponent,
        OperationDeleteDialogComponent,
        OperationDeletePopupComponent
    ],
    entryComponents: [OperationComponent, OperationUpdateComponent, OperationDeleteDialogComponent, OperationDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WalletOperationModule {}
