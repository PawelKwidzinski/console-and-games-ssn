import {Component, OnInit} from '@angular/core';
import {AuthenticationRequest} from '../../services/models/authentication-request';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';
import {TokenService} from '../../services/token/token.service';
import {KeycloakService} from '../../services/keycloak/keycloak.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit{

  // authRequest: AuthenticationRequest = {email: '', password: ''};
  // errorMessage: Array<string> = [];

  constructor(
    private keycloakService: KeycloakService
  ) {
  }

  async ngOnInit(): Promise<void> {
    await this.keycloakService.init();
    await this.keycloakService.login();
  }

/*  login() {
    this.errorMessage = []; // to reset error messages
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: response => {
        this.keycloakService.keycloak.token = response.token as string; // save token
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
  }*/
}
