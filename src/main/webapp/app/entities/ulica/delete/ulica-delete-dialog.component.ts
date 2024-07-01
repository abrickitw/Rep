import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUlica } from '../ulica.model';
import { UlicaService } from '../service/ulica.service';

@Component({
  standalone: true,
  templateUrl: './ulica-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UlicaDeleteDialogComponent {
  ulica?: IUlica;

  protected ulicaService = inject(UlicaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ulicaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
