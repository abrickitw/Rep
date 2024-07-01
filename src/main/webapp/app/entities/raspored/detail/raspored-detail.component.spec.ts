import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RasporedDetailComponent } from './raspored-detail.component';

describe('Raspored Management Detail Component', () => {
  let comp: RasporedDetailComponent;
  let fixture: ComponentFixture<RasporedDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RasporedDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RasporedDetailComponent,
              resolve: { raspored: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RasporedDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RasporedDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load raspored on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RasporedDetailComponent);

      // THEN
      expect(instance.raspored()).toEqual(expect.objectContaining({ id: 123 }));
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
