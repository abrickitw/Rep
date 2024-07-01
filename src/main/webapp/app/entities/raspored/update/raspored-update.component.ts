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
import { IUlica } from 'app/entities/ulica/ulica.model';
import { UlicaService } from 'app/entities/ulica/service/ulica.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { RasporedService } from '../service/raspored.service';
import { IRaspored } from '../raspored.model';
import { RasporedFormService, RasporedFormGroup } from './raspored-form.service';

@Component({
  standalone: true,
  selector: 'jhi-raspored-update',
  templateUrl: './raspored-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RasporedUpdateComponent implements OnInit {
  isSaving = false;
  raspored: IRaspored | null = null;

  gradsSharedCollection: IGrad[] = [];
  naseljesSharedCollection: INaselje[] = [];
  ulicasSharedCollection: IUlica[] = [];
  usersSharedCollection: IUser[] = [];

  protected rasporedService = inject(RasporedService);
  protected rasporedFormService = inject(RasporedFormService);
  protected gradService = inject(GradService);
  protected naseljeService = inject(NaseljeService);
  protected ulicaService = inject(UlicaService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RasporedFormGroup = this.rasporedFormService.createRasporedFormGroup();

  compareGrad = (o1: IGrad | null, o2: IGrad | null): boolean => this.gradService.compareGrad(o1, o2);

  compareNaselje = (o1: INaselje | null, o2: INaselje | null): boolean => this.naseljeService.compareNaselje(o1, o2);

  compareUlica = (o1: IUlica | null, o2: IUlica | null): boolean => this.ulicaService.compareUlica(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raspored }) => {
      this.raspored = raspored;
      if (raspored) {
        this.updateForm(raspored);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const raspored = this.rasporedFormService.getRaspored(this.editForm);
    if (raspored.id !== null) {
      this.subscribeToSaveResponse(this.rasporedService.update(raspored));
    } else {
      this.subscribeToSaveResponse(this.rasporedService.create(raspored));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRaspored>>): void {
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

  protected updateForm(raspored: IRaspored): void {
    this.raspored = raspored;
    this.rasporedFormService.resetForm(this.editForm, raspored);

    this.gradsSharedCollection = this.gradService.addGradToCollectionIfMissing<IGrad>(this.gradsSharedCollection, raspored.grad);
    this.naseljesSharedCollection = this.naseljeService.addNaseljeToCollectionIfMissing<INaselje>(
      this.naseljesSharedCollection,
      raspored.naselje,
    );
    this.ulicasSharedCollection = this.ulicaService.addUlicaToCollectionIfMissing<IUlica>(this.ulicasSharedCollection, raspored.ulica);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, raspored.korisnikKreirao);
  }

  protected loadRelationshipsOptions(): void {
    this.gradService
      .query()
      .pipe(map((res: HttpResponse<IGrad[]>) => res.body ?? []))
      .pipe(map((grads: IGrad[]) => this.gradService.addGradToCollectionIfMissing<IGrad>(grads, this.raspored?.grad)))
      .subscribe((grads: IGrad[]) => (this.gradsSharedCollection = grads));

    this.naseljeService
      .query()
      .pipe(map((res: HttpResponse<INaselje[]>) => res.body ?? []))
      .pipe(map((naseljes: INaselje[]) => this.naseljeService.addNaseljeToCollectionIfMissing<INaselje>(naseljes, this.raspored?.naselje)))
      .subscribe((naseljes: INaselje[]) => (this.naseljesSharedCollection = naseljes));

    this.ulicaService
      .query()
      .pipe(map((res: HttpResponse<IUlica[]>) => res.body ?? []))
      .pipe(map((ulicas: IUlica[]) => this.ulicaService.addUlicaToCollectionIfMissing<IUlica>(ulicas, this.raspored?.ulica)))
      .subscribe((ulicas: IUlica[]) => (this.ulicasSharedCollection = ulicas));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.raspored?.korisnikKreirao)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
