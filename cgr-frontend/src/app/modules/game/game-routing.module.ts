import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainComponent} from './pages/main/main.component';
import {GameListComponent} from './pages/game-list/game-list.component';
import {MyGamesComponent} from './pages/my-games/my-games.component';
import {ManageGameComponent} from './pages/manage-game/manage-game.component';
import {BorrowedGameListComponent} from './pages/borrowed-game-list/borrowed-game-list.component';
import {ReturnGamesComponent} from './pages/return-games/return-games.component';
import {authGuard} from '../../services/guard/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        component: GameListComponent,
        canActivate: [authGuard],
      },
      {
        path: 'my-games',
        component: MyGamesComponent,
        canActivate: [authGuard],
      },
      {
        path: 'my-borrowed-games',
        component: BorrowedGameListComponent,
        canActivate: [authGuard],
      },
      {
        path: 'my-returned-games',
        component: ReturnGamesComponent,
        canActivate: [authGuard],
      },
      {
        path: 'manage',
        component: ManageGameComponent,
        canActivate: [authGuard],
      },
      {
        path: 'manage/:gameId',
        component: ManageGameComponent,
        canActivate: [authGuard],
      }
    ]
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GameRoutingModule { }
