import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedGameResponse} from '../../../../services/models/page-response-borrowed-game-response';
import {BorrowedGameResponse} from '../../../../services/models/borrowed-game-response';
import {GameService} from '../../../../services/services/game.service';
import {FeedbackRequest} from '../../../../services/models/feedback-request';
import {FeedbackService} from '../../../../services/services/feedback.service';

@Component({
  selector: 'app-borrowed-game-list',
  templateUrl: './borrowed-game-list.component.html',
  styleUrl: './borrowed-game-list.component.scss'
})
export class BorrowedGameListComponent implements OnInit {
  borrowedGamesResponse: PageResponseBorrowedGameResponse = {};
  selectedGame: BorrowedGameResponse | undefined = undefined;
  feedbackRequest: FeedbackRequest = {comment: '', gameId: 0, note: 0};

  page = 0;
  size = 5;
  pages: any = [];

  constructor(private gameService: GameService, private feedbackService: FeedbackService) {
  }

  ngOnInit(): void {
    this.findAllBorrowedGames();
  }

  returnBorrowedGame(game: BorrowedGameResponse) {
    this.selectedGame = game;
    this.feedbackRequest.gameId = game.id as number;
  }

  private findAllBorrowedGames() {
    this.gameService.findAllBorrowedGames({
      page: this.page,
      size: this.size,
    }).subscribe({
      next: (res) => {
        this.borrowedGamesResponse = res;
        this.pages = Array(this.borrowedGamesResponse.totalPages)
          .fill(0)
          .map((x, i) => i);
      }
    })
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllBorrowedGames();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedGames();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedGames();
  }

  goToLastPage() {
    this.page = this.borrowedGamesResponse.totalPages as number - 1;
    this.findAllBorrowedGames();
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedGames();
  }

  get isLastPage() {
    return this.page === this.borrowedGamesResponse.totalPages as number - 1;
  }

  returnGame(withFeedback: boolean) {
    this.gameService.returnBorrowedGame({
      'game-id': this.selectedGame?.id as number,
    }).subscribe({
      next: () => {
        if (withFeedback) {
          this.giveFeedback();
        }
        this.selectedGame = undefined;
        this.findAllBorrowedGames();
      }
    })
  }

  private giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: () => {

      }
    })
  }
}
