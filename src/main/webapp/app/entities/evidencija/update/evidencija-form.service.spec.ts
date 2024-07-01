import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../evidencija.test-samples';

import { EvidencijaFormService } from './evidencija-form.service';

describe('Evidencija Form Service', () => {
  let service: EvidencijaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvidencijaFormService);
  });

  describe('Service methods', () => {
    describe('createEvidencijaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvidencijaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nazivEvidencija: expect.any(Object),
            vrijemeUsluge: expect.any(Object),
            komentar: expect.any(Object),
            imeStanara: expect.any(Object),
            prezimeStanara: expect.any(Object),
            kontaktStanara: expect.any(Object),
            datumIspravka: expect.any(Object),
            komentarIspravka: expect.any(Object),
            kucniBroj: expect.any(Object),
            korisnikIzvrsio: expect.any(Object),
            korisnikIspravio: expect.any(Object),
            raspored: expect.any(Object),
            vrstaUsluge: expect.any(Object),
            statusEvidencije: expect.any(Object),
          }),
        );
      });

      it('passing IEvidencija should create a new form with FormGroup', () => {
        const formGroup = service.createEvidencijaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nazivEvidencija: expect.any(Object),
            vrijemeUsluge: expect.any(Object),
            komentar: expect.any(Object),
            imeStanara: expect.any(Object),
            prezimeStanara: expect.any(Object),
            kontaktStanara: expect.any(Object),
            datumIspravka: expect.any(Object),
            komentarIspravka: expect.any(Object),
            kucniBroj: expect.any(Object),
            korisnikIzvrsio: expect.any(Object),
            korisnikIspravio: expect.any(Object),
            raspored: expect.any(Object),
            vrstaUsluge: expect.any(Object),
            statusEvidencije: expect.any(Object),
          }),
        );
      });
    });

    describe('getEvidencija', () => {
      it('should return NewEvidencija for default Evidencija initial value', () => {
        const formGroup = service.createEvidencijaFormGroup(sampleWithNewData);

        const evidencija = service.getEvidencija(formGroup) as any;

        expect(evidencija).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvidencija for empty Evidencija initial value', () => {
        const formGroup = service.createEvidencijaFormGroup();

        const evidencija = service.getEvidencija(formGroup) as any;

        expect(evidencija).toMatchObject({});
      });

      it('should return IEvidencija', () => {
        const formGroup = service.createEvidencijaFormGroup(sampleWithRequiredData);

        const evidencija = service.getEvidencija(formGroup) as any;

        expect(evidencija).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvidencija should not enable id FormControl', () => {
        const formGroup = service.createEvidencijaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvidencija should disable id FormControl', () => {
        const formGroup = service.createEvidencijaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
