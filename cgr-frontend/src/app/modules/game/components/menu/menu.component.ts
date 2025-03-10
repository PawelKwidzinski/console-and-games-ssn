import {Component, OnInit} from '@angular/core';
import {KeycloakService} from '../../../../services/keycloak/keycloak.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {

  constructor(private keycloakService: KeycloakService) {
  }

  loggedUser: string | undefined = '';

  ngOnInit(): void {
    const linkColor = document.querySelectorAll('.nav-link')
    linkColor.forEach((link) => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active')
      }
      link.addEventListener('click', () => {
        linkColor.forEach((link) => {
          link.classList.remove('active');
        })
        link.classList.add('active');
      });
    });
    this.loggedUser = this.keycloakService.getUsernameFromKeycloak()
  }

  async logout() {
    this.keycloakService.logout()
  }


}
