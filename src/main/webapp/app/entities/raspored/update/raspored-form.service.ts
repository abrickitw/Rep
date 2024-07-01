import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRaspored, NewRaspored } from '../raspored.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRaspored for edit and NewRasporedFormGroupInput for create.
 */
type RasporedFormGroupInput = IRaspored | PartialWithRequiredKeyOf<NewRaspored>;

type RasporedFormDefaults = Pick<NewRaspored, 'id'>;

type RasporedFormGroupContent = {
  id: FormControl<IRaspored['id'] | NewRaspored['id']>;
  datumUsluge: FormControl<IRaspored['datumUsluge']>;
  grad: FormControl<IRaspored['grad']>;
  naselje: FormControl<IRaspored['naselje']>;
  ulica: FormControl<IRaspored['ulica']>;
  korisnikKreirao: FormControl<IRaspored['korisnikKreirao']>;
};

export type RasporedFormGroup = FormGroup<RasporedFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RasporedFormService {
  createRasporedFormGroup(raspored: RasporedFormGroupInput = { id: null }): RasporedFormGroup {
    const rasporedRawValue = {
      ...this.getFormDefaults(),
      ...raspored,
    };
    return new FormGroup<RasporedFormGroupContent>({
      id: new FormControl(
        { value: rasporedRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      datumUsluge: new FormControl(rasporedRawValue.datumUsluge, {
        validators: [Validators.required],
      }),
      grad: new FormControl(rasporedRawValue.grad),
      naselje: new FormControl(rasporedRawValue.naselje),
      ulica: new FormControl(rasporedRawValue.ulica),
      korisnikKreirao: new FormControl(rasporedRawValue.korisnikKreirao),
    });
  }

  getRaspored(form: RasporedFormGroup): IRaspored | NewRaspored {
    return form.getRawValue() as IRaspored | NewRaspored;
  }

  resetForm(form: RasporedFormGroup, raspored: RasporedFormGroupInput): void {
    const rasporedRawValue = { ...this.getFormDefaults(), ...raspored };
    form.reset(
      {
        ...rasporedRawValue,
        id: { value: rasporedRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RasporedFormDefaults {
    return {
      id: null,
    };
  }
}
