import { IUlica, NewUlica } from './ulica.model';

export const sampleWithRequiredData: IUlica = {
  id: 8935,
  ulicaNaziv: 'vivacious bonfire but',
};

export const sampleWithPartialData: IUlica = {
  id: 11110,
  ulicaNaziv: 'waltz astride',
};

export const sampleWithFullData: IUlica = {
  id: 1292,
  ulicaNaziv: 'besides tow-truck till',
};

export const sampleWithNewData: NewUlica = {
  ulicaNaziv: 'solemnly soon yahoo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
