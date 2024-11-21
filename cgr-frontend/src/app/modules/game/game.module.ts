import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GameRoutingModule } from './game-routing.module';
import { MainComponent } from './pages/main/main.component';
import { MenuComponent } from './components/menu/menu.component';
import { GameListComponent } from './pages/game-list/game-list.component';
import { GameCardComponent } from './components/game-card/game-card.component';
import { RatingComponent } from './components/rating/rating.component';
import { MyGamesComponent } from './pages/my-games/my-games.component';
import { ManageGameComponent } from './pages/manage-game/manage-game.component';
import {FormsModule} from '@angular/forms';
import { BorrowedGameListComponent } from './pages/borrowed-game-list/borrowed-game-list.component';


@NgModule({
  declarations: [
    MainComponent,
    MenuComponent,
    GameListComponent,
    GameCardComponent,
    RatingComponent,
    MyGamesComponent,
    ManageGameComponent,
    BorrowedGameListComponent
  ],
  imports: [
    CommonModule,
    GameRoutingModule,
    FormsModule
  ]
})
export class GameModule { }
