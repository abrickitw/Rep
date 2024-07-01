import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStatusEvidencije } from '../status-evidencije.model';
import { StatusEvidencijeService } from '../service/status-evidencije.service';
import { StatusEvidencijeFormService, StatusEvidencijeFormGroup } from './status-evidencije-form.service';

@Component({
  standalone: true,
  selector: 'jhi-status-evidencije-update',
  templateUrl: './status-evidencije-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StatusEvidencijeUpdateComponent implements OnInit {
  isSaving = false;
  statusEvidencije: IStatusEvidencije | null = null;

  protected statusEvidencijeService = inject(StatusEvidencijeService);
  protected statusEvidencijeFormService = inject(StatusEvidencijeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StatusEvidencijeFormGroup = this.statusEvidencijeFormService.createStatusEvidencijeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statusEvidencije }) => {
      this.statusEvidencije = statusEvidencije;
      if (statusEvidencije) {
        this.updateForm(statusEvidencije);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const statusEvidencije = this.statusEvidencijeFormService.getStatusEvidencije(this.editForm);
    if (statusEvidencije.id !== null) {
      this.subscribeToSaveResponse(this.statusEvidencijeService.update(statusEvidencije));
    } else {
      this.subscribeToSaveResponse(this.statusEvidencijeService.create(statusEvidencije));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStatusEvidencije>>): void {
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

  protected updateForm(statusEvidencije: IStatusEvidencije): void {
    this.statusEvidencije = statusEvidencije;
    this.statusEvidencijeFormService.resetForm(this.editForm, statusEvidencije);
  }
}
