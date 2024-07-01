import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RasporedComponent } from './list/raspored.component';
import { RasporedDetailComponent } from './detail/raspored-detail.component';
import { RasporedUpdateComponent } from './update/raspored-update.component';
import RasporedResolve from './route/raspored-routing-resolve.service';

const rasporedRoute: Routes = [
  {
    path: '',
    component: RasporedComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RasporedDetailComponent,
    resolve: {
      raspored: RasporedResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RasporedUpdateComponent,
    resolve: {
      raspored: RasporedResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RasporedUpdateComponent,
    resolve: {
      raspored: RasporedResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rasporedRoute;
