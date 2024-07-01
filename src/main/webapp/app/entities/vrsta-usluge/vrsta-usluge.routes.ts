import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { VrstaUslugeComponent } from './list/vrsta-usluge.component';
import { VrstaUslugeDetailComponent } from './detail/vrsta-usluge-detail.component';
import { VrstaUslugeUpdateComponent } from './update/vrsta-usluge-update.component';
import VrstaUslugeResolve from './route/vrsta-usluge-routing-resolve.service';

const vrstaUslugeRoute: Routes = [
  {
    path: '',
    component: VrstaUslugeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VrstaUslugeDetailComponent,
    resolve: {
      vrstaUsluge: VrstaUslugeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VrstaUslugeUpdateComponent,
    resolve: {
      vrstaUsluge: VrstaUslugeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VrstaUslugeUpdateComponent,
    resolve: {
      vrstaUsluge: VrstaUslugeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vrstaUslugeRoute;
