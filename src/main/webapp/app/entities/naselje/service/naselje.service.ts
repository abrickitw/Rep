import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INaselje, NewNaselje } from '../naselje.model';

export type PartialUpdateNaselje = Partial<INaselje> & Pick<INaselje, 'id'>;

export type EntityResponseType = HttpResponse<INaselje>;
export type EntityArrayResponseType = HttpResponse<INaselje[]>;

@Injectable({ providedIn: 'root' })
export class NaseljeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/naseljes');

  create(naselje: NewNaselje): Observable<EntityResponseType> {
    return this.http.post<INaselje>(this.resourceUrl, naselje, { observe: 'response' });
  }

  update(naselje: INaselje): Observable<EntityResponseType> {
    return this.http.put<INaselje>(`${this.resourceUrl}/${this.getNaseljeIdentifier(naselje)}`, naselje, { observe: 'response' });
  }

  partialUpdate(naselje: PartialUpdateNaselje): Observable<EntityResponseType> {
    return this.http.patch<INaselje>(`${this.resourceUrl}/${this.getNaseljeIdentifier(naselje)}`, naselje, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INaselje>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INaselje[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNaseljeIdentifier(naselje: Pick<INaselje, 'id'>): number {
    return naselje.id;
  }

  compareNaselje(o1: Pick<INaselje, 'id'> | null, o2: Pick<INaselje, 'id'> | null): boolean {
    return o1 && o2 ? this.getNaseljeIdentifier(o1) === this.getNaseljeIdentifier(o2) : o1 === o2;
  }

  addNaseljeToCollectionIfMissing<Type extends Pick<INaselje, 'id'>>(
    naseljeCollection: Type[],
    ...naseljesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const naseljes: Type[] = naseljesToCheck.filter(isPresent);
    if (naseljes.length > 0) {
      const naseljeCollectionIdentifiers = naseljeCollection.map(naseljeItem => this.getNaseljeIdentifier(naseljeItem));
      const naseljesToAdd = naseljes.filter(naseljeItem => {
        const naseljeIdentifier = this.getNaseljeIdentifier(naseljeItem);
        if (naseljeCollectionIdentifiers.includes(naseljeIdentifier)) {
          return false;
        }
        naseljeCollectionIdentifiers.push(naseljeIdentifier);
        return true;
      });
      return [...naseljesToAdd, ...naseljeCollection];
    }
    return naseljeCollection;
  }
}
