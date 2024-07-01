import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVrstaUsluge } from '../vrsta-usluge.model';
import { VrstaUslugeService } from '../service/vrsta-usluge.service';

const vrstaUslugeResolve = (route: ActivatedRouteSnapshot): Observable<null | IVrstaUsluge> => {
  const id = route.params['id'];
  if (id) {
    return inject(VrstaUslugeService)
      .find(id)
      .pipe(
        mergeMap((vrstaUsluge: HttpResponse<IVrstaUsluge>) => {
          if (vrstaUsluge.body) {
            return of(vrstaUsluge.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default vrstaUslugeResolve;
