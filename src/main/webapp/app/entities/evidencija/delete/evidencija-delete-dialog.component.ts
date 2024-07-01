import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEvidencija } from '../evidencija.model';
import { EvidencijaService } from '../service/evidencija.service';

@Component({
  standalone: true,
  templateUrl: './evidencija-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EvidencijaDeleteDialogComponent {
  evidencija?: IEvidencija;

  protected evidencijaService = inject(EvidencijaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evidencijaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
