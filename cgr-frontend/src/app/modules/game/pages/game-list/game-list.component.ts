import {Component, OnInit} from '@angular/core';
import {GameService} from '../../../../services/services/game.service';
import {Router} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.scss'
})
export class GameListComponent implements OnInit {
  gameResponse: PageResponseGameResponse = {};
  page = 0;
  size = 2;
  pages: any = [];

  constructor(private gameService: GameService, private router: Router) {
  }

  ngOnInit(): void {
    this.findAllGames();
  }

  private findAllGames() {
    this.gameService.findAllGames({
        page: this.page,
        size: this.size
      }).subscribe({
      next: (games) => {
        this.gameResponse = games;
        this.pages = Array(this.gameResponse.totalPages)
          .fill(0)
          .map((x, i) => i)
      }
    });
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllGames();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllGames();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllGames();
  }

  goToLastPage() {
    this.page = this.gameResponse.totalPages as number - 1;
    this.findAllGames();
  }

  goToNextPage() {
    this.page++;
    this.findAllGames();
  }

  get isLastPage() {
    return this.page === this.gameResponse.totalPages as number - 1;
  }

}
