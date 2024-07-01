import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EvidencijaComponent } from './list/evidencija.component';
import { EvidencijaDetailComponent } from './detail/evidencija-detail.component';
import { EvidencijaUpdateComponent } from './update/evidencija-update.component';
import EvidencijaResolve from './route/evidencija-routing-resolve.service';

const evidencijaRoute: Routes = [
  {
    path: '',
    component: EvidencijaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EvidencijaDetailComponent,
    resolve: {
      evidencija: EvidencijaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EvidencijaUpdateComponent,
    resolve: {
      evidencija: EvidencijaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EvidencijaUpdateComponent,
    resolve: {
      evidencija: EvidencijaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default evidencijaRoute;
