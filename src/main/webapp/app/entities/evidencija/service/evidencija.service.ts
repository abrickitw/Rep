import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvidencija, NewEvidencija } from '../evidencija.model';

export type PartialUpdateEvidencija = Partial<IEvidencija> & Pick<IEvidencija, 'id'>;

type RestOf<T extends IEvidencija | NewEvidencija> = Omit<T, 'datumIspravka'> & {
  datumIspravka?: string | null;
};

export type RestEvidencija = RestOf<IEvidencija>;

export type NewRestEvidencija = RestOf<NewEvidencija>;

export type PartialUpdateRestEvidencija = RestOf<PartialUpdateEvidencija>;

export type EntityResponseType = HttpResponse<IEvidencija>;
export type EntityArrayResponseType = HttpResponse<IEvidencija[]>;

@Injectable({ providedIn: 'root' })
export class EvidencijaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evidencijas');

  create(evidencija: NewEvidencija): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evidencija);
    return this.http
      .post<RestEvidencija>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(evidencija: IEvidencija): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evidencija);
    return this.http
      .put<RestEvidencija>(`${this.resourceUrl}/${this.getEvidencijaIdentifier(evidencija)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(evidencija: PartialUpdateEvidencija): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evidencija);
    return this.http
      .patch<RestEvidencija>(`${this.resourceUrl}/${this.getEvidencijaIdentifier(evidencija)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEvidencija>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEvidencija[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEvidencijaIdentifier(evidencija: Pick<IEvidencija, 'id'>): number {
    return evidencija.id;
  }

  compareEvidencija(o1: Pick<IEvidencija, 'id'> | null, o2: Pick<IEvidencija, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvidencijaIdentifier(o1) === this.getEvidencijaIdentifier(o2) : o1 === o2;
  }

  addEvidencijaToCollectionIfMissing<Type extends Pick<IEvidencija, 'id'>>(
    evidencijaCollection: Type[],
    ...evidencijasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evidencijas: Type[] = evidencijasToCheck.filter(isPresent);
    if (evidencijas.length > 0) {
      const evidencijaCollectionIdentifiers = evidencijaCollection.map(evidencijaItem => this.getEvidencijaIdentifier(evidencijaItem));
      const evidencijasToAdd = evidencijas.filter(evidencijaItem => {
        const evidencijaIdentifier = this.getEvidencijaIdentifier(evidencijaItem);
        if (evidencijaCollectionIdentifiers.includes(evidencijaIdentifier)) {
          return false;
        }
        evidencijaCollectionIdentifiers.push(evidencijaIdentifier);
        return true;
      });
      return [...evidencijasToAdd, ...evidencijaCollection];
    }
    return evidencijaCollection;
  }

  protected convertDateFromClient<T extends IEvidencija | NewEvidencija | PartialUpdateEvidencija>(evidencija: T): RestOf<T> {
    return {
      ...evidencija,
      datumIspravka: evidencija.datumIspravka?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEvidencija: RestEvidencija): IEvidencija {
    return {
      ...restEvidencija,
      datumIspravka: restEvidencija.datumIspravka ? dayjs(restEvidencija.datumIspravka) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEvidencija>): HttpResponse<IEvidencija> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEvidencija[]>): HttpResponse<IEvidencija[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
