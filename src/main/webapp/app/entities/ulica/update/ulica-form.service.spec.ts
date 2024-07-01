import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ulica.test-samples';

import { UlicaFormService } from './ulica-form.service';

describe('Ulica Form Service', () => {
  let service: UlicaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UlicaFormService);
  });

  describe('Service methods', () => {
    describe('createUlicaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUlicaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ulicaNaziv: expect.any(Object),
            grad: expect.any(Object),
            naselje: expect.any(Object),
          }),
        );
      });

      it('passing IUlica should create a new form with FormGroup', () => {
        const formGroup = service.createUlicaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ulicaNaziv: expect.any(Object),
            grad: expect.any(Object),
            naselje: expect.any(Object),
          }),
        );
      });
    });

    describe('getUlica', () => {
      it('should return NewUlica for default Ulica initial value', () => {
        const formGroup = service.createUlicaFormGroup(sampleWithNewData);

        const ulica = service.getUlica(formGroup) as any;

        expect(ulica).toMatchObject(sampleWithNewData);
      });

      it('should return NewUlica for empty Ulica initial value', () => {
        const formGroup = service.createUlicaFormGroup();

        const ulica = service.getUlica(formGroup) as any;

        expect(ulica).toMatchObject({});
      });

      it('should return IUlica', () => {
        const formGroup = service.createUlicaFormGroup(sampleWithRequiredData);

        const ulica = service.getUlica(formGroup) as any;

        expect(ulica).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUlica should not enable id FormControl', () => {
        const formGroup = service.createUlicaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUlica should disable id FormControl', () => {
        const formGroup = service.createUlicaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
