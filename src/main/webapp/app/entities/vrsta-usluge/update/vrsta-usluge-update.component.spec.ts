import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { VrstaUslugeService } from '../service/vrsta-usluge.service';
import { IVrstaUsluge } from '../vrsta-usluge.model';
import { VrstaUslugeFormService } from './vrsta-usluge-form.service';

import { VrstaUslugeUpdateComponent } from './vrsta-usluge-update.component';

describe('VrstaUsluge Management Update Component', () => {
  let comp: VrstaUslugeUpdateComponent;
  let fixture: ComponentFixture<VrstaUslugeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vrstaUslugeFormService: VrstaUslugeFormService;
  let vrstaUslugeService: VrstaUslugeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VrstaUslugeUpdateComponent],
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
      .overrideTemplate(VrstaUslugeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VrstaUslugeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vrstaUslugeFormService = TestBed.inject(VrstaUslugeFormService);
    vrstaUslugeService = TestBed.inject(VrstaUslugeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vrstaUsluge: IVrstaUsluge = { id: 456 };

      activatedRoute.data = of({ vrstaUsluge });
      comp.ngOnInit();

      expect(comp.vrstaUsluge).toEqual(vrstaUsluge);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVrstaUsluge>>();
      const vrstaUsluge = { id: 123 };
      jest.spyOn(vrstaUslugeFormService, 'getVrstaUsluge').mockReturnValue(vrstaUsluge);
      jest.spyOn(vrstaUslugeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vrstaUsluge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vrstaUsluge }));
      saveSubject.complete();

      // THEN
      expect(vrstaUslugeFormService.getVrstaUsluge).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vrstaUslugeService.update).toHaveBeenCalledWith(expect.objectContaining(vrstaUsluge));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVrstaUsluge>>();
      const vrstaUsluge = { id: 123 };
      jest.spyOn(vrstaUslugeFormService, 'getVrstaUsluge').mockReturnValue({ id: null });
      jest.spyOn(vrstaUslugeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vrstaUsluge: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vrstaUsluge }));
      saveSubject.complete();

      // THEN
      expect(vrstaUslugeFormService.getVrstaUsluge).toHaveBeenCalled();
      expect(vrstaUslugeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVrstaUsluge>>();
      const vrstaUsluge = { id: 123 };
      jest.spyOn(vrstaUslugeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vrstaUsluge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vrstaUslugeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
