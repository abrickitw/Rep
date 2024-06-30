import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '5abf612f-8c9c-4624-821f-0d183f714e0b',
};

export const sampleWithPartialData: IAuthority = {
  name: '31e06b9b-04ae-405b-a65d-5e77b49d36db',
};

export const sampleWithFullData: IAuthority = {
  name: 'c09d9456-e109-4f51-aede-136cb1fa36f6',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
