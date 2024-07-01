import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEvidencija, NewEvidencija } from '../evidencija.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvidencija for edit and NewEvidencijaFormGroupInput for create.
 */
type EvidencijaFormGroupInput = IEvidencija | PartialWithRequiredKeyOf<NewEvidencija>;

type EvidencijaFormDefaults = Pick<NewEvidencija, 'id'>;

type EvidencijaFormGroupContent = {
  id: FormControl<IEvidencija['id'] | NewEvidencija['id']>;
  nazivEvidencija: FormControl<IEvidencija['nazivEvidencija']>;
  vrijemeUsluge: FormControl<IEvidencija['vrijemeUsluge']>;
  komentar: FormControl<IEvidencija['komentar']>;
  imeStanara: FormControl<IEvidencija['imeStanara']>;
  prezimeStanara: FormControl<IEvidencija['prezimeStanara']>;
  kontaktStanara: FormControl<IEvidencija['kontaktStanara']>;
  datumIspravka: FormControl<IEvidencija['datumIspravka']>;
  komentarIspravka: FormControl<IEvidencija['komentarIspravka']>;
  kucniBroj: FormControl<IEvidencija['kucniBroj']>;
  korisnikIzvrsio: FormControl<IEvidencija['korisnikIzvrsio']>;
  korisnikIspravio: FormControl<IEvidencija['korisnikIspravio']>;
  raspored: FormControl<IEvidencija['raspored']>;
  vrstaUsluge: FormControl<IEvidencija['vrstaUsluge']>;
  statusEvidencije: FormControl<IEvidencija['statusEvidencije']>;
};

export type EvidencijaFormGroup = FormGroup<EvidencijaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvidencijaFormService {
  createEvidencijaFormGroup(evidencija: EvidencijaFormGroupInput = { id: null }): EvidencijaFormGroup {
    const evidencijaRawValue = {
      ...this.getFormDefaults(),
      ...evidencija,
    };
    return new FormGroup<EvidencijaFormGroupContent>({
      id: new FormControl(
        { value: evidencijaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nazivEvidencija: new FormControl(evidencijaRawValue.nazivEvidencija, {
        validators: [Validators.required],
      }),
      vrijemeUsluge: new FormControl(evidencijaRawValue.vrijemeUsluge, {
        validators: [Validators.required],
      }),
      komentar: new FormControl(evidencijaRawValue.komentar, {
        validators: [Validators.required],
      }),
      imeStanara: new FormControl(evidencijaRawValue.imeStanara, {
        validators: [Validators.required],
      }),
      prezimeStanara: new FormControl(evidencijaRawValue.prezimeStanara, {
        validators: [Validators.required],
      }),
      kontaktStanara: new FormControl(evidencijaRawValue.kontaktStanara, {
        validators: [Validators.required],
      }),
      datumIspravka: new FormControl(evidencijaRawValue.datumIspravka),
      komentarIspravka: new FormControl(evidencijaRawValue.komentarIspravka),
      kucniBroj: new FormControl(evidencijaRawValue.kucniBroj),
      korisnikIzvrsio: new FormControl(evidencijaRawValue.korisnikIzvrsio),
      korisnikIspravio: new FormControl(evidencijaRawValue.korisnikIspravio),
      raspored: new FormControl(evidencijaRawValue.raspored),
      vrstaUsluge: new FormControl(evidencijaRawValue.vrstaUsluge),
      statusEvidencije: new FormControl(evidencijaRawValue.statusEvidencije),
    });
  }

  getEvidencija(form: EvidencijaFormGroup): IEvidencija | NewEvidencija {
    return form.getRawValue() as IEvidencija | NewEvidencija;
  }

  resetForm(form: EvidencijaFormGroup, evidencija: EvidencijaFormGroupInput): void {
    const evidencijaRawValue = { ...this.getFormDefaults(), ...evidencija };
    form.reset(
      {
        ...evidencijaRawValue,
        id: { value: evidencijaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EvidencijaFormDefaults {
    return {
      id: null,
    };
  }
}
