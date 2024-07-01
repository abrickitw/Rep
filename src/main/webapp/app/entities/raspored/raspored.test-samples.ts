import dayjs from 'dayjs/esm';

import { IRaspored, NewRaspored } from './raspored.model';

export const sampleWithRequiredData: IRaspored = {
  id: 24092,
  datumUsluge: dayjs('2024-07-01'),
};

export const sampleWithPartialData: IRaspored = {
  id: 28808,
  datumUsluge: dayjs('2024-07-01'),
};

export const sampleWithFullData: IRaspored = {
  id: 10791,
  datumUsluge: dayjs('2024-07-01'),
};

export const sampleWithNewData: NewRaspored = {
  datumUsluge: dayjs('2024-07-01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
