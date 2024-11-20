import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainComponent} from './pages/main/main.component';
import {GameListComponent} from './pages/game-list/game-list.component';
import {MyGamesComponent} from './pages/my-games/my-games.component';
import {ManageGameComponent} from './pages/manage-game/manage-game.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    children: [
      {
        path: '',
        component: GameListComponent
      },
      {
        path: 'my-games',
        component: MyGamesComponent
      },
      {
        path: 'manage',
        component: ManageGameComponent
      },
      {
        path: 'manage/:gameId',
        component: ManageGameComponent
      },
    ]
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GameRoutingModule { }
