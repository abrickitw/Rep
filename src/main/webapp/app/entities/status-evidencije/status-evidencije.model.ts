export interface IStatusEvidencije {
  id: number;
  statusEvidencijeNaziv?: string | null;
}

export type NewStatusEvidencije = Omit<IStatusEvidencije, 'id'> & { id: null };
