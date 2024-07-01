import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUlica } from '../ulica.model';

@Component({
  standalone: true,
  selector: 'jhi-ulica-detail',
  templateUrl: './ulica-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UlicaDetailComponent {
  ulica = input<IUlica | null>(null);

  previousState(): void {
    window.history.back();
  }
}
