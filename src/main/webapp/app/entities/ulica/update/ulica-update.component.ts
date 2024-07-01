import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGrad } from 'app/entities/grad/grad.model';
import { GradService } from 'app/entities/grad/service/grad.service';
import { INaselje } from 'app/entities/naselje/naselje.model';
import { NaseljeService } from 'app/entities/naselje/service/naselje.service';
import { UlicaService } from '../service/ulica.service';
import { IUlica } from '../ulica.model';
import { UlicaFormService, UlicaFormGroup } from './ulica-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ulica-update',
  templateUrl: './ulica-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UlicaUpdateComponent implements OnInit {
  isSaving = false;
  ulica: IUlica | null = null;

  gradsSharedCollection: IGrad[] = [];
  naseljesSharedCollection: INaselje[] = [];

  protected ulicaService = inject(UlicaService);
  protected ulicaFormService = inject(UlicaFormService);
  protected gradService = inject(GradService);
  protected naseljeService = inject(NaseljeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UlicaFormGroup = this.ulicaFormService.createUlicaFormGroup();

  compareGrad = (o1: IGrad | null, o2: IGrad | null): boolean => this.gradService.compareGrad(o1, o2);

  compareNaselje = (o1: INaselje | null, o2: INaselje | null): boolean => this.naseljeService.compareNaselje(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ulica }) => {
      this.ulica = ulica;
      if (ulica) {
        this.updateForm(ulica);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ulica = this.ulicaFormService.getUlica(this.editForm);
    if (ulica.id !== null) {
      this.subscribeToSaveResponse(this.ulicaService.update(ulica));
    } else {
      this.subscribeToSaveResponse(this.ulicaService.create(ulica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUlica>>): void {
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

  protected updateForm(ulica: IUlica): void {
    this.ulica = ulica;
    this.ulicaFormService.resetForm(this.editForm, ulica);

    this.gradsSharedCollection = this.gradService.addGradToCollectionIfMissing<IGrad>(this.gradsSharedCollection, ulica.grad);
    this.naseljesSharedCollection = this.naseljeService.addNaseljeToCollectionIfMissing<INaselje>(
      this.naseljesSharedCollection,
      ulica.naselje,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gradService
      .query()
      .pipe(map((res: HttpResponse<IGrad[]>) => res.body ?? []))
      .pipe(map((grads: IGrad[]) => this.gradService.addGradToCollectionIfMissing<IGrad>(grads, this.ulica?.grad)))
      .subscribe((grads: IGrad[]) => (this.gradsSharedCollection = grads));

    this.naseljeService
      .query()
      .pipe(map((res: HttpResponse<INaselje[]>) => res.body ?? []))
      .pipe(map((naseljes: INaselje[]) => this.naseljeService.addNaseljeToCollectionIfMissing<INaselje>(naseljes, this.ulica?.naselje)))
      .subscribe((naseljes: INaselje[]) => (this.naseljesSharedCollection = naseljes));
  }
}
