import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGrad, NewGrad } from '../grad.model';

export type PartialUpdateGrad = Partial<IGrad> & Pick<IGrad, 'id'>;

export type EntityResponseType = HttpResponse<IGrad>;
export type EntityArrayResponseType = HttpResponse<IGrad[]>;

@Injectable({ providedIn: 'root' })
export class GradService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/grads');

  create(grad: NewGrad): Observable<EntityResponseType> {
    return this.http.post<IGrad>(this.resourceUrl, grad, { observe: 'response' });
  }

  update(grad: IGrad): Observable<EntityResponseType> {
    return this.http.put<IGrad>(`${this.resourceUrl}/${this.getGradIdentifier(grad)}`, grad, { observe: 'response' });
  }

  partialUpdate(grad: PartialUpdateGrad): Observable<EntityResponseType> {
    return this.http.patch<IGrad>(`${this.resourceUrl}/${this.getGradIdentifier(grad)}`, grad, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGrad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGrad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGradIdentifier(grad: Pick<IGrad, 'id'>): number {
    return grad.id;
  }

  compareGrad(o1: Pick<IGrad, 'id'> | null, o2: Pick<IGrad, 'id'> | null): boolean {
    return o1 && o2 ? this.getGradIdentifier(o1) === this.getGradIdentifier(o2) : o1 === o2;
  }

  addGradToCollectionIfMissing<Type extends Pick<IGrad, 'id'>>(
    gradCollection: Type[],
    ...gradsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const grads: Type[] = gradsToCheck.filter(isPresent);
    if (grads.length > 0) {
      const gradCollectionIdentifiers = gradCollection.map(gradItem => this.getGradIdentifier(gradItem));
      const gradsToAdd = grads.filter(gradItem => {
        const gradIdentifier = this.getGradIdentifier(gradItem);
        if (gradCollectionIdentifiers.includes(gradIdentifier)) {
          return false;
        }
        gradCollectionIdentifiers.push(gradIdentifier);
        return true;
      });
      return [...gradsToAdd, ...gradCollection];
    }
    return gradCollection;
  }
}
