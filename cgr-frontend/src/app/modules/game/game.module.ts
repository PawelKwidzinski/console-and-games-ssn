import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GameRoutingModule } from './game-routing.module';
import { MainComponent } from './pages/main/main.component';
import { MenuComponent } from './components/menu/menu.component';
import { GameListComponent } from './pages/game-list/game-list.component';


@NgModule({
  declarations: [
    MainComponent,
    MenuComponent,
    GameListComponent
  ],
  imports: [
    CommonModule,
    GameRoutingModule
  ]
})
export class GameModule { }
