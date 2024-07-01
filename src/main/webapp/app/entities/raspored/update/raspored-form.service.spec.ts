import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../raspored.test-samples';

import { RasporedFormService } from './raspored-form.service';

describe('Raspored Form Service', () => {
  let service: RasporedFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RasporedFormService);
  });

  describe('Service methods', () => {
    describe('createRasporedFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRasporedFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            datumUsluge: expect.any(Object),
            grad: expect.any(Object),
            naselje: expect.any(Object),
            ulica: expect.any(Object),
            korisnikKreirao: expect.any(Object),
          }),
        );
      });

      it('passing IRaspored should create a new form with FormGroup', () => {
        const formGroup = service.createRasporedFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            datumUsluge: expect.any(Object),
            grad: expect.any(Object),
            naselje: expect.any(Object),
            ulica: expect.any(Object),
            korisnikKreirao: expect.any(Object),
          }),
        );
      });
    });

    describe('getRaspored', () => {
      it('should return NewRaspored for default Raspored initial value', () => {
        const formGroup = service.createRasporedFormGroup(sampleWithNewData);

        const raspored = service.getRaspored(formGroup) as any;

        expect(raspored).toMatchObject(sampleWithNewData);
      });

      it('should return NewRaspored for empty Raspored initial value', () => {
        const formGroup = service.createRasporedFormGroup();

        const raspored = service.getRaspored(formGroup) as any;

        expect(raspored).toMatchObject({});
      });

      it('should return IRaspored', () => {
        const formGroup = service.createRasporedFormGroup(sampleWithRequiredData);

        const raspored = service.getRaspored(formGroup) as any;

        expect(raspored).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRaspored should not enable id FormControl', () => {
        const formGroup = service.createRasporedFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRaspored should disable id FormControl', () => {
        const formGroup = service.createRasporedFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
