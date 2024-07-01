import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVrstaUsluge, NewVrstaUsluge } from '../vrsta-usluge.model';

export type PartialUpdateVrstaUsluge = Partial<IVrstaUsluge> & Pick<IVrstaUsluge, 'id'>;

export type EntityResponseType = HttpResponse<IVrstaUsluge>;
export type EntityArrayResponseType = HttpResponse<IVrstaUsluge[]>;

@Injectable({ providedIn: 'root' })
export class VrstaUslugeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vrsta-usluges');

  create(vrstaUsluge: NewVrstaUsluge): Observable<EntityResponseType> {
    return this.http.post<IVrstaUsluge>(this.resourceUrl, vrstaUsluge, { observe: 'response' });
  }

  update(vrstaUsluge: IVrstaUsluge): Observable<EntityResponseType> {
    return this.http.put<IVrstaUsluge>(`${this.resourceUrl}/${this.getVrstaUslugeIdentifier(vrstaUsluge)}`, vrstaUsluge, {
      observe: 'response',
    });
  }

  partialUpdate(vrstaUsluge: PartialUpdateVrstaUsluge): Observable<EntityResponseType> {
    return this.http.patch<IVrstaUsluge>(`${this.resourceUrl}/${this.getVrstaUslugeIdentifier(vrstaUsluge)}`, vrstaUsluge, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVrstaUsluge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVrstaUsluge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVrstaUslugeIdentifier(vrstaUsluge: Pick<IVrstaUsluge, 'id'>): number {
    return vrstaUsluge.id;
  }

  compareVrstaUsluge(o1: Pick<IVrstaUsluge, 'id'> | null, o2: Pick<IVrstaUsluge, 'id'> | null): boolean {
    return o1 && o2 ? this.getVrstaUslugeIdentifier(o1) === this.getVrstaUslugeIdentifier(o2) : o1 === o2;
  }

  addVrstaUslugeToCollectionIfMissing<Type extends Pick<IVrstaUsluge, 'id'>>(
    vrstaUslugeCollection: Type[],
    ...vrstaUslugesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vrstaUsluges: Type[] = vrstaUslugesToCheck.filter(isPresent);
    if (vrstaUsluges.length > 0) {
      const vrstaUslugeCollectionIdentifiers = vrstaUslugeCollection.map(vrstaUslugeItem => this.getVrstaUslugeIdentifier(vrstaUslugeItem));
      const vrstaUslugesToAdd = vrstaUsluges.filter(vrstaUslugeItem => {
        const vrstaUslugeIdentifier = this.getVrstaUslugeIdentifier(vrstaUslugeItem);
        if (vrstaUslugeCollectionIdentifiers.includes(vrstaUslugeIdentifier)) {
          return false;
        }
        vrstaUslugeCollectionIdentifiers.push(vrstaUslugeIdentifier);
        return true;
      });
      return [...vrstaUslugesToAdd, ...vrstaUslugeCollection];
    }
    return vrstaUslugeCollection;
  }
}
