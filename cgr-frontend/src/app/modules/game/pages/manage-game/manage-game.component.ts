import { Component } from '@angular/core';
import {GameRequest} from '../../../../services/models/game-request';
import {GameService} from '../../../../services/services/game.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-manage-game',
  templateUrl: './manage-game.component.html',
  styleUrl: './manage-game.component.scss'
})
export class ManageGameComponent {
  errorMsg: string[] = [];
  selectedGameCover: any;
  selectedPicture: string | undefined;
  gameRequest: GameRequest = {
    description: '',
    genre: '',
    language: '',
    platform: '',
    studio: '',
    title: ''
  };

  constructor(private gameService: GameService, private router: Router) {
  }

  onFileSelected(fileCover: any) {
    this.selectedGameCover = fileCover.target.files[0];
    console.log(this.selectedGameCover);
    if (this.selectedGameCover) {
      const fileReader = new FileReader();
      fileReader.onload = () => {
        this.selectedPicture = fileReader.result as string;
      }
      fileReader.readAsDataURL(this.selectedGameCover);
    }
  }

  saveMyGame() {
    this.gameService.saveGame({
      body: this.gameRequest
    }).subscribe({
      next: (gameId) => {
        this.gameService.uploadGameCoverPicture({
          'game-id': gameId,
          body: {
            file: this.selectedGameCover
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/games/my-games']);
          }
        })
      },
      error: (err) => {
        this.errorMsg = err.error.validationErrors;
      }
    });
  }
}
