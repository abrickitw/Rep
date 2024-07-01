import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INaselje } from '../naselje.model';
import { NaseljeService } from '../service/naselje.service';

const naseljeResolve = (route: ActivatedRouteSnapshot): Observable<null | INaselje> => {
  const id = route.params['id'];
  if (id) {
    return inject(NaseljeService)
      .find(id)
      .pipe(
        mergeMap((naselje: HttpResponse<INaselje>) => {
          if (naselje.body) {
            return of(naselje.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default naseljeResolve;
