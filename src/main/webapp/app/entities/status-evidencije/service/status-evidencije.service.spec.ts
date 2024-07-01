import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStatusEvidencije } from '../status-evidencije.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../status-evidencije.test-samples';

import { StatusEvidencijeService } from './status-evidencije.service';

const requireRestSample: IStatusEvidencije = {
  ...sampleWithRequiredData,
};

describe('StatusEvidencije Service', () => {
  let service: StatusEvidencijeService;
  let httpMock: HttpTestingController;
  let expectedResult: IStatusEvidencije | IStatusEvidencije[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StatusEvidencijeService);
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

    it('should create a StatusEvidencije', () => {
      const statusEvidencije = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(statusEvidencije).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StatusEvidencije', () => {
      const statusEvidencije = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(statusEvidencije).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StatusEvidencije', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StatusEvidencije', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StatusEvidencije', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStatusEvidencijeToCollectionIfMissing', () => {
      it('should add a StatusEvidencije to an empty array', () => {
        const statusEvidencije: IStatusEvidencije = sampleWithRequiredData;
        expectedResult = service.addStatusEvidencijeToCollectionIfMissing([], statusEvidencije);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(statusEvidencije);
      });

      it('should not add a StatusEvidencije to an array that contains it', () => {
        const statusEvidencije: IStatusEvidencije = sampleWithRequiredData;
        const statusEvidencijeCollection: IStatusEvidencije[] = [
          {
            ...statusEvidencije,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStatusEvidencijeToCollectionIfMissing(statusEvidencijeCollection, statusEvidencije);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StatusEvidencije to an array that doesn't contain it", () => {
        const statusEvidencije: IStatusEvidencije = sampleWithRequiredData;
        const statusEvidencijeCollection: IStatusEvidencije[] = [sampleWithPartialData];
        expectedResult = service.addStatusEvidencijeToCollectionIfMissing(statusEvidencijeCollection, statusEvidencije);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statusEvidencije);
      });

      it('should add only unique StatusEvidencije to an array', () => {
        const statusEvidencijeArray: IStatusEvidencije[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const statusEvidencijeCollection: IStatusEvidencije[] = [sampleWithRequiredData];
        expectedResult = service.addStatusEvidencijeToCollectionIfMissing(statusEvidencijeCollection, ...statusEvidencijeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const statusEvidencije: IStatusEvidencije = sampleWithRequiredData;
        const statusEvidencije2: IStatusEvidencije = sampleWithPartialData;
        expectedResult = service.addStatusEvidencijeToCollectionIfMissing([], statusEvidencije, statusEvidencije2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statusEvidencije);
        expect(expectedResult).toContain(statusEvidencije2);
      });

      it('should accept null and undefined values', () => {
        const statusEvidencije: IStatusEvidencije = sampleWithRequiredData;
        expectedResult = service.addStatusEvidencijeToCollectionIfMissing([], null, statusEvidencije, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(statusEvidencije);
      });

      it('should return initial array if no StatusEvidencije is added', () => {
        const statusEvidencijeCollection: IStatusEvidencije[] = [sampleWithRequiredData];
        expectedResult = service.addStatusEvidencijeToCollectionIfMissing(statusEvidencijeCollection, undefined, null);
        expect(expectedResult).toEqual(statusEvidencijeCollection);
      });
    });

    describe('compareStatusEvidencije', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStatusEvidencije(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStatusEvidencije(entity1, entity2);
        const compareResult2 = service.compareStatusEvidencije(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStatusEvidencije(entity1, entity2);
        const compareResult2 = service.compareStatusEvidencije(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStatusEvidencije(entity1, entity2);
        const compareResult2 = service.compareStatusEvidencije(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
