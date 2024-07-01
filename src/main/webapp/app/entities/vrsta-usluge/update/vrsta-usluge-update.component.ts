import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVrstaUsluge } from '../vrsta-usluge.model';
import { VrstaUslugeService } from '../service/vrsta-usluge.service';
import { VrstaUslugeFormService, VrstaUslugeFormGroup } from './vrsta-usluge-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vrsta-usluge-update',
  templateUrl: './vrsta-usluge-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VrstaUslugeUpdateComponent implements OnInit {
  isSaving = false;
  vrstaUsluge: IVrstaUsluge | null = null;

  protected vrstaUslugeService = inject(VrstaUslugeService);
  protected vrstaUslugeFormService = inject(VrstaUslugeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VrstaUslugeFormGroup = this.vrstaUslugeFormService.createVrstaUslugeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vrstaUsluge }) => {
      this.vrstaUsluge = vrstaUsluge;
      if (vrstaUsluge) {
        this.updateForm(vrstaUsluge);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vrstaUsluge = this.vrstaUslugeFormService.getVrstaUsluge(this.editForm);
    if (vrstaUsluge.id !== null) {
      this.subscribeToSaveResponse(this.vrstaUslugeService.update(vrstaUsluge));
    } else {
      this.subscribeToSaveResponse(this.vrstaUslugeService.create(vrstaUsluge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVrstaUsluge>>): void {
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

  protected updateForm(vrstaUsluge: IVrstaUsluge): void {
    this.vrstaUsluge = vrstaUsluge;
    this.vrstaUslugeFormService.resetForm(this.editForm, vrstaUsluge);
  }
}
