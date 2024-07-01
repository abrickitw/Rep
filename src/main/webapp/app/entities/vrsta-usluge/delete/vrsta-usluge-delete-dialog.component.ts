import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVrstaUsluge } from '../vrsta-usluge.model';
import { VrstaUslugeService } from '../service/vrsta-usluge.service';

@Component({
  standalone: true,
  templateUrl: './vrsta-usluge-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VrstaUslugeDeleteDialogComponent {
  vrstaUsluge?: IVrstaUsluge;

  protected vrstaUslugeService = inject(VrstaUslugeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vrstaUslugeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
