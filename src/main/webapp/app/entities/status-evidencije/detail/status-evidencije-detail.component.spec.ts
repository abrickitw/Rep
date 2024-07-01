import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StatusEvidencijeDetailComponent } from './status-evidencije-detail.component';

describe('StatusEvidencije Management Detail Component', () => {
  let comp: StatusEvidencijeDetailComponent;
  let fixture: ComponentFixture<StatusEvidencijeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatusEvidencijeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StatusEvidencijeDetailComponent,
              resolve: { statusEvidencije: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StatusEvidencijeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusEvidencijeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load statusEvidencije on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StatusEvidencijeDetailComponent);

      // THEN
      expect(instance.statusEvidencije()).toEqual(expect.objectContaining({ id: 123 }));
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
