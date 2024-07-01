import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { INaselje } from '../naselje.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../naselje.test-samples';

import { NaseljeService } from './naselje.service';

const requireRestSample: INaselje = {
  ...sampleWithRequiredData,
};

describe('Naselje Service', () => {
  let service: NaseljeService;
  let httpMock: HttpTestingController;
  let expectedResult: INaselje | INaselje[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(NaseljeService);
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

    it('should create a Naselje', () => {
      const naselje = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(naselje).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Naselje', () => {
      const naselje = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(naselje).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Naselje', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Naselje', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Naselje', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNaseljeToCollectionIfMissing', () => {
      it('should add a Naselje to an empty array', () => {
        const naselje: INaselje = sampleWithRequiredData;
        expectedResult = service.addNaseljeToCollectionIfMissing([], naselje);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(naselje);
      });

      it('should not add a Naselje to an array that contains it', () => {
        const naselje: INaselje = sampleWithRequiredData;
        const naseljeCollection: INaselje[] = [
          {
            ...naselje,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNaseljeToCollectionIfMissing(naseljeCollection, naselje);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Naselje to an array that doesn't contain it", () => {
        const naselje: INaselje = sampleWithRequiredData;
        const naseljeCollection: INaselje[] = [sampleWithPartialData];
        expectedResult = service.addNaseljeToCollectionIfMissing(naseljeCollection, naselje);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(naselje);
      });

      it('should add only unique Naselje to an array', () => {
        const naseljeArray: INaselje[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const naseljeCollection: INaselje[] = [sampleWithRequiredData];
        expectedResult = service.addNaseljeToCollectionIfMissing(naseljeCollection, ...naseljeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const naselje: INaselje = sampleWithRequiredData;
        const naselje2: INaselje = sampleWithPartialData;
        expectedResult = service.addNaseljeToCollectionIfMissing([], naselje, naselje2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(naselje);
        expect(expectedResult).toContain(naselje2);
      });

      it('should accept null and undefined values', () => {
        const naselje: INaselje = sampleWithRequiredData;
        expectedResult = service.addNaseljeToCollectionIfMissing([], null, naselje, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(naselje);
      });

      it('should return initial array if no Naselje is added', () => {
        const naseljeCollection: INaselje[] = [sampleWithRequiredData];
        expectedResult = service.addNaseljeToCollectionIfMissing(naseljeCollection, undefined, null);
        expect(expectedResult).toEqual(naseljeCollection);
      });
    });

    describe('compareNaselje', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNaselje(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNaselje(entity1, entity2);
        const compareResult2 = service.compareNaselje(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNaselje(entity1, entity2);
        const compareResult2 = service.compareNaselje(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNaselje(entity1, entity2);
        const compareResult2 = service.compareNaselje(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
