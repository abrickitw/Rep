import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UlicaDetailComponent } from './ulica-detail.component';

describe('Ulica Management Detail Component', () => {
  let comp: UlicaDetailComponent;
  let fixture: ComponentFixture<UlicaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UlicaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UlicaDetailComponent,
              resolve: { ulica: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UlicaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UlicaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ulica on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UlicaDetailComponent);

      // THEN
      expect(instance.ulica()).toEqual(expect.objectContaining({ id: 123 }));
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
