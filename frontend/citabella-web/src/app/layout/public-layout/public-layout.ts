import { Component } from '@angular/core';

@Component({
  selector: 'app-public-layout',
  standalone: false,
  template: `
    <app-public-header></app-public-header>
    <main class="public-content">
      <router-outlet></router-outlet>
    </main>
    <footer class="pub-footer">
      <div class="pub-footer-inner">
        <div class="pub-footer-brand">
          <h3>CitaBella</h3>
          <p>Tu centro de belleza de confianza. Reserva tus tratamientos favoritos en minutos.</p>
        </div>
        <div class="pub-footer-col">
          <h4>Explorar</h4>
          <ul>
            <li><a routerLink="/">Inicio</a></li>
            <li><a routerLink="/servicios">Servicios</a></li>
            <li><a routerLink="/productos">Productos</a></li>
          </ul>
        </div>
      </div>
      <div class="pub-footer-bottom">
        <span>&copy; {{ currentYear }} CitaBella. Todos los derechos reservados.</span>
        <div>
          <a href="#">Política de privacidad</a> • <a href="#">Términos</a>
        </div>
      </div>
    </footer>
  `,
})
export class PublicLayout {
  currentYear = new Date().getFullYear();
}
