import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRaspored } from '../raspored.model';

@Component({
  standalone: true,
  selector: 'jhi-raspored-detail',
  templateUrl: './raspored-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RasporedDetailComponent {
  raspored = input<IRaspored | null>(null);

  previousState(): void {
    window.history.back();
  }
}
