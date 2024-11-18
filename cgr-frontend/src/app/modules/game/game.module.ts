import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GameRoutingModule } from './game-routing.module';
import { MainComponent } from './pages/main/main.component';
import { MenuComponent } from './components/menu/menu.component';
import { GameListComponent } from './pages/game-list/game-list.component';
import { GameCardComponent } from './components/game-card/game-card.component';
import { RatingComponent } from './components/rating/rating.component';


@NgModule({
  declarations: [
    MainComponent,
    MenuComponent,
    GameListComponent,
    GameCardComponent,
    RatingComponent
  ],
  imports: [
    CommonModule,
    GameRoutingModule
  ]
})
export class GameModule { }
