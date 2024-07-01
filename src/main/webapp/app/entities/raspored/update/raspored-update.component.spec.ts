import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IGrad } from 'app/entities/grad/grad.model';
import { GradService } from 'app/entities/grad/service/grad.service';
import { INaselje } from 'app/entities/naselje/naselje.model';
import { NaseljeService } from 'app/entities/naselje/service/naselje.service';
import { IUlica } from 'app/entities/ulica/ulica.model';
import { UlicaService } from 'app/entities/ulica/service/ulica.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IRaspored } from '../raspored.model';
import { RasporedService } from '../service/raspored.service';
import { RasporedFormService } from './raspored-form.service';

import { RasporedUpdateComponent } from './raspored-update.component';

describe('Raspored Management Update Component', () => {
  let comp: RasporedUpdateComponent;
  let fixture: ComponentFixture<RasporedUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rasporedFormService: RasporedFormService;
  let rasporedService: RasporedService;
  let gradService: GradService;
  let naseljeService: NaseljeService;
  let ulicaService: UlicaService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RasporedUpdateComponent],
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
      .overrideTemplate(RasporedUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RasporedUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rasporedFormService = TestBed.inject(RasporedFormService);
    rasporedService = TestBed.inject(RasporedService);
    gradService = TestBed.inject(GradService);
    naseljeService = TestBed.inject(NaseljeService);
    ulicaService = TestBed.inject(UlicaService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Grad query and add missing value', () => {
      const raspored: IRaspored = { id: 456 };
      const grad: IGrad = { id: 12176 };
      raspored.grad = grad;

      const gradCollection: IGrad[] = [{ id: 20618 }];
      jest.spyOn(gradService, 'query').mockReturnValue(of(new HttpResponse({ body: gradCollection })));
      const additionalGrads = [grad];
      const expectedCollection: IGrad[] = [...additionalGrads, ...gradCollection];
      jest.spyOn(gradService, 'addGradToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ raspored });
      comp.ngOnInit();

      expect(gradService.query).toHaveBeenCalled();
      expect(gradService.addGradToCollectionIfMissing).toHaveBeenCalledWith(
        gradCollection,
        ...additionalGrads.map(expect.objectContaining),
      );
      expect(comp.gradsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Naselje query and add missing value', () => {
      const raspored: IRaspored = { id: 456 };
      const naselje: INaselje = { id: 4832 };
      raspored.naselje = naselje;

      const naseljeCollection: INaselje[] = [{ id: 27289 }];
      jest.spyOn(naseljeService, 'query').mockReturnValue(of(new HttpResponse({ body: naseljeCollection })));
      const additionalNaseljes = [naselje];
      const expectedCollection: INaselje[] = [...additionalNaseljes, ...naseljeCollection];
      jest.spyOn(naseljeService, 'addNaseljeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ raspored });
      comp.ngOnInit();

      expect(naseljeService.query).toHaveBeenCalled();
      expect(naseljeService.addNaseljeToCollectionIfMissing).toHaveBeenCalledWith(
        naseljeCollection,
        ...additionalNaseljes.map(expect.objectContaining),
      );
      expect(comp.naseljesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ulica query and add missing value', () => {
      const raspored: IRaspored = { id: 456 };
      const ulica: IUlica = { id: 7938 };
      raspored.ulica = ulica;

      const ulicaCollection: IUlica[] = [{ id: 4749 }];
      jest.spyOn(ulicaService, 'query').mockReturnValue(of(new HttpResponse({ body: ulicaCollection })));
      const additionalUlicas = [ulica];
      const expectedCollection: IUlica[] = [...additionalUlicas, ...ulicaCollection];
      jest.spyOn(ulicaService, 'addUlicaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ raspored });
      comp.ngOnInit();

      expect(ulicaService.query).toHaveBeenCalled();
      expect(ulicaService.addUlicaToCollectionIfMissing).toHaveBeenCalledWith(
        ulicaCollection,
        ...additionalUlicas.map(expect.objectContaining),
      );
      expect(comp.ulicasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const raspored: IRaspored = { id: 456 };
      const korisnikKreirao: IUser = { id: 14317 };
      raspored.korisnikKreirao = korisnikKreirao;

      const userCollection: IUser[] = [{ id: 14267 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [korisnikKreirao];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ raspored });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const raspored: IRaspored = { id: 456 };
      const grad: IGrad = { id: 27948 };
      raspored.grad = grad;
      const naselje: INaselje = { id: 18991 };
      raspored.naselje = naselje;
      const ulica: IUlica = { id: 9488 };
      raspored.ulica = ulica;
      const korisnikKreirao: IUser = { id: 5090 };
      raspored.korisnikKreirao = korisnikKreirao;

      activatedRoute.data = of({ raspored });
      comp.ngOnInit();

      expect(comp.gradsSharedCollection).toContain(grad);
      expect(comp.naseljesSharedCollection).toContain(naselje);
      expect(comp.ulicasSharedCollection).toContain(ulica);
      expect(comp.usersSharedCollection).toContain(korisnikKreirao);
      expect(comp.raspored).toEqual(raspored);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRaspored>>();
      const raspored = { id: 123 };
      jest.spyOn(rasporedFormService, 'getRaspored').mockReturnValue(raspored);
      jest.spyOn(rasporedService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raspored });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raspored }));
      saveSubject.complete();

      // THEN
      expect(rasporedFormService.getRaspored).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rasporedService.update).toHaveBeenCalledWith(expect.objectContaining(raspored));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRaspored>>();
      const raspored = { id: 123 };
      jest.spyOn(rasporedFormService, 'getRaspored').mockReturnValue({ id: null });
      jest.spyOn(rasporedService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raspored: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raspored }));
      saveSubject.complete();

      // THEN
      expect(rasporedFormService.getRaspored).toHaveBeenCalled();
      expect(rasporedService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRaspored>>();
      const raspored = { id: 123 };
      jest.spyOn(rasporedService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raspored });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rasporedService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGrad', () => {
      it('Should forward to gradService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gradService, 'compareGrad');
        comp.compareGrad(entity, entity2);
        expect(gradService.compareGrad).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareNaselje', () => {
      it('Should forward to naseljeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(naseljeService, 'compareNaselje');
        comp.compareNaselje(entity, entity2);
        expect(naseljeService.compareNaselje).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUlica', () => {
      it('Should forward to ulicaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ulicaService, 'compareUlica');
        comp.compareUlica(entity, entity2);
        expect(ulicaService.compareUlica).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
