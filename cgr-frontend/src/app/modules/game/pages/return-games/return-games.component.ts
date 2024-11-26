import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedGameResponse} from '../../../../services/models/page-response-borrowed-game-response';
import {GameService} from '../../../../services/services/game.service';
import {BorrowedGameResponse} from '../../../../services/models/borrowed-game-response';

@Component({
  selector: 'app-return-games',
  templateUrl: './return-games.component.html',
  styleUrl: './return-games.component.scss'
})
export class ReturnGamesComponent implements OnInit {
  returnedGames: PageResponseBorrowedGameResponse = {};

  page = 0;
  size = 5;
  pages: any = [];
  message = '';
  level = 'success';

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    this.findAllReturnedGames();
  }

  private findAllReturnedGames() {
    this.gameService.findAllReturnedGames({
      page: this.page,
      size: this.size,
    }).subscribe({
      next: (response) => {
        this.returnedGames = response;
        this.pages = Array(this.returnedGames.totalPages)
          .fill(0)
          .map((x, i) => i);
      }
    })
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllReturnedGames();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedGames();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllReturnedGames();
  }

  goToLastPage() {
    this.page = this.returnedGames.totalPages as number - 1;
    this.findAllReturnedGames();
  }

  goToNextPage() {
    this.page++;
    this.findAllReturnedGames();
  }

  get isLastPage() {
    return this.page === this.returnedGames.totalPages as number - 1;
  }


  approveGameReturn(game: BorrowedGameResponse) {
    if (!game.returned) {
      this.level = 'error';
      this.message = 'The Game is not yet returned';
      return;
    }
    this.gameService.approveReturnBorrowedGame({
      'game-id': game.id as number,
    }).subscribe({
      next: () => {
      this.level = 'success';
      this.message = 'Game return approved';
      this.findAllReturnedGames();
      }
    });
  }

}
