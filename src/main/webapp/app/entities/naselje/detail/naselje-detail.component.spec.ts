import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { NaseljeDetailComponent } from './naselje-detail.component';

describe('Naselje Management Detail Component', () => {
  let comp: NaseljeDetailComponent;
  let fixture: ComponentFixture<NaseljeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NaseljeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: NaseljeDetailComponent,
              resolve: { naselje: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(NaseljeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NaseljeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load naselje on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', NaseljeDetailComponent);

      // THEN
      expect(instance.naselje()).toEqual(expect.objectContaining({ id: 123 }));
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
