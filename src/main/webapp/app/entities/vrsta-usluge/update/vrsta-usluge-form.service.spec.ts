import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vrsta-usluge.test-samples';

import { VrstaUslugeFormService } from './vrsta-usluge-form.service';

describe('VrstaUsluge Form Service', () => {
  let service: VrstaUslugeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VrstaUslugeFormService);
  });

  describe('Service methods', () => {
    describe('createVrstaUslugeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVrstaUslugeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vrstaUslugeNaziv: expect.any(Object),
          }),
        );
      });

      it('passing IVrstaUsluge should create a new form with FormGroup', () => {
        const formGroup = service.createVrstaUslugeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vrstaUslugeNaziv: expect.any(Object),
          }),
        );
      });
    });

    describe('getVrstaUsluge', () => {
      it('should return NewVrstaUsluge for default VrstaUsluge initial value', () => {
        const formGroup = service.createVrstaUslugeFormGroup(sampleWithNewData);

        const vrstaUsluge = service.getVrstaUsluge(formGroup) as any;

        expect(vrstaUsluge).toMatchObject(sampleWithNewData);
      });

      it('should return NewVrstaUsluge for empty VrstaUsluge initial value', () => {
        const formGroup = service.createVrstaUslugeFormGroup();

        const vrstaUsluge = service.getVrstaUsluge(formGroup) as any;

        expect(vrstaUsluge).toMatchObject({});
      });

      it('should return IVrstaUsluge', () => {
        const formGroup = service.createVrstaUslugeFormGroup(sampleWithRequiredData);

        const vrstaUsluge = service.getVrstaUsluge(formGroup) as any;

        expect(vrstaUsluge).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVrstaUsluge should not enable id FormControl', () => {
        const formGroup = service.createVrstaUslugeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVrstaUsluge should disable id FormControl', () => {
        const formGroup = service.createVrstaUslugeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
