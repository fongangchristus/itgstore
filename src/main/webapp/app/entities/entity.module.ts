import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WalletCompteModule } from './compte/compte.module';
import { WalletClientModule } from './client/client.module';
import { WalletFactureModule } from './facture/facture.module';
import { WalletPartenaireModule } from './partenaire/partenaire.module';
import { WalletDeviceModule } from './device/device.module';
import { WalletAgenceModule } from './agence/agence.module';
import { WalletEcritureModule } from './ecriture/ecriture.module';
import { WalletTransactionModule } from './transaction/transaction.module';
import { WalletOperationModule } from './operation/operation.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        WalletCompteModule,
        WalletClientModule,
        WalletFactureModule,
        WalletPartenaireModule,
        WalletDeviceModule,
        WalletAgenceModule,
        WalletEcritureModule,
        WalletTransactionModule,
        WalletOperationModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WalletEntityModule {}
