import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGrad, NewGrad } from '../grad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGrad for edit and NewGradFormGroupInput for create.
 */
type GradFormGroupInput = IGrad | PartialWithRequiredKeyOf<NewGrad>;

type GradFormDefaults = Pick<NewGrad, 'id'>;

type GradFormGroupContent = {
  id: FormControl<IGrad['id'] | NewGrad['id']>;
  gradNaziv: FormControl<IGrad['gradNaziv']>;
};

export type GradFormGroup = FormGroup<GradFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GradFormService {
  createGradFormGroup(grad: GradFormGroupInput = { id: null }): GradFormGroup {
    const gradRawValue = {
      ...this.getFormDefaults(),
      ...grad,
    };
    return new FormGroup<GradFormGroupContent>({
      id: new FormControl(
        { value: gradRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      gradNaziv: new FormControl(gradRawValue.gradNaziv, {
        validators: [Validators.required],
      }),
    });
  }

  getGrad(form: GradFormGroup): IGrad | NewGrad {
    return form.getRawValue() as IGrad | NewGrad;
  }

  resetForm(form: GradFormGroup, grad: GradFormGroupInput): void {
    const gradRawValue = { ...this.getFormDefaults(), ...grad };
    form.reset(
      {
        ...gradRawValue,
        id: { value: gradRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GradFormDefaults {
    return {
      id: null,
    };
  }
}
