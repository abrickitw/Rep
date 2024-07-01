import { IGrad, NewGrad } from './grad.model';

export const sampleWithRequiredData: IGrad = {
  id: 22546,
  gradNaziv: 'before upright',
};

export const sampleWithPartialData: IGrad = {
  id: 29167,
  gradNaziv: 'council mincemeat curtain',
};

export const sampleWithFullData: IGrad = {
  id: 27804,
  gradNaziv: 'violently considering',
};

export const sampleWithNewData: NewGrad = {
  gradNaziv: 'glower',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
