import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRaspored } from '../raspored.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../raspored.test-samples';

import { RasporedService, RestRaspored } from './raspored.service';

const requireRestSample: RestRaspored = {
  ...sampleWithRequiredData,
  datumUsluge: sampleWithRequiredData.datumUsluge?.format(DATE_FORMAT),
};

describe('Raspored Service', () => {
  let service: RasporedService;
  let httpMock: HttpTestingController;
  let expectedResult: IRaspored | IRaspored[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RasporedService);
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

    it('should create a Raspored', () => {
      const raspored = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(raspored).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Raspored', () => {
      const raspored = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(raspored).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Raspored', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Raspored', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Raspored', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRasporedToCollectionIfMissing', () => {
      it('should add a Raspored to an empty array', () => {
        const raspored: IRaspored = sampleWithRequiredData;
        expectedResult = service.addRasporedToCollectionIfMissing([], raspored);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raspored);
      });

      it('should not add a Raspored to an array that contains it', () => {
        const raspored: IRaspored = sampleWithRequiredData;
        const rasporedCollection: IRaspored[] = [
          {
            ...raspored,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRasporedToCollectionIfMissing(rasporedCollection, raspored);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Raspored to an array that doesn't contain it", () => {
        const raspored: IRaspored = sampleWithRequiredData;
        const rasporedCollection: IRaspored[] = [sampleWithPartialData];
        expectedResult = service.addRasporedToCollectionIfMissing(rasporedCollection, raspored);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raspored);
      });

      it('should add only unique Raspored to an array', () => {
        const rasporedArray: IRaspored[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rasporedCollection: IRaspored[] = [sampleWithRequiredData];
        expectedResult = service.addRasporedToCollectionIfMissing(rasporedCollection, ...rasporedArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const raspored: IRaspored = sampleWithRequiredData;
        const raspored2: IRaspored = sampleWithPartialData;
        expectedResult = service.addRasporedToCollectionIfMissing([], raspored, raspored2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raspored);
        expect(expectedResult).toContain(raspored2);
      });

      it('should accept null and undefined values', () => {
        const raspored: IRaspored = sampleWithRequiredData;
        expectedResult = service.addRasporedToCollectionIfMissing([], null, raspored, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raspored);
      });

      it('should return initial array if no Raspored is added', () => {
        const rasporedCollection: IRaspored[] = [sampleWithRequiredData];
        expectedResult = service.addRasporedToCollectionIfMissing(rasporedCollection, undefined, null);
        expect(expectedResult).toEqual(rasporedCollection);
      });
    });

    describe('compareRaspored', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRaspored(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRaspored(entity1, entity2);
        const compareResult2 = service.compareRaspored(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRaspored(entity1, entity2);
        const compareResult2 = service.compareRaspored(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRaspored(entity1, entity2);
        const compareResult2 = service.compareRaspored(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
