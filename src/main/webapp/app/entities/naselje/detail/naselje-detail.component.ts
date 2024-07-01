import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { INaselje } from '../naselje.model';

@Component({
  standalone: true,
  selector: 'jhi-naselje-detail',
  templateUrl: './naselje-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class NaseljeDetailComponent {
  naselje = input<INaselje | null>(null);

  previousState(): void {
    window.history.back();
  }
}
