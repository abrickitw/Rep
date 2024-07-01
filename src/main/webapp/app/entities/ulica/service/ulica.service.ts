import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUlica, NewUlica } from '../ulica.model';

export type PartialUpdateUlica = Partial<IUlica> & Pick<IUlica, 'id'>;

export type EntityResponseType = HttpResponse<IUlica>;
export type EntityArrayResponseType = HttpResponse<IUlica[]>;

@Injectable({ providedIn: 'root' })
export class UlicaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ulicas');

  create(ulica: NewUlica): Observable<EntityResponseType> {
    return this.http.post<IUlica>(this.resourceUrl, ulica, { observe: 'response' });
  }

  update(ulica: IUlica): Observable<EntityResponseType> {
    return this.http.put<IUlica>(`${this.resourceUrl}/${this.getUlicaIdentifier(ulica)}`, ulica, { observe: 'response' });
  }

  partialUpdate(ulica: PartialUpdateUlica): Observable<EntityResponseType> {
    return this.http.patch<IUlica>(`${this.resourceUrl}/${this.getUlicaIdentifier(ulica)}`, ulica, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUlica>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUlica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUlicaIdentifier(ulica: Pick<IUlica, 'id'>): number {
    return ulica.id;
  }

  compareUlica(o1: Pick<IUlica, 'id'> | null, o2: Pick<IUlica, 'id'> | null): boolean {
    return o1 && o2 ? this.getUlicaIdentifier(o1) === this.getUlicaIdentifier(o2) : o1 === o2;
  }

  addUlicaToCollectionIfMissing<Type extends Pick<IUlica, 'id'>>(
    ulicaCollection: Type[],
    ...ulicasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ulicas: Type[] = ulicasToCheck.filter(isPresent);
    if (ulicas.length > 0) {
      const ulicaCollectionIdentifiers = ulicaCollection.map(ulicaItem => this.getUlicaIdentifier(ulicaItem));
      const ulicasToAdd = ulicas.filter(ulicaItem => {
        const ulicaIdentifier = this.getUlicaIdentifier(ulicaItem);
        if (ulicaCollectionIdentifiers.includes(ulicaIdentifier)) {
          return false;
        }
        ulicaCollectionIdentifiers.push(ulicaIdentifier);
        return true;
      });
      return [...ulicasToAdd, ...ulicaCollection];
    }
    return ulicaCollection;
  }
}
