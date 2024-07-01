import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IRaspored } from 'app/entities/raspored/raspored.model';
import { RasporedService } from 'app/entities/raspored/service/raspored.service';
import { IVrstaUsluge } from 'app/entities/vrsta-usluge/vrsta-usluge.model';
import { VrstaUslugeService } from 'app/entities/vrsta-usluge/service/vrsta-usluge.service';
import { IStatusEvidencije } from 'app/entities/status-evidencije/status-evidencije.model';
import { StatusEvidencijeService } from 'app/entities/status-evidencije/service/status-evidencije.service';
import { IEvidencija } from '../evidencija.model';
import { EvidencijaService } from '../service/evidencija.service';
import { EvidencijaFormService } from './evidencija-form.service';

import { EvidencijaUpdateComponent } from './evidencija-update.component';

describe('Evidencija Management Update Component', () => {
  let comp: EvidencijaUpdateComponent;
  let fixture: ComponentFixture<EvidencijaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evidencijaFormService: EvidencijaFormService;
  let evidencijaService: EvidencijaService;
  let userService: UserService;
  let rasporedService: RasporedService;
  let vrstaUslugeService: VrstaUslugeService;
  let statusEvidencijeService: StatusEvidencijeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EvidencijaUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EvidencijaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvidencijaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evidencijaFormService = TestBed.inject(EvidencijaFormService);
    evidencijaService = TestBed.inject(EvidencijaService);
    userService = TestBed.inject(UserService);
    rasporedService = TestBed.inject(RasporedService);
    vrstaUslugeService = TestBed.inject(VrstaUslugeService);
    statusEvidencijeService = TestBed.inject(StatusEvidencijeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const evidencija: IEvidencija = { id: 456 };
      const korisnikIzvrsio: IUser = { id: 16457 };
      evidencija.korisnikIzvrsio = korisnikIzvrsio;
      const korisnikIspravio: IUser = { id: 19661 };
      evidencija.korisnikIspravio = korisnikIspravio;

      const userCollection: IUser[] = [{ id: 21012 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [korisnikIzvrsio, korisnikIspravio];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evidencija });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Raspored query and add missing value', () => {
      const evidencija: IEvidencija = { id: 456 };
      const raspored: IRaspored = { id: 24077 };
      evidencija.raspored = raspored;

      const rasporedCollection: IRaspored[] = [{ id: 31828 }];
      jest.spyOn(rasporedService, 'query').mockReturnValue(of(new HttpResponse({ body: rasporedCollection })));
      const additionalRasporeds = [raspored];
      const expectedCollection: IRaspored[] = [...additionalRasporeds, ...rasporedCollection];
      jest.spyOn(rasporedService, 'addRasporedToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evidencija });
      comp.ngOnInit();

      expect(rasporedService.query).toHaveBeenCalled();
      expect(rasporedService.addRasporedToCollectionIfMissing).toHaveBeenCalledWith(
        rasporedCollection,
        ...additionalRasporeds.map(expect.objectContaining),
      );
      expect(comp.rasporedsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call VrstaUsluge query and add missing value', () => {
      const evidencija: IEvidencija = { id: 456 };
      const vrstaUsluge: IVrstaUsluge = { id: 9702 };
      evidencija.vrstaUsluge = vrstaUsluge;

      const vrstaUslugeCollection: IVrstaUsluge[] = [{ id: 7737 }];
      jest.spyOn(vrstaUslugeService, 'query').mockReturnValue(of(new HttpResponse({ body: vrstaUslugeCollection })));
      const additionalVrstaUsluges = [vrstaUsluge];
      const expectedCollection: IVrstaUsluge[] = [...additionalVrstaUsluges, ...vrstaUslugeCollection];
      jest.spyOn(vrstaUslugeService, 'addVrstaUslugeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evidencija });
      comp.ngOnInit();

      expect(vrstaUslugeService.query).toHaveBeenCalled();
      expect(vrstaUslugeService.addVrstaUslugeToCollectionIfMissing).toHaveBeenCalledWith(
        vrstaUslugeCollection,
        ...additionalVrstaUsluges.map(expect.objectContaining),
      );
      expect(comp.vrstaUslugesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call StatusEvidencije query and add missing value', () => {
      const evidencija: IEvidencija = { id: 456 };
      const statusEvidencije: IStatusEvidencije = { id: 25599 };
      evidencija.statusEvidencije = statusEvidencije;

      const statusEvidencijeCollection: IStatusEvidencije[] = [{ id: 13890 }];
      jest.spyOn(statusEvidencijeService, 'query').mockReturnValue(of(new HttpResponse({ body: statusEvidencijeCollection })));
      const additionalStatusEvidencijes = [statusEvidencije];
      const expectedCollection: IStatusEvidencije[] = [...additionalStatusEvidencijes, ...statusEvidencijeCollection];
      jest.spyOn(statusEvidencijeService, 'addStatusEvidencijeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evidencija });
      comp.ngOnInit();

      expect(statusEvidencijeService.query).toHaveBeenCalled();
      expect(statusEvidencijeService.addStatusEvidencijeToCollectionIfMissing).toHaveBeenCalledWith(
        statusEvidencijeCollection,
        ...additionalStatusEvidencijes.map(expect.objectContaining),
      );
      expect(comp.statusEvidencijesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evidencija: IEvidencija = { id: 456 };
      const korisnikIzvrsio: IUser = { id: 17874 };
      evidencija.korisnikIzvrsio = korisnikIzvrsio;
      const korisnikIspravio: IUser = { id: 22696 };
      evidencija.korisnikIspravio = korisnikIspravio;
      const raspored: IRaspored = { id: 15694 };
      evidencija.raspored = raspored;
      const vrstaUsluge: IVrstaUsluge = { id: 15225 };
      evidencija.vrstaUsluge = vrstaUsluge;
      const statusEvidencije: IStatusEvidencije = { id: 7897 };
      evidencija.statusEvidencije = statusEvidencije;

      activatedRoute.data = of({ evidencija });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(korisnikIzvrsio);
      expect(comp.usersSharedCollection).toContain(korisnikIspravio);
      expect(comp.rasporedsSharedCollection).toContain(raspored);
      expect(comp.vrstaUslugesSharedCollection).toContain(vrstaUsluge);
      expect(comp.statusEvidencijesSharedCollection).toContain(statusEvidencije);
      expect(comp.evidencija).toEqual(evidencija);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvidencija>>();
      const evidencija = { id: 123 };
      jest.spyOn(evidencijaFormService, 'getEvidencija').mockReturnValue(evidencija);
      jest.spyOn(evidencijaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evidencija });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evidencija }));
      saveSubject.complete();

      // THEN
      expect(evidencijaFormService.getEvidencija).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evidencijaService.update).toHaveBeenCalledWith(expect.objectContaining(evidencija));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvidencija>>();
      const evidencija = { id: 123 };
      jest.spyOn(evidencijaFormService, 'getEvidencija').mockReturnValue({ id: null });
      jest.spyOn(evidencijaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evidencija: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evidencija }));
      saveSubject.complete();

      // THEN
      expect(evidencijaFormService.getEvidencija).toHaveBeenCalled();
      expect(evidencijaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvidencija>>();
      const evidencija = { id: 123 };
      jest.spyOn(evidencijaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evidencija });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evidencijaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRaspored', () => {
      it('Should forward to rasporedService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(rasporedService, 'compareRaspored');
        comp.compareRaspored(entity, entity2);
        expect(rasporedService.compareRaspored).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVrstaUsluge', () => {
      it('Should forward to vrstaUslugeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vrstaUslugeService, 'compareVrstaUsluge');
        comp.compareVrstaUsluge(entity, entity2);
        expect(vrstaUslugeService.compareVrstaUsluge).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStatusEvidencije', () => {
      it('Should forward to statusEvidencijeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(statusEvidencijeService, 'compareStatusEvidencije');
        comp.compareStatusEvidencije(entity, entity2);
        expect(statusEvidencijeService.compareStatusEvidencije).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
