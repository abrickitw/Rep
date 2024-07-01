import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStatusEvidencije, NewStatusEvidencije } from '../status-evidencije.model';

export type PartialUpdateStatusEvidencije = Partial<IStatusEvidencije> & Pick<IStatusEvidencije, 'id'>;

export type EntityResponseType = HttpResponse<IStatusEvidencije>;
export type EntityArrayResponseType = HttpResponse<IStatusEvidencije[]>;

@Injectable({ providedIn: 'root' })
export class StatusEvidencijeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/status-evidencijes');

  create(statusEvidencije: NewStatusEvidencije): Observable<EntityResponseType> {
    return this.http.post<IStatusEvidencije>(this.resourceUrl, statusEvidencije, { observe: 'response' });
  }

  update(statusEvidencije: IStatusEvidencije): Observable<EntityResponseType> {
    return this.http.put<IStatusEvidencije>(
      `${this.resourceUrl}/${this.getStatusEvidencijeIdentifier(statusEvidencije)}`,
      statusEvidencije,
      { observe: 'response' },
    );
  }

  partialUpdate(statusEvidencije: PartialUpdateStatusEvidencije): Observable<EntityResponseType> {
    return this.http.patch<IStatusEvidencije>(
      `${this.resourceUrl}/${this.getStatusEvidencijeIdentifier(statusEvidencije)}`,
      statusEvidencije,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStatusEvidencije>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStatusEvidencije[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStatusEvidencijeIdentifier(statusEvidencije: Pick<IStatusEvidencije, 'id'>): number {
    return statusEvidencije.id;
  }

  compareStatusEvidencije(o1: Pick<IStatusEvidencije, 'id'> | null, o2: Pick<IStatusEvidencije, 'id'> | null): boolean {
    return o1 && o2 ? this.getStatusEvidencijeIdentifier(o1) === this.getStatusEvidencijeIdentifier(o2) : o1 === o2;
  }

  addStatusEvidencijeToCollectionIfMissing<Type extends Pick<IStatusEvidencije, 'id'>>(
    statusEvidencijeCollection: Type[],
    ...statusEvidencijesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const statusEvidencijes: Type[] = statusEvidencijesToCheck.filter(isPresent);
    if (statusEvidencijes.length > 0) {
      const statusEvidencijeCollectionIdentifiers = statusEvidencijeCollection.map(statusEvidencijeItem =>
        this.getStatusEvidencijeIdentifier(statusEvidencijeItem),
      );
      const statusEvidencijesToAdd = statusEvidencijes.filter(statusEvidencijeItem => {
        const statusEvidencijeIdentifier = this.getStatusEvidencijeIdentifier(statusEvidencijeItem);
        if (statusEvidencijeCollectionIdentifiers.includes(statusEvidencijeIdentifier)) {
          return false;
        }
        statusEvidencijeCollectionIdentifiers.push(statusEvidencijeIdentifier);
        return true;
      });
      return [...statusEvidencijesToAdd, ...statusEvidencijeCollection];
    }
    return statusEvidencijeCollection;
  }
}
