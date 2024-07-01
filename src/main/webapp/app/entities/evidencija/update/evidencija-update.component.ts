import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IRaspored } from 'app/entities/raspored/raspored.model';
import { RasporedService } from 'app/entities/raspored/service/raspored.service';
import { IVrstaUsluge } from 'app/entities/vrsta-usluge/vrsta-usluge.model';
import { VrstaUslugeService } from 'app/entities/vrsta-usluge/service/vrsta-usluge.service';
import { IStatusEvidencije } from 'app/entities/status-evidencije/status-evidencije.model';
import { StatusEvidencijeService } from 'app/entities/status-evidencije/service/status-evidencije.service';
import { EvidencijaService } from '../service/evidencija.service';
import { IEvidencija } from '../evidencija.model';
import { EvidencijaFormService, EvidencijaFormGroup } from './evidencija-form.service';

@Component({
  standalone: true,
  selector: 'jhi-evidencija-update',
  templateUrl: './evidencija-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EvidencijaUpdateComponent implements OnInit {
  isSaving = false;
  evidencija: IEvidencija | null = null;

  usersSharedCollection: IUser[] = [];
  rasporedsSharedCollection: IRaspored[] = [];
  vrstaUslugesSharedCollection: IVrstaUsluge[] = [];
  statusEvidencijesSharedCollection: IStatusEvidencije[] = [];

  protected evidencijaService = inject(EvidencijaService);
  protected evidencijaFormService = inject(EvidencijaFormService);
  protected userService = inject(UserService);
  protected rasporedService = inject(RasporedService);
  protected vrstaUslugeService = inject(VrstaUslugeService);
  protected statusEvidencijeService = inject(StatusEvidencijeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EvidencijaFormGroup = this.evidencijaFormService.createEvidencijaFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareRaspored = (o1: IRaspored | null, o2: IRaspored | null): boolean => this.rasporedService.compareRaspored(o1, o2);

  compareVrstaUsluge = (o1: IVrstaUsluge | null, o2: IVrstaUsluge | null): boolean => this.vrstaUslugeService.compareVrstaUsluge(o1, o2);

  compareStatusEvidencije = (o1: IStatusEvidencije | null, o2: IStatusEvidencije | null): boolean =>
    this.statusEvidencijeService.compareStatusEvidencije(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evidencija }) => {
      this.evidencija = evidencija;
      if (evidencija) {
        this.updateForm(evidencija);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evidencija = this.evidencijaFormService.getEvidencija(this.editForm);
    if (evidencija.id !== null) {
      this.subscribeToSaveResponse(this.evidencijaService.update(evidencija));
    } else {
      this.subscribeToSaveResponse(this.evidencijaService.create(evidencija));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvidencija>>): void {
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

  protected updateForm(evidencija: IEvidencija): void {
    this.evidencija = evidencija;
    this.evidencijaFormService.resetForm(this.editForm, evidencija);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      evidencija.korisnikIzvrsio,
      evidencija.korisnikIspravio,
    );
    this.rasporedsSharedCollection = this.rasporedService.addRasporedToCollectionIfMissing<IRaspored>(
      this.rasporedsSharedCollection,
      evidencija.raspored,
    );
    this.vrstaUslugesSharedCollection = this.vrstaUslugeService.addVrstaUslugeToCollectionIfMissing<IVrstaUsluge>(
      this.vrstaUslugesSharedCollection,
      evidencija.vrstaUsluge,
    );
    this.statusEvidencijesSharedCollection = this.statusEvidencijeService.addStatusEvidencijeToCollectionIfMissing<IStatusEvidencije>(
      this.statusEvidencijesSharedCollection,
      evidencija.statusEvidencije,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.evidencija?.korisnikIzvrsio, this.evidencija?.korisnikIspravio),
        ),
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.rasporedService
      .query()
      .pipe(map((res: HttpResponse<IRaspored[]>) => res.body ?? []))
      .pipe(
        map((rasporeds: IRaspored[]) =>
          this.rasporedService.addRasporedToCollectionIfMissing<IRaspored>(rasporeds, this.evidencija?.raspored),
        ),
      )
      .subscribe((rasporeds: IRaspored[]) => (this.rasporedsSharedCollection = rasporeds));

    this.vrstaUslugeService
      .query()
      .pipe(map((res: HttpResponse<IVrstaUsluge[]>) => res.body ?? []))
      .pipe(
        map((vrstaUsluges: IVrstaUsluge[]) =>
          this.vrstaUslugeService.addVrstaUslugeToCollectionIfMissing<IVrstaUsluge>(vrstaUsluges, this.evidencija?.vrstaUsluge),
        ),
      )
      .subscribe((vrstaUsluges: IVrstaUsluge[]) => (this.vrstaUslugesSharedCollection = vrstaUsluges));

    this.statusEvidencijeService
      .query()
      .pipe(map((res: HttpResponse<IStatusEvidencije[]>) => res.body ?? []))
      .pipe(
        map((statusEvidencijes: IStatusEvidencije[]) =>
          this.statusEvidencijeService.addStatusEvidencijeToCollectionIfMissing<IStatusEvidencije>(
            statusEvidencijes,
            this.evidencija?.statusEvidencije,
          ),
        ),
      )
      .subscribe((statusEvidencijes: IStatusEvidencije[]) => (this.statusEvidencijesSharedCollection = statusEvidencijes));
  }
}
