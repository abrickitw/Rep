import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGrad } from 'app/entities/grad/grad.model';
import { GradService } from 'app/entities/grad/service/grad.service';
import { INaselje } from '../naselje.model';
import { NaseljeService } from '../service/naselje.service';
import { NaseljeFormService, NaseljeFormGroup } from './naselje-form.service';

@Component({
  standalone: true,
  selector: 'jhi-naselje-update',
  templateUrl: './naselje-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NaseljeUpdateComponent implements OnInit {
  isSaving = false;
  naselje: INaselje | null = null;

  gradsSharedCollection: IGrad[] = [];

  protected naseljeService = inject(NaseljeService);
  protected naseljeFormService = inject(NaseljeFormService);
  protected gradService = inject(GradService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NaseljeFormGroup = this.naseljeFormService.createNaseljeFormGroup();

  compareGrad = (o1: IGrad | null, o2: IGrad | null): boolean => this.gradService.compareGrad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ naselje }) => {
      this.naselje = naselje;
      if (naselje) {
        this.updateForm(naselje);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const naselje = this.naseljeFormService.getNaselje(this.editForm);
    if (naselje.id !== null) {
      this.subscribeToSaveResponse(this.naseljeService.update(naselje));
    } else {
      this.subscribeToSaveResponse(this.naseljeService.create(naselje));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INaselje>>): void {
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

  protected updateForm(naselje: INaselje): void {
    this.naselje = naselje;
    this.naseljeFormService.resetForm(this.editForm, naselje);

    this.gradsSharedCollection = this.gradService.addGradToCollectionIfMissing<IGrad>(this.gradsSharedCollection, naselje.grad);
  }

  protected loadRelationshipsOptions(): void {
    this.gradService
      .query()
      .pipe(map((res: HttpResponse<IGrad[]>) => res.body ?? []))
      .pipe(map((grads: IGrad[]) => this.gradService.addGradToCollectionIfMissing<IGrad>(grads, this.naselje?.grad)))
      .subscribe((grads: IGrad[]) => (this.gradsSharedCollection = grads));
  }
}
