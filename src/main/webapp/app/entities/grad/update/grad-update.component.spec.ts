import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { GradService } from '../service/grad.service';
import { IGrad } from '../grad.model';
import { GradFormService } from './grad-form.service';

import { GradUpdateComponent } from './grad-update.component';

describe('Grad Management Update Component', () => {
  let comp: GradUpdateComponent;
  let fixture: ComponentFixture<GradUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gradFormService: GradFormService;
  let gradService: GradService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GradUpdateComponent],
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
      .overrideTemplate(GradUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GradUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gradFormService = TestBed.inject(GradFormService);
    gradService = TestBed.inject(GradService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const grad: IGrad = { id: 456 };

      activatedRoute.data = of({ grad });
      comp.ngOnInit();

      expect(comp.grad).toEqual(grad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGrad>>();
      const grad = { id: 123 };
      jest.spyOn(gradFormService, 'getGrad').mockReturnValue(grad);
      jest.spyOn(gradService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: grad }));
      saveSubject.complete();

      // THEN
      expect(gradFormService.getGrad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gradService.update).toHaveBeenCalledWith(expect.objectContaining(grad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGrad>>();
      const grad = { id: 123 };
      jest.spyOn(gradFormService, 'getGrad').mockReturnValue({ id: null });
      jest.spyOn(gradService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: grad }));
      saveSubject.complete();

      // THEN
      expect(gradFormService.getGrad).toHaveBeenCalled();
      expect(gradService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGrad>>();
      const grad = { id: 123 };
      jest.spyOn(gradService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gradService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
