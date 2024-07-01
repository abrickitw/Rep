import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStatusEvidencije, NewStatusEvidencije } from '../status-evidencije.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatusEvidencije for edit and NewStatusEvidencijeFormGroupInput for create.
 */
type StatusEvidencijeFormGroupInput = IStatusEvidencije | PartialWithRequiredKeyOf<NewStatusEvidencije>;

type StatusEvidencijeFormDefaults = Pick<NewStatusEvidencije, 'id'>;

type StatusEvidencijeFormGroupContent = {
  id: FormControl<IStatusEvidencije['id'] | NewStatusEvidencije['id']>;
  statusEvidencijeNaziv: FormControl<IStatusEvidencije['statusEvidencijeNaziv']>;
};

export type StatusEvidencijeFormGroup = FormGroup<StatusEvidencijeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StatusEvidencijeFormService {
  createStatusEvidencijeFormGroup(statusEvidencije: StatusEvidencijeFormGroupInput = { id: null }): StatusEvidencijeFormGroup {
    const statusEvidencijeRawValue = {
      ...this.getFormDefaults(),
      ...statusEvidencije,
    };
    return new FormGroup<StatusEvidencijeFormGroupContent>({
      id: new FormControl(
        { value: statusEvidencijeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      statusEvidencijeNaziv: new FormControl(statusEvidencijeRawValue.statusEvidencijeNaziv, {
        validators: [Validators.required],
      }),
    });
  }

  getStatusEvidencije(form: StatusEvidencijeFormGroup): IStatusEvidencije | NewStatusEvidencije {
    return form.getRawValue() as IStatusEvidencije | NewStatusEvidencije;
  }

  resetForm(form: StatusEvidencijeFormGroup, statusEvidencije: StatusEvidencijeFormGroupInput): void {
    const statusEvidencijeRawValue = { ...this.getFormDefaults(), ...statusEvidencije };
    form.reset(
      {
        ...statusEvidencijeRawValue,
        id: { value: statusEvidencijeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StatusEvidencijeFormDefaults {
    return {
      id: null,
    };
  }
}
