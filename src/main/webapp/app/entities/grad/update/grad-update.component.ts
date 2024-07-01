import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGrad } from '../grad.model';
import { GradService } from '../service/grad.service';
import { GradFormService, GradFormGroup } from './grad-form.service';

@Component({
  standalone: true,
  selector: 'jhi-grad-update',
  templateUrl: './grad-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GradUpdateComponent implements OnInit {
  isSaving = false;
  grad: IGrad | null = null;

  protected gradService = inject(GradService);
  protected gradFormService = inject(GradFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GradFormGroup = this.gradFormService.createGradFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grad }) => {
      this.grad = grad;
      if (grad) {
        this.updateForm(grad);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const grad = this.gradFormService.getGrad(this.editForm);
    if (grad.id !== null) {
      this.subscribeToSaveResponse(this.gradService.update(grad));
    } else {
      this.subscribeToSaveResponse(this.gradService.create(grad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGrad>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(grad: IGrad): void {
    this.grad = grad;
    this.gradFormService.resetForm(this.editForm, grad);
  }
}
