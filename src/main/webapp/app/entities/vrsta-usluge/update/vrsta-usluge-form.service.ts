import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVrstaUsluge, NewVrstaUsluge } from '../vrsta-usluge.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVrstaUsluge for edit and NewVrstaUslugeFormGroupInput for create.
 */
type VrstaUslugeFormGroupInput = IVrstaUsluge | PartialWithRequiredKeyOf<NewVrstaUsluge>;

type VrstaUslugeFormDefaults = Pick<NewVrstaUsluge, 'id'>;

type VrstaUslugeFormGroupContent = {
  id: FormControl<IVrstaUsluge['id'] | NewVrstaUsluge['id']>;
  vrstaUslugeNaziv: FormControl<IVrstaUsluge['vrstaUslugeNaziv']>;
};

export type VrstaUslugeFormGroup = FormGroup<VrstaUslugeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VrstaUslugeFormService {
  createVrstaUslugeFormGroup(vrstaUsluge: VrstaUslugeFormGroupInput = { id: null }): VrstaUslugeFormGroup {
    const vrstaUslugeRawValue = {
      ...this.getFormDefaults(),
      ...vrstaUsluge,
    };
    return new FormGroup<VrstaUslugeFormGroupContent>({
      id: new FormControl(
        { value: vrstaUslugeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vrstaUslugeNaziv: new FormControl(vrstaUslugeRawValue.vrstaUslugeNaziv, {
        validators: [Validators.required],
      }),
    });
  }

  getVrstaUsluge(form: VrstaUslugeFormGroup): IVrstaUsluge | NewVrstaUsluge {
    return form.getRawValue() as IVrstaUsluge | NewVrstaUsluge;
  }

  resetForm(form: VrstaUslugeFormGroup, vrstaUsluge: VrstaUslugeFormGroupInput): void {
    const vrstaUslugeRawValue = { ...this.getFormDefaults(), ...vrstaUsluge };
    form.reset(
      {
        ...vrstaUslugeRawValue,
        id: { value: vrstaUslugeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VrstaUslugeFormDefaults {
    return {
      id: null,
    };
  }
}
