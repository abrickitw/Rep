import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRaspored, NewRaspored } from '../raspored.model';

export type PartialUpdateRaspored = Partial<IRaspored> & Pick<IRaspored, 'id'>;

type RestOf<T extends IRaspored | NewRaspored> = Omit<T, 'datumUsluge'> & {
  datumUsluge?: string | null;
};

export type RestRaspored = RestOf<IRaspored>;

export type NewRestRaspored = RestOf<NewRaspored>;

export type PartialUpdateRestRaspored = RestOf<PartialUpdateRaspored>;

export type EntityResponseType = HttpResponse<IRaspored>;
export type EntityArrayResponseType = HttpResponse<IRaspored[]>;

@Injectable({ providedIn: 'root' })
export class RasporedService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rasporeds');

  create(raspored: NewRaspored): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(raspored);
    return this.http
      .post<RestRaspored>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(raspored: IRaspored): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(raspored);
    return this.http
      .put<RestRaspored>(`${this.resourceUrl}/${this.getRasporedIdentifier(raspored)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(raspored: PartialUpdateRaspored): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(raspored);
    return this.http
      .patch<RestRaspored>(`${this.resourceUrl}/${this.getRasporedIdentifier(raspored)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRaspored>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRaspored[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRasporedIdentifier(raspored: Pick<IRaspored, 'id'>): number {
    return raspored.id;
  }

  compareRaspored(o1: Pick<IRaspored, 'id'> | null, o2: Pick<IRaspored, 'id'> | null): boolean {
    return o1 && o2 ? this.getRasporedIdentifier(o1) === this.getRasporedIdentifier(o2) : o1 === o2;
  }

  addRasporedToCollectionIfMissing<Type extends Pick<IRaspored, 'id'>>(
    rasporedCollection: Type[],
    ...rasporedsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rasporeds: Type[] = rasporedsToCheck.filter(isPresent);
    if (rasporeds.length > 0) {
      const rasporedCollectionIdentifiers = rasporedCollection.map(rasporedItem => this.getRasporedIdentifier(rasporedItem));
      const rasporedsToAdd = rasporeds.filter(rasporedItem => {
        const rasporedIdentifier = this.getRasporedIdentifier(rasporedItem);
        if (rasporedCollectionIdentifiers.includes(rasporedIdentifier)) {
          return false;
        }
        rasporedCollectionIdentifiers.push(rasporedIdentifier);
        return true;
      });
      return [...rasporedsToAdd, ...rasporedCollection];
    }
    return rasporedCollection;
  }

  protected convertDateFromClient<T extends IRaspored | NewRaspored | PartialUpdateRaspored>(raspored: T): RestOf<T> {
    return {
      ...raspored,
      datumUsluge: raspored.datumUsluge?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRaspored: RestRaspored): IRaspored {
    return {
      ...restRaspored,
      datumUsluge: restRaspored.datumUsluge ? dayjs(restRaspored.datumUsluge) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRaspored>): HttpResponse<IRaspored> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRaspored[]>): HttpResponse<IRaspored[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
