import dayjs from 'dayjs/esm';

import { IEvidencija, NewEvidencija } from './evidencija.model';

export const sampleWithRequiredData: IEvidencija = {
  id: 16619,
  nazivEvidencija: 'behind how who',
  vrijemeUsluge: 'yippee beside',
  komentar: 'ha why',
  imeStanara: 'rehabilitate',
  prezimeStanara: 'cautiously meanwhile',
  kontaktStanara: 'whirlwind',
};

export const sampleWithPartialData: IEvidencija = {
  id: 5523,
  nazivEvidencija: 'eek worried clumsy',
  vrijemeUsluge: 'ugh selfishly',
  komentar: 'elapse oh',
  imeStanara: 'circa out',
  prezimeStanara: 'yawning overweight compromise',
  kontaktStanara: 'unused pitiful',
};

export const sampleWithFullData: IEvidencija = {
  id: 2397,
  nazivEvidencija: 'engine after righteously',
  vrijemeUsluge: 'thoughtfully huzzah bop',
  komentar: 'intercut exchange because',
  imeStanara: 'per',
  prezimeStanara: 'barring heckle',
  kontaktStanara: 'the hmph',
  datumIspravka: dayjs('2024-07-01'),
  komentarIspravka: 'oof vainly peacock',
  kucniBroj: 'ugh gadzooks',
};

export const sampleWithNewData: NewEvidencija = {
  nazivEvidencija: 'prostanoid hmph',
  vrijemeUsluge: 'ack now toll',
  komentar: 'wherever transfer',
  imeStanara: 'fooey',
  prezimeStanara: 'oval eternity conscientise',
  kontaktStanara: 'how resolve lively',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
