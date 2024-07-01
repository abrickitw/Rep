import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IGrad } from 'app/entities/grad/grad.model';
import { GradService } from 'app/entities/grad/service/grad.service';
import { NaseljeService } from '../service/naselje.service';
import { INaselje } from '../naselje.model';
import { NaseljeFormService } from './naselje-form.service';

import { NaseljeUpdateComponent } from './naselje-update.component';

describe('Naselje Management Update Component', () => {
  let comp: NaseljeUpdateComponent;
  let fixture: ComponentFixture<NaseljeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let naseljeFormService: NaseljeFormService;
  let naseljeService: NaseljeService;
  let gradService: GradService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NaseljeUpdateComponent],
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
      .overrideTemplate(NaseljeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NaseljeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    naseljeFormService = TestBed.inject(NaseljeFormService);
    naseljeService = TestBed.inject(NaseljeService);
    gradService = TestBed.inject(GradService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Grad query and add missing value', () => {
      const naselje: INaselje = { id: 456 };
      const grad: IGrad = { id: 23184 };
      naselje.grad = grad;

      const gradCollection: IGrad[] = [{ id: 1066 }];
      jest.spyOn(gradService, 'query').mockReturnValue(of(new HttpResponse({ body: gradCollection })));
      const additionalGrads = [grad];
      const expectedCollection: IGrad[] = [...additionalGrads, ...gradCollection];
      jest.spyOn(gradService, 'addGradToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ naselje });
      comp.ngOnInit();

      expect(gradService.query).toHaveBeenCalled();
      expect(gradService.addGradToCollectionIfMissing).toHaveBeenCalledWith(
        gradCollection,
        ...additionalGrads.map(expect.objectContaining),
      );
      expect(comp.gradsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const naselje: INaselje = { id: 456 };
      const grad: IGrad = { id: 5969 };
      naselje.grad = grad;

      activatedRoute.data = of({ naselje });
      comp.ngOnInit();

      expect(comp.gradsSharedCollection).toContain(grad);
      expect(comp.naselje).toEqual(naselje);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INaselje>>();
      const naselje = { id: 123 };
      jest.spyOn(naseljeFormService, 'getNaselje').mockReturnValue(naselje);
      jest.spyOn(naseljeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ naselje });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: naselje }));
      saveSubject.complete();

      // THEN
      expect(naseljeFormService.getNaselje).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(naseljeService.update).toHaveBeenCalledWith(expect.objectContaining(naselje));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INaselje>>();
      const naselje = { id: 123 };
      jest.spyOn(naseljeFormService, 'getNaselje').mockReturnValue({ id: null });
      jest.spyOn(naseljeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ naselje: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: naselje }));
      saveSubject.complete();

      // THEN
      expect(naseljeFormService.getNaselje).toHaveBeenCalled();
      expect(naseljeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INaselje>>();
      const naselje = { id: 123 };
      jest.spyOn(naseljeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ naselje });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(naseljeService.update).toHaveBeenCalled();
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
  });
});
