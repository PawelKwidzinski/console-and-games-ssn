import {Component, OnInit} from '@angular/core';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {GameService} from '../../../../services/services/game.service';
import {Router} from '@angular/router';
import {GameResponse} from '../../../../services/models/game-response';

@Component({
  selector: 'app-my-games',
  templateUrl: './my-games.component.html',
  styleUrl: './my-games.component.scss'
})
export class MyGamesComponent implements OnInit {
  gameResponse: PageResponseGameResponse = {};
  page = 0;
  size = 5;
  pages: any = [];


  constructor(private gameService: GameService, private router: Router) {
  }

  ngOnInit(): void {
    this.findAllGames();
  }

  private findAllGames() {
    this.gameService.findAllGamesByOwner({
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

  archiveGame(game: GameResponse) {
    this.gameService.updateArchiveStatus({
      'game-id': game.id as number
    }).subscribe({
      next: () => {
        game.archived = !game.archived;
      }
    })
  }

  shareGame(game: GameResponse) {
    this.gameService.updateSharableStatus({
      'game-id': game.id as number
    }).subscribe({
      next: () => {
        game.shareable = !game.shareable;
      }
    })
  }

  editGame(game: GameResponse) {
    this.router.navigate(['games', 'manage', game.id]);
  }
}
