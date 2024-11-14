import {Component} from '@angular/core';
import {AuthenticationRequest} from '../../services/models/authentication-request';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';
import {TokenService} from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMessage: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {
  }

  login() {
    this.errorMessage = []; // to reset error messages
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: response => {
        this.tokenService.token = response.token as string; // save token
        this.router.navigate(['games']);
      },
      error: err => {
        console.log(err);
        if (err.error.validationErrors) { // to display in login form validationErrors from backend
          this.errorMessage = err.error.validationErrors;
        } else {
          this.errorMessage.push(err.error.error);
        }
      }
    })
  }

  register() {
    this.router.navigate(['register']);
  }
}
