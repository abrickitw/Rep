import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUlica, NewUlica } from '../ulica.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUlica for edit and NewUlicaFormGroupInput for create.
 */
type UlicaFormGroupInput = IUlica | PartialWithRequiredKeyOf<NewUlica>;

type UlicaFormDefaults = Pick<NewUlica, 'id'>;

type UlicaFormGroupContent = {
  id: FormControl<IUlica['id'] | NewUlica['id']>;
  ulicaNaziv: FormControl<IUlica['ulicaNaziv']>;
  grad: FormControl<IUlica['grad']>;
  naselje: FormControl<IUlica['naselje']>;
};

export type UlicaFormGroup = FormGroup<UlicaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UlicaFormService {
  createUlicaFormGroup(ulica: UlicaFormGroupInput = { id: null }): UlicaFormGroup {
    const ulicaRawValue = {
      ...this.getFormDefaults(),
      ...ulica,
    };
    return new FormGroup<UlicaFormGroupContent>({
      id: new FormControl(
        { value: ulicaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ulicaNaziv: new FormControl(ulicaRawValue.ulicaNaziv, {
        validators: [Validators.required],
      }),
      grad: new FormControl(ulicaRawValue.grad),
      naselje: new FormControl(ulicaRawValue.naselje),
    });
  }

  getUlica(form: UlicaFormGroup): IUlica | NewUlica {
    return form.getRawValue() as IUlica | NewUlica;
  }

  resetForm(form: UlicaFormGroup, ulica: UlicaFormGroupInput): void {
    const ulicaRawValue = { ...this.getFormDefaults(), ...ulica };
    form.reset(
      {
        ...ulicaRawValue,
        id: { value: ulicaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UlicaFormDefaults {
    return {
      id: null,
    };
  }
}
