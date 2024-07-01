import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IGrad } from '../grad.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../grad.test-samples';

import { GradService } from './grad.service';

const requireRestSample: IGrad = {
  ...sampleWithRequiredData,
};

describe('Grad Service', () => {
  let service: GradService;
  let httpMock: HttpTestingController;
  let expectedResult: IGrad | IGrad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GradService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Grad', () => {
      const grad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(grad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Grad', () => {
      const grad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(grad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Grad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Grad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Grad', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGradToCollectionIfMissing', () => {
      it('should add a Grad to an empty array', () => {
        const grad: IGrad = sampleWithRequiredData;
        expectedResult = service.addGradToCollectionIfMissing([], grad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grad);
      });

      it('should not add a Grad to an array that contains it', () => {
        const grad: IGrad = sampleWithRequiredData;
        const gradCollection: IGrad[] = [
          {
            ...grad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGradToCollectionIfMissing(gradCollection, grad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Grad to an array that doesn't contain it", () => {
        const grad: IGrad = sampleWithRequiredData;
        const gradCollection: IGrad[] = [sampleWithPartialData];
        expectedResult = service.addGradToCollectionIfMissing(gradCollection, grad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grad);
      });

      it('should add only unique Grad to an array', () => {
        const gradArray: IGrad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gradCollection: IGrad[] = [sampleWithRequiredData];
        expectedResult = service.addGradToCollectionIfMissing(gradCollection, ...gradArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const grad: IGrad = sampleWithRequiredData;
        const grad2: IGrad = sampleWithPartialData;
        expectedResult = service.addGradToCollectionIfMissing([], grad, grad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grad);
        expect(expectedResult).toContain(grad2);
      });

      it('should accept null and undefined values', () => {
        const grad: IGrad = sampleWithRequiredData;
        expectedResult = service.addGradToCollectionIfMissing([], null, grad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grad);
      });

      it('should return initial array if no Grad is added', () => {
        const gradCollection: IGrad[] = [sampleWithRequiredData];
        expectedResult = service.addGradToCollectionIfMissing(gradCollection, undefined, null);
        expect(expectedResult).toEqual(gradCollection);
      });
    });

    describe('compareGrad', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGrad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGrad(entity1, entity2);
        const compareResult2 = service.compareGrad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGrad(entity1, entity2);
        const compareResult2 = service.compareGrad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGrad(entity1, entity2);
        const compareResult2 = service.compareGrad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
