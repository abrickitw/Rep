import dayjs from 'dayjs/esm';
import { IGrad } from 'app/entities/grad/grad.model';
import { INaselje } from 'app/entities/naselje/naselje.model';
import { IUlica } from 'app/entities/ulica/ulica.model';
import { IUser } from 'app/entities/user/user.model';

export interface IRaspored {
  id: number;
  datumUsluge?: dayjs.Dayjs | null;
  grad?: Pick<IGrad, 'id' | 'gradNaziv'> | null;
  naselje?: Pick<INaselje, 'id' | 'naseljeNaziv'> | null;
  ulica?: Pick<IUlica, 'id' | 'ulicaNaziv'> | null;
  korisnikKreirao?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewRaspored = Omit<IRaspored, 'id'> & { id: null };
