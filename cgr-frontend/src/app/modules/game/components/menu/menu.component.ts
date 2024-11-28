import {Component, OnInit} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {

  loggedUser = '';

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
    this.getUsernameFromToken()
  }

  logout() {
    localStorage.removeItem('token');
    window.location.reload();
  }

  private getUsernameFromToken() {
    const jwtHelper = new JwtHelperService();
    const token = localStorage.getItem('token') as string;

    if (!token) {
     this.loggedUser = 'null';
    }
    const decodedToken = jwtHelper.decodeToken(token);

    let fullName = decodedToken?.fullName as string;
    this.loggedUser = fullName.split(' ')[0];
  }

}
