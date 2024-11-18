import {Component, EventEmitter, Input, Output} from '@angular/core';
import {GameResponse} from '../../../../services/models/game-response';

@Component({
  selector: 'app-game-card',
  templateUrl: './game-card.component.html',
  styleUrl: './game-card.component.scss'
})
export class GameCardComponent {
  private _game: GameResponse = {};
  private _gameCover: string | undefined;
  private _manage = false;

  get game(): GameResponse {
    return this._game;
  }

  @Input()
  set game(value: GameResponse) {
    this._game = value;
  }

  get gameCover(): string | undefined {
    if (this._game.cover) {
      return 'data:image/jpg;base64, ' + this._game.cover;
    }
    return "https://imgs.search.brave.com/5T0YhrQyy2-DYWBl5aq_9ic3XklE-ucMB_kkMPGcqr0/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly9wcmV2/aWV3LnJlZGQuaXQv/d2hhdHMteW91ci1m/YXZvcml0ZS1sb3Jk/LW9mLXRoZS1yaW5n/cy1nYW1lLWJlc2lk/ZXMtc2hhZG93LXYw/LTJrYjI0cnZ3enZw/YTEuanBnP3dpZHRo/PTY0MCZjcm9wPXNt/YXJ0JmF1dG89d2Vi/cCZzPTkxMTk2NWM2/YWMwZDAzM2RmMTM3/NmVmNWM3ODc2NWMz/MWQ1YWY3OGY";
  }

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  @Output() private share: EventEmitter<GameResponse> = new EventEmitter();
  @Output() private archive: EventEmitter<GameResponse> = new EventEmitter();
  @Output() private addToWaitingList: EventEmitter<GameResponse> = new EventEmitter();
  @Output() private borrow: EventEmitter<GameResponse> = new EventEmitter();
  @Output() private edit: EventEmitter<GameResponse> = new EventEmitter();
  @Output() private details: EventEmitter<GameResponse> = new EventEmitter();

  onShowDetails() {
    this.details.emit(this._game)
  }

  onBorrow() {
    this.borrow.emit(this._game)
  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this._game)
  }

  onEdit() {
    this.edit.emit(this._game)
  }

  onShare() {
    this.share.emit(this._game)
  }

  onArchive() {
    this.archive.emit(this._game)
  }

}
