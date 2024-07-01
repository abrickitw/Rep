import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGrad } from '../grad.model';
import { GradService } from '../service/grad.service';

const gradResolve = (route: ActivatedRouteSnapshot): Observable<null | IGrad> => {
  const id = route.params['id'];
  if (id) {
    return inject(GradService)
      .find(id)
      .pipe(
        mergeMap((grad: HttpResponse<IGrad>) => {
          if (grad.body) {
            return of(grad.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default gradResolve;
