import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUlica } from '../ulica.model';
import { UlicaService } from '../service/ulica.service';

const ulicaResolve = (route: ActivatedRouteSnapshot): Observable<null | IUlica> => {
  const id = route.params['id'];
  if (id) {
    return inject(UlicaService)
      .find(id)
      .pipe(
        mergeMap((ulica: HttpResponse<IUlica>) => {
          if (ulica.body) {
            return of(ulica.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ulicaResolve;
