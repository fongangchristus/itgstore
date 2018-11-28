import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WalletSharedModule } from 'app/shared';
import {
    EcritureComponent,
    EcritureDetailComponent,
    EcritureUpdateComponent,
    EcritureDeletePopupComponent,
    EcritureDeleteDialogComponent,
    ecritureRoute,
    ecriturePopupRoute
} from './';

const ENTITY_STATES = [...ecritureRoute, ...ecriturePopupRoute];

@NgModule({
    imports: [WalletSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EcritureComponent,
        EcritureDetailComponent,
        EcritureUpdateComponent,
        EcritureDeleteDialogComponent,
        EcritureDeletePopupComponent
    ],
    entryComponents: [EcritureComponent, EcritureUpdateComponent, EcritureDeleteDialogComponent, EcritureDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WalletEcritureModule {}
