import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../naselje.test-samples';

import { NaseljeFormService } from './naselje-form.service';

describe('Naselje Form Service', () => {
  let service: NaseljeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NaseljeFormService);
  });

  describe('Service methods', () => {
    describe('createNaseljeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNaseljeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            naseljeNaziv: expect.any(Object),
            grad: expect.any(Object),
          }),
        );
      });

      it('passing INaselje should create a new form with FormGroup', () => {
        const formGroup = service.createNaseljeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            naseljeNaziv: expect.any(Object),
            grad: expect.any(Object),
          }),
        );
      });
    });

    describe('getNaselje', () => {
      it('should return NewNaselje for default Naselje initial value', () => {
        const formGroup = service.createNaseljeFormGroup(sampleWithNewData);

        const naselje = service.getNaselje(formGroup) as any;

        expect(naselje).toMatchObject(sampleWithNewData);
      });

      it('should return NewNaselje for empty Naselje initial value', () => {
        const formGroup = service.createNaseljeFormGroup();

        const naselje = service.getNaselje(formGroup) as any;

        expect(naselje).toMatchObject({});
      });

      it('should return INaselje', () => {
        const formGroup = service.createNaseljeFormGroup(sampleWithRequiredData);

        const naselje = service.getNaselje(formGroup) as any;

        expect(naselje).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INaselje should not enable id FormControl', () => {
        const formGroup = service.createNaseljeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNaselje should disable id FormControl', () => {
        const formGroup = service.createNaseljeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
