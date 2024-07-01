import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StatusEvidencijeComponent } from './list/status-evidencije.component';
import { StatusEvidencijeDetailComponent } from './detail/status-evidencije-detail.component';
import { StatusEvidencijeUpdateComponent } from './update/status-evidencije-update.component';
import StatusEvidencijeResolve from './route/status-evidencije-routing-resolve.service';

const statusEvidencijeRoute: Routes = [
  {
    path: '',
    component: StatusEvidencijeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StatusEvidencijeDetailComponent,
    resolve: {
      statusEvidencije: StatusEvidencijeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StatusEvidencijeUpdateComponent,
    resolve: {
      statusEvidencije: StatusEvidencijeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StatusEvidencijeUpdateComponent,
    resolve: {
      statusEvidencije: StatusEvidencijeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default statusEvidencijeRoute;
