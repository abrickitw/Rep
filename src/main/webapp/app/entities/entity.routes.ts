import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'kaminApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'evidencija',
    data: { pageTitle: 'kaminApp.evidencija.home.title' },
    loadChildren: () => import('./evidencija/evidencija.routes'),
  },
  {
    path: 'raspored',
    data: { pageTitle: 'kaminApp.raspored.home.title' },
    loadChildren: () => import('./raspored/raspored.routes'),
  },
  {
    path: 'grad',
    data: { pageTitle: 'kaminApp.grad.home.title' },
    loadChildren: () => import('./grad/grad.routes'),
  },
  {
    path: 'ulica',
    data: { pageTitle: 'kaminApp.ulica.home.title' },
    loadChildren: () => import('./ulica/ulica.routes'),
  },
  {
    path: 'naselje',
    data: { pageTitle: 'kaminApp.naselje.home.title' },
    loadChildren: () => import('./naselje/naselje.routes'),
  },
  {
    path: 'vrsta-usluge',
    data: { pageTitle: 'kaminApp.vrstaUsluge.home.title' },
    loadChildren: () => import('./vrsta-usluge/vrsta-usluge.routes'),
  },
  {
    path: 'status-evidencije',
    data: { pageTitle: 'kaminApp.statusEvidencije.home.title' },
    loadChildren: () => import('./status-evidencije/status-evidencije.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
