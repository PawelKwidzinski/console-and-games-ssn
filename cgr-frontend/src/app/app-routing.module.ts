import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {ActivateAccountComponent} from './pages/activate-acconunt/activate-account.component';

const routes: Routes = [{
  path: 'login',
  component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'activate-account',
    component: ActivateAccountComponent
  },
  {
    path:'games',
    loadChildren: () => import('./modules/game/game.module')
      .then(m => m.GameModule)
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
