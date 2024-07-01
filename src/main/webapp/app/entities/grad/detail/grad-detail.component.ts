import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IGrad } from '../grad.model';

@Component({
  standalone: true,
  selector: 'jhi-grad-detail',
  templateUrl: './grad-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class GradDetailComponent {
  grad = input<IGrad | null>(null);

  previousState(): void {
    window.history.back();
  }
}
