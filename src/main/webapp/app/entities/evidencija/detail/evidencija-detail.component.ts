import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IEvidencija } from '../evidencija.model';

@Component({
  standalone: true,
  selector: 'jhi-evidencija-detail',
  templateUrl: './evidencija-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EvidencijaDetailComponent {
  evidencija = input<IEvidencija | null>(null);

  previousState(): void {
    window.history.back();
  }
}
