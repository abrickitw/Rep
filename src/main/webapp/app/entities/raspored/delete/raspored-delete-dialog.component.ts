import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRaspored } from '../raspored.model';
import { RasporedService } from '../service/raspored.service';

@Component({
  standalone: true,
  templateUrl: './raspored-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RasporedDeleteDialogComponent {
  raspored?: IRaspored;

  protected rasporedService = inject(RasporedService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rasporedService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
