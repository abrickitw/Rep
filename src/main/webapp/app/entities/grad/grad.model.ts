export interface IGrad {
  id: number;
  gradNaziv?: string | null;
}

export type NewGrad = Omit<IGrad, 'id'> & { id: null };
