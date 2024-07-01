export interface IVrstaUsluge {
  id: number;
  vrstaUslugeNaziv?: string | null;
}

export type NewVrstaUsluge = Omit<IVrstaUsluge, 'id'> & { id: null };
