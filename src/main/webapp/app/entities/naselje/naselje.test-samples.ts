import { INaselje, NewNaselje } from './naselje.model';

export const sampleWithRequiredData: INaselje = {
  id: 1972,
  naseljeNaziv: 'aha minus',
};

export const sampleWithPartialData: INaselje = {
  id: 27832,
  naseljeNaziv: 'uh-huh fooey',
};

export const sampleWithFullData: INaselje = {
  id: 10770,
  naseljeNaziv: 'outstanding daintily yowza',
};

export const sampleWithNewData: NewNaselje = {
  naseljeNaziv: 'stunning excepting oof',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
