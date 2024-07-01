import { IVrstaUsluge, NewVrstaUsluge } from './vrsta-usluge.model';

export const sampleWithRequiredData: IVrstaUsluge = {
  id: 9131,
  vrstaUslugeNaziv: 'woefully phooey times',
};

export const sampleWithPartialData: IVrstaUsluge = {
  id: 21971,
  vrstaUslugeNaziv: 'pish revenge paper',
};

export const sampleWithFullData: IVrstaUsluge = {
  id: 18307,
  vrstaUslugeNaziv: 'illusion for physically',
};

export const sampleWithNewData: NewVrstaUsluge = {
  vrstaUslugeNaziv: 'aware psst',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
