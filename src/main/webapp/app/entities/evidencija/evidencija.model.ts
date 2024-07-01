import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IRaspored } from 'app/entities/raspored/raspored.model';
import { IVrstaUsluge } from 'app/entities/vrsta-usluge/vrsta-usluge.model';
import { IStatusEvidencije } from 'app/entities/status-evidencije/status-evidencije.model';

export interface IEvidencija {
  id: number;
  nazivEvidencija?: string | null;
  vrijemeUsluge?: string | null;
  komentar?: string | null;
  imeStanara?: string | null;
  prezimeStanara?: string | null;
  kontaktStanara?: string | null;
  datumIspravka?: dayjs.Dayjs | null;
  komentarIspravka?: string | null;
  kucniBroj?: string | null;
  korisnikIzvrsio?: Pick<IUser, 'id' | 'login'> | null;
  korisnikIspravio?: Pick<IUser, 'id' | 'login'> | null;
  raspored?: Pick<IRaspored, 'id' | 'datumUsluge'> | null;
  vrstaUsluge?: Pick<IVrstaUsluge, 'id' | 'vrstaUslugeNaziv'> | null;
  statusEvidencije?: Pick<IStatusEvidencije, 'id' | 'statusEvidencijeNaziv'> | null;
}

export type NewEvidencija = Omit<IEvidencija, 'id'> & { id: null };
