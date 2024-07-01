import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IGrad } from 'app/entities/grad/grad.model';
import { GradService } from 'app/entities/grad/service/grad.service';
import { INaselje } from 'app/entities/naselje/naselje.model';
import { NaseljeService } from 'app/entities/naselje/service/naselje.service';
import { IUlica } from '../ulica.model';
import { UlicaService } from '../service/ulica.service';
import { UlicaFormService } from './ulica-form.service';

import { UlicaUpdateComponent } from './ulica-update.component';

describe('Ulica Management Update Component', () => {
  let comp: UlicaUpdateComponent;
  let fixture: ComponentFixture<UlicaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ulicaFormService: UlicaFormService;
  let ulicaService: UlicaService;
  let gradService: GradService;
  let naseljeService: NaseljeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UlicaUpdateComponent],
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
      .overrideTemplate(UlicaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UlicaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ulicaFormService = TestBed.inject(UlicaFormService);
    ulicaService = TestBed.inject(UlicaService);
    gradService = TestBed.inject(GradService);
    naseljeService = TestBed.inject(NaseljeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Grad query and add missing value', () => {
      const ulica: IUlica = { id: 456 };
      const grad: IGrad = { id: 21046 };
      ulica.grad = grad;

      const gradCollection: IGrad[] = [{ id: 11517 }];
      jest.spyOn(gradService, 'query').mockReturnValue(of(new HttpResponse({ body: gradCollection })));
      const additionalGrads = [grad];
      const expectedCollection: IGrad[] = [...additionalGrads, ...gradCollection];
      jest.spyOn(gradService, 'addGradToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ulica });
      comp.ngOnInit();

      expect(gradService.query).toHaveBeenCalled();
      expect(gradService.addGradToCollectionIfMissing).toHaveBeenCalledWith(
        gradCollection,
        ...additionalGrads.map(expect.objectContaining),
      );
      expect(comp.gradsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Naselje query and add missing value', () => {
      const ulica: IUlica = { id: 456 };
      const naselje: INaselje = { id: 20852 };
      ulica.naselje = naselje;

      const naseljeCollection: INaselje[] = [{ id: 25744 }];
      jest.spyOn(naseljeService, 'query').mockReturnValue(of(new HttpResponse({ body: naseljeCollection })));
      const additionalNaseljes = [naselje];
      const expectedCollection: INaselje[] = [...additionalNaseljes, ...naseljeCollection];
      jest.spyOn(naseljeService, 'addNaseljeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ulica });
      comp.ngOnInit();

      expect(naseljeService.query).toHaveBeenCalled();
      expect(naseljeService.addNaseljeToCollectionIfMissing).toHaveBeenCalledWith(
        naseljeCollection,
        ...additionalNaseljes.map(expect.objectContaining),
      );
      expect(comp.naseljesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ulica: IUlica = { id: 456 };
      const grad: IGrad = { id: 12597 };
      ulica.grad = grad;
      const naselje: INaselje = { id: 20232 };
      ulica.naselje = naselje;

      activatedRoute.data = of({ ulica });
      comp.ngOnInit();

      expect(comp.gradsSharedCollection).toContain(grad);
      expect(comp.naseljesSharedCollection).toContain(naselje);
      expect(comp.ulica).toEqual(ulica);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUlica>>();
      const ulica = { id: 123 };
      jest.spyOn(ulicaFormService, 'getUlica').mockReturnValue(ulica);
      jest.spyOn(ulicaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ulica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ulica }));
      saveSubject.complete();

      // THEN
      expect(ulicaFormService.getUlica).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ulicaService.update).toHaveBeenCalledWith(expect.objectContaining(ulica));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUlica>>();
      const ulica = { id: 123 };
      jest.spyOn(ulicaFormService, 'getUlica').mockReturnValue({ id: null });
      jest.spyOn(ulicaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ulica: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ulica }));
      saveSubject.complete();

      // THEN
      expect(ulicaFormService.getUlica).toHaveBeenCalled();
      expect(ulicaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUlica>>();
      const ulica = { id: 123 };
      jest.spyOn(ulicaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ulica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ulicaService.update).toHaveBeenCalled();
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
  });
});
