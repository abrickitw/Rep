import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvidencija } from '../evidencija.model';
import { EvidencijaService } from '../service/evidencija.service';

const evidencijaResolve = (route: ActivatedRouteSnapshot): Observable<null | IEvidencija> => {
  const id = route.params['id'];
  if (id) {
    return inject(EvidencijaService)
      .find(id)
      .pipe(
        mergeMap((evidencija: HttpResponse<IEvidencija>) => {
          if (evidencija.body) {
            return of(evidencija.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default evidencijaResolve;
