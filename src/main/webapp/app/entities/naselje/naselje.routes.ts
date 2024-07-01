import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { NaseljeComponent } from './list/naselje.component';
import { NaseljeDetailComponent } from './detail/naselje-detail.component';
import { NaseljeUpdateComponent } from './update/naselje-update.component';
import NaseljeResolve from './route/naselje-routing-resolve.service';

const naseljeRoute: Routes = [
  {
    path: '',
    component: NaseljeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NaseljeDetailComponent,
    resolve: {
      naselje: NaseljeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NaseljeUpdateComponent,
    resolve: {
      naselje: NaseljeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NaseljeUpdateComponent,
    resolve: {
      naselje: NaseljeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default naseljeRoute;
