import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../grad.test-samples';

import { GradFormService } from './grad-form.service';

describe('Grad Form Service', () => {
  let service: GradFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GradFormService);
  });

  describe('Service methods', () => {
    describe('createGradFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGradFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            gradNaziv: expect.any(Object),
          }),
        );
      });

      it('passing IGrad should create a new form with FormGroup', () => {
        const formGroup = service.createGradFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            gradNaziv: expect.any(Object),
          }),
        );
      });
    });

    describe('getGrad', () => {
      it('should return NewGrad for default Grad initial value', () => {
        const formGroup = service.createGradFormGroup(sampleWithNewData);

        const grad = service.getGrad(formGroup) as any;

        expect(grad).toMatchObject(sampleWithNewData);
      });

      it('should return NewGrad for empty Grad initial value', () => {
        const formGroup = service.createGradFormGroup();

        const grad = service.getGrad(formGroup) as any;

        expect(grad).toMatchObject({});
      });

      it('should return IGrad', () => {
        const formGroup = service.createGradFormGroup(sampleWithRequiredData);

        const grad = service.getGrad(formGroup) as any;

        expect(grad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGrad should not enable id FormControl', () => {
        const formGroup = service.createGradFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGrad should disable id FormControl', () => {
        const formGroup = service.createGradFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
