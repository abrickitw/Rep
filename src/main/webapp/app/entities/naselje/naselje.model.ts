import { IGrad } from 'app/entities/grad/grad.model';

export interface INaselje {
  id: number;
  naseljeNaziv?: string | null;
  grad?: Pick<IGrad, 'id' | 'gradNaziv'> | null;
}

export type NewNaselje = Omit<INaselje, 'id'> & { id: null };
