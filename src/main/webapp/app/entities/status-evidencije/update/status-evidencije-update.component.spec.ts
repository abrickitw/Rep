import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { StatusEvidencijeService } from '../service/status-evidencije.service';
import { IStatusEvidencije } from '../status-evidencije.model';
import { StatusEvidencijeFormService } from './status-evidencije-form.service';

import { StatusEvidencijeUpdateComponent } from './status-evidencije-update.component';

describe('StatusEvidencije Management Update Component', () => {
  let comp: StatusEvidencijeUpdateComponent;
  let fixture: ComponentFixture<StatusEvidencijeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let statusEvidencijeFormService: StatusEvidencijeFormService;
  let statusEvidencijeService: StatusEvidencijeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StatusEvidencijeUpdateComponent],
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
      .overrideTemplate(StatusEvidencijeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StatusEvidencijeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusEvidencijeFormService = TestBed.inject(StatusEvidencijeFormService);
    statusEvidencijeService = TestBed.inject(StatusEvidencijeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const statusEvidencije: IStatusEvidencije = { id: 456 };

      activatedRoute.data = of({ statusEvidencije });
      comp.ngOnInit();

      expect(comp.statusEvidencije).toEqual(statusEvidencije);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusEvidencije>>();
      const statusEvidencije = { id: 123 };
      jest.spyOn(statusEvidencijeFormService, 'getStatusEvidencije').mockReturnValue(statusEvidencije);
      jest.spyOn(statusEvidencijeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusEvidencije });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: statusEvidencije }));
      saveSubject.complete();

      // THEN
      expect(statusEvidencijeFormService.getStatusEvidencije).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusEvidencijeService.update).toHaveBeenCalledWith(expect.objectContaining(statusEvidencije));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusEvidencije>>();
      const statusEvidencije = { id: 123 };
      jest.spyOn(statusEvidencijeFormService, 'getStatusEvidencije').mockReturnValue({ id: null });
      jest.spyOn(statusEvidencijeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusEvidencije: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: statusEvidencije }));
      saveSubject.complete();

      // THEN
      expect(statusEvidencijeFormService.getStatusEvidencije).toHaveBeenCalled();
      expect(statusEvidencijeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusEvidencije>>();
      const statusEvidencije = { id: 123 };
      jest.spyOn(statusEvidencijeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusEvidencije });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusEvidencijeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
