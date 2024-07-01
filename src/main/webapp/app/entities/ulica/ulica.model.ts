import { IGrad } from 'app/entities/grad/grad.model';
import { INaselje } from 'app/entities/naselje/naselje.model';

export interface IUlica {
  id: number;
  ulicaNaziv?: string | null;
  grad?: Pick<IGrad, 'id' | 'gradNaziv'> | null;
  naselje?: Pick<INaselje, 'id' | 'naseljeNaziv'> | null;
}

export type NewUlica = Omit<IUlica, 'id'> & { id: null };
