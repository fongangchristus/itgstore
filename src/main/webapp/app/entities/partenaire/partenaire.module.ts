import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WalletSharedModule } from 'app/shared';
import {
    PartenaireComponent,
    PartenaireDetailComponent,
    PartenaireUpdateComponent,
    PartenaireDeletePopupComponent,
    PartenaireDeleteDialogComponent,
    partenaireRoute,
    partenairePopupRoute
} from './';

const ENTITY_STATES = [...partenaireRoute, ...partenairePopupRoute];

@NgModule({
    imports: [WalletSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PartenaireComponent,
        PartenaireDetailComponent,
        PartenaireUpdateComponent,
        PartenaireDeleteDialogComponent,
        PartenaireDeletePopupComponent
    ],
    entryComponents: [PartenaireComponent, PartenaireUpdateComponent, PartenaireDeleteDialogComponent, PartenaireDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WalletPartenaireModule {}
