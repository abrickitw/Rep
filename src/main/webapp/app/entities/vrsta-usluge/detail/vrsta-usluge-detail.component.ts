import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IVrstaUsluge } from '../vrsta-usluge.model';

@Component({
  standalone: true,
  selector: 'jhi-vrsta-usluge-detail',
  templateUrl: './vrsta-usluge-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VrstaUslugeDetailComponent {
  vrstaUsluge = input<IVrstaUsluge | null>(null);

  previousState(): void {
    window.history.back();
  }
}
