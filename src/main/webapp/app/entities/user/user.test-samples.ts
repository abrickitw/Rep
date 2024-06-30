import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 28654,
  login: '.OVe',
};

export const sampleWithPartialData: IUser = {
  id: 18327,
  login: 's@WHTX\\\\DE',
};

export const sampleWithFullData: IUser = {
  id: 17947,
  login: 'mtM@hNb9sv',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
