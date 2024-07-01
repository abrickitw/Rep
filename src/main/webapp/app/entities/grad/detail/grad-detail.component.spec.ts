import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { GradDetailComponent } from './grad-detail.component';

describe('Grad Management Detail Component', () => {
  let comp: GradDetailComponent;
  let fixture: ComponentFixture<GradDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GradDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: GradDetailComponent,
              resolve: { grad: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GradDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GradDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load grad on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GradDetailComponent);

      // THEN
      expect(instance.grad()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
