import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EvidencijaDetailComponent } from './evidencija-detail.component';

describe('Evidencija Management Detail Component', () => {
  let comp: EvidencijaDetailComponent;
  let fixture: ComponentFixture<EvidencijaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvidencijaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EvidencijaDetailComponent,
              resolve: { evidencija: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EvidencijaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EvidencijaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load evidencija on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EvidencijaDetailComponent);

      // THEN
      expect(instance.evidencija()).toEqual(expect.objectContaining({ id: 123 }));
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
