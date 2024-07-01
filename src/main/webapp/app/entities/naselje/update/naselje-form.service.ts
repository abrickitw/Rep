import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INaselje, NewNaselje } from '../naselje.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INaselje for edit and NewNaseljeFormGroupInput for create.
 */
type NaseljeFormGroupInput = INaselje | PartialWithRequiredKeyOf<NewNaselje>;

type NaseljeFormDefaults = Pick<NewNaselje, 'id'>;

type NaseljeFormGroupContent = {
  id: FormControl<INaselje['id'] | NewNaselje['id']>;
  naseljeNaziv: FormControl<INaselje['naseljeNaziv']>;
  grad: FormControl<INaselje['grad']>;
};

export type NaseljeFormGroup = FormGroup<NaseljeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NaseljeFormService {
  createNaseljeFormGroup(naselje: NaseljeFormGroupInput = { id: null }): NaseljeFormGroup {
    const naseljeRawValue = {
      ...this.getFormDefaults(),
      ...naselje,
    };
    return new FormGroup<NaseljeFormGroupContent>({
      id: new FormControl(
        { value: naseljeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      naseljeNaziv: new FormControl(naseljeRawValue.naseljeNaziv, {
        validators: [Validators.required],
      }),
      grad: new FormControl(naseljeRawValue.grad),
    });
  }

  getNaselje(form: NaseljeFormGroup): INaselje | NewNaselje {
    return form.getRawValue() as INaselje | NewNaselje;
  }

  resetForm(form: NaseljeFormGroup, naselje: NaseljeFormGroupInput): void {
    const naseljeRawValue = { ...this.getFormDefaults(), ...naselje };
    form.reset(
      {
        ...naseljeRawValue,
        id: { value: naseljeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NaseljeFormDefaults {
    return {
      id: null,
    };
  }
}
