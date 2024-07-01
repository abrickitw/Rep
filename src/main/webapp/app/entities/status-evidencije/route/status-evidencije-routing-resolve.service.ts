import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStatusEvidencije } from '../status-evidencije.model';
import { StatusEvidencijeService } from '../service/status-evidencije.service';

const statusEvidencijeResolve = (route: ActivatedRouteSnapshot): Observable<null | IStatusEvidencije> => {
  const id = route.params['id'];
  if (id) {
    return inject(StatusEvidencijeService)
      .find(id)
      .pipe(
        mergeMap((statusEvidencije: HttpResponse<IStatusEvidencije>) => {
          if (statusEvidencije.body) {
            return of(statusEvidencije.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default statusEvidencijeResolve;
