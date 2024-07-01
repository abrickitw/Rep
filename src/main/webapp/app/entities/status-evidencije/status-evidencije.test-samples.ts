import { IStatusEvidencije, NewStatusEvidencije } from './status-evidencije.model';

export const sampleWithRequiredData: IStatusEvidencije = {
  id: 30272,
  statusEvidencijeNaziv: 'quarrelsomely realistic',
};

export const sampleWithPartialData: IStatusEvidencije = {
  id: 26456,
  statusEvidencijeNaziv: 'radiant wetly',
};

export const sampleWithFullData: IStatusEvidencije = {
  id: 12855,
  statusEvidencijeNaziv: 'jaded bless',
};

export const sampleWithNewData: NewStatusEvidencije = {
  statusEvidencijeNaziv: 'green',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
