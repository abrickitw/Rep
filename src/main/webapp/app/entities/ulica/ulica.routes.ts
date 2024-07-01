import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UlicaComponent } from './list/ulica.component';
import { UlicaDetailComponent } from './detail/ulica-detail.component';
import { UlicaUpdateComponent } from './update/ulica-update.component';
import UlicaResolve from './route/ulica-routing-resolve.service';

const ulicaRoute: Routes = [
  {
    path: '',
    component: UlicaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UlicaDetailComponent,
    resolve: {
      ulica: UlicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UlicaUpdateComponent,
    resolve: {
      ulica: UlicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UlicaUpdateComponent,
    resolve: {
      ulica: UlicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ulicaRoute;
