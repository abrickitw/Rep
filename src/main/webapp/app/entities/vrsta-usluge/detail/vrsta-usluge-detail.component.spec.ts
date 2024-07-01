import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VrstaUslugeDetailComponent } from './vrsta-usluge-detail.component';

describe('VrstaUsluge Management Detail Component', () => {
  let comp: VrstaUslugeDetailComponent;
  let fixture: ComponentFixture<VrstaUslugeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VrstaUslugeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: VrstaUslugeDetailComponent,
              resolve: { vrstaUsluge: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VrstaUslugeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VrstaUslugeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vrstaUsluge on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VrstaUslugeDetailComponent);

      // THEN
      expect(instance.vrstaUsluge()).toEqual(expect.objectContaining({ id: 123 }));
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
