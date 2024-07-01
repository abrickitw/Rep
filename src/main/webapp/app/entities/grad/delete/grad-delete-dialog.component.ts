import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGrad } from '../grad.model';
import { GradService } from '../service/grad.service';

@Component({
  standalone: true,
  templateUrl: './grad-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GradDeleteDialogComponent {
  grad?: IGrad;

  protected gradService = inject(GradService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gradService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
