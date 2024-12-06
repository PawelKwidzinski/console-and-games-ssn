import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {authGuard} from './services/guard/auth.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'games',
    pathMatch: 'full',
  },
  {
    path: 'login',
    redirectTo: 'games',
    pathMatch: 'full',
  },
  {
    path:'games',
    loadChildren: () => import('./modules/game/game.module')
      .then(m => m.GameModule),
    canActivate: [authGuard],
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
