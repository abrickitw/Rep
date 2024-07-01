import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRaspored } from '../raspored.model';
import { RasporedService } from '../service/raspored.service';

const rasporedResolve = (route: ActivatedRouteSnapshot): Observable<null | IRaspored> => {
  const id = route.params['id'];
  if (id) {
    return inject(RasporedService)
      .find(id)
      .pipe(
        mergeMap((raspored: HttpResponse<IRaspored>) => {
          if (raspored.body) {
            return of(raspored.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default rasporedResolve;
