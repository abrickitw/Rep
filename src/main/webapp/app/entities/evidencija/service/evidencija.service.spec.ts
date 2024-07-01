import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEvidencija } from '../evidencija.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../evidencija.test-samples';

import { EvidencijaService, RestEvidencija } from './evidencija.service';

const requireRestSample: RestEvidencija = {
  ...sampleWithRequiredData,
  datumIspravka: sampleWithRequiredData.datumIspravka?.format(DATE_FORMAT),
};

describe('Evidencija Service', () => {
  let service: EvidencijaService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvidencija | IEvidencija[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EvidencijaService);
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

    it('should create a Evidencija', () => {
      const evidencija = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evidencija).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Evidencija', () => {
      const evidencija = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evidencija).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Evidencija', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Evidencija', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Evidencija', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEvidencijaToCollectionIfMissing', () => {
      it('should add a Evidencija to an empty array', () => {
        const evidencija: IEvidencija = sampleWithRequiredData;
        expectedResult = service.addEvidencijaToCollectionIfMissing([], evidencija);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evidencija);
      });

      it('should not add a Evidencija to an array that contains it', () => {
        const evidencija: IEvidencija = sampleWithRequiredData;
        const evidencijaCollection: IEvidencija[] = [
          {
            ...evidencija,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvidencijaToCollectionIfMissing(evidencijaCollection, evidencija);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Evidencija to an array that doesn't contain it", () => {
        const evidencija: IEvidencija = sampleWithRequiredData;
        const evidencijaCollection: IEvidencija[] = [sampleWithPartialData];
        expectedResult = service.addEvidencijaToCollectionIfMissing(evidencijaCollection, evidencija);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evidencija);
      });

      it('should add only unique Evidencija to an array', () => {
        const evidencijaArray: IEvidencija[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evidencijaCollection: IEvidencija[] = [sampleWithRequiredData];
        expectedResult = service.addEvidencijaToCollectionIfMissing(evidencijaCollection, ...evidencijaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evidencija: IEvidencija = sampleWithRequiredData;
        const evidencija2: IEvidencija = sampleWithPartialData;
        expectedResult = service.addEvidencijaToCollectionIfMissing([], evidencija, evidencija2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evidencija);
        expect(expectedResult).toContain(evidencija2);
      });

      it('should accept null and undefined values', () => {
        const evidencija: IEvidencija = sampleWithRequiredData;
        expectedResult = service.addEvidencijaToCollectionIfMissing([], null, evidencija, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evidencija);
      });

      it('should return initial array if no Evidencija is added', () => {
        const evidencijaCollection: IEvidencija[] = [sampleWithRequiredData];
        expectedResult = service.addEvidencijaToCollectionIfMissing(evidencijaCollection, undefined, null);
        expect(expectedResult).toEqual(evidencijaCollection);
      });
    });

    describe('compareEvidencija', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvidencija(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEvidencija(entity1, entity2);
        const compareResult2 = service.compareEvidencija(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEvidencija(entity1, entity2);
        const compareResult2 = service.compareEvidencija(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEvidencija(entity1, entity2);
        const compareResult2 = service.compareEvidencija(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
