import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../status-evidencije.test-samples';

import { StatusEvidencijeFormService } from './status-evidencije-form.service';

describe('StatusEvidencije Form Service', () => {
  let service: StatusEvidencijeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StatusEvidencijeFormService);
  });

  describe('Service methods', () => {
    describe('createStatusEvidencijeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStatusEvidencijeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statusEvidencijeNaziv: expect.any(Object),
          }),
        );
      });

      it('passing IStatusEvidencije should create a new form with FormGroup', () => {
        const formGroup = service.createStatusEvidencijeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statusEvidencijeNaziv: expect.any(Object),
          }),
        );
      });
    });

    describe('getStatusEvidencije', () => {
      it('should return NewStatusEvidencije for default StatusEvidencije initial value', () => {
        const formGroup = service.createStatusEvidencijeFormGroup(sampleWithNewData);

        const statusEvidencije = service.getStatusEvidencije(formGroup) as any;

        expect(statusEvidencije).toMatchObject(sampleWithNewData);
      });

      it('should return NewStatusEvidencije for empty StatusEvidencije initial value', () => {
        const formGroup = service.createStatusEvidencijeFormGroup();

        const statusEvidencije = service.getStatusEvidencije(formGroup) as any;

        expect(statusEvidencije).toMatchObject({});
      });

      it('should return IStatusEvidencije', () => {
        const formGroup = service.createStatusEvidencijeFormGroup(sampleWithRequiredData);

        const statusEvidencije = service.getStatusEvidencije(formGroup) as any;

        expect(statusEvidencije).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStatusEvidencije should not enable id FormControl', () => {
        const formGroup = service.createStatusEvidencijeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStatusEvidencije should disable id FormControl', () => {
        const formGroup = service.createStatusEvidencijeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
