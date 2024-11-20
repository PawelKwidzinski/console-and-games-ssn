import {Component, OnInit} from '@angular/core';
import {GameRequest} from '../../../../services/models/game-request';
import {GameService} from '../../../../services/services/game.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-manage-game',
  templateUrl: './manage-game.component.html',
  styleUrl: './manage-game.component.scss'
})
export class ManageGameComponent implements OnInit {
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

  constructor(private gameService: GameService, private router: Router, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    const gameId = this.activatedRoute.snapshot.params['gameId'];
    if (gameId) {
      this.gameService.findGameById({
        'game-id': gameId,
      }).subscribe({
        next: (game) => {
          this.gameRequest = {
            id: game.id,
            description: game.description as string,
            genre: game.genre as string,
            language: game.language as string,
            platform: game.platform as string,
            studio: game.studio as string,
            title: game.title as string,
            shareable: game.shareable
          }
          this.selectedPicture = 'data:image/jpeg;base64,' + game.cover;
          console.log(game.cover);
        }
      });
    }
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
