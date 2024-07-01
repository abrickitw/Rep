import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStatusEvidencije } from '../status-evidencije.model';
import { StatusEvidencijeService } from '../service/status-evidencije.service';

@Component({
  standalone: true,
  templateUrl: './status-evidencije-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StatusEvidencijeDeleteDialogComponent {
  statusEvidencije?: IStatusEvidencije;

  protected statusEvidencijeService = inject(StatusEvidencijeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.statusEvidencijeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
