import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-public-layout',
  standalone: false,
  template: `
    <app-public-header></app-public-header>
    <main class="public-content">
      <router-outlet></router-outlet>
    </main>
    @if (router.url === '/'){
      <div class="features-strip">
        <div class="features-strip-inner">
          <div class="feature-item">
            <span class="feature-icon material-symbols-outlined">calendar_month</span>
            <div class="feature-text">
              <div class="feature-title">Revisa tus citas online 24/7</div>
              <div class="feature-desc">Visualiza tu agenda desde cualquier dispositivo</div>
            </div>
          </div>
          <div class="feature-sep"></div>
          <div class="feature-item">
            <span class="feature-icon material-symbols-outlined">spa</span>
            <div class="feature-text">
              <div class="feature-title">Tratamientos exclusivos</div>
              <div class="feature-desc">Personalizados para cada tipo de belleza</div>
            </div>
          </div>
          <div class="feature-sep"></div>
          <div class="feature-item">
            <span class="feature-icon material-symbols-outlined">verified</span>
            <div class="feature-text">
              <div class="feature-title">Profesionales expertas</div>
              <div class="feature-desc">Equipo certificado con años de experiencia</div>
            </div>
          </div>
          <div class="feature-sep"></div>
          <div class="feature-item">
            <span class="feature-icon material-symbols-outlined">star</span>
            <div class="feature-text">
              <div class="feature-title">Productos premium</div>
              <div class="feature-desc">Solo usamos marcas de alta gama</div>
            </div>
          </div>
        </div>
      </div>
    }
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
    <app-toast-container></app-toast-container>
  `,
})
export class PublicLayout {
  currentYear = new Date().getFullYear();
  constructor(public router: Router) {}
}
