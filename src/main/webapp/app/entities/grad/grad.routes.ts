import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { GradComponent } from './list/grad.component';
import { GradDetailComponent } from './detail/grad-detail.component';
import { GradUpdateComponent } from './update/grad-update.component';
import GradResolve from './route/grad-routing-resolve.service';

const gradRoute: Routes = [
  {
    path: '',
    component: GradComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GradDetailComponent,
    resolve: {
      grad: GradResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GradUpdateComponent,
    resolve: {
      grad: GradResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GradUpdateComponent,
    resolve: {
      grad: GradResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default gradRoute;
