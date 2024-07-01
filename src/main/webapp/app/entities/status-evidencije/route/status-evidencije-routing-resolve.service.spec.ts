import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IStatusEvidencije } from '../status-evidencije.model';
import { StatusEvidencijeService } from '../service/status-evidencije.service';

import statusEvidencijeResolve from './status-evidencije-routing-resolve.service';

describe('StatusEvidencije routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: StatusEvidencijeService;
  let resultStatusEvidencije: IStatusEvidencije | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(StatusEvidencijeService);
    resultStatusEvidencije = undefined;
  });

  describe('resolve', () => {
    it('should return IStatusEvidencije returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        statusEvidencijeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStatusEvidencije = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultStatusEvidencije).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        statusEvidencijeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStatusEvidencije = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultStatusEvidencije).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IStatusEvidencije>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        statusEvidencijeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStatusEvidencije = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultStatusEvidencije).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
