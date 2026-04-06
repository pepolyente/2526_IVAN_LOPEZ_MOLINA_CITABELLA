import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserService } from '../../../core/services/user.service';
import { UserResponse } from '../../../shared/models/user.model';

@Component({
  selector: 'app-user-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Usuarios del sistema</h2>
    </div>
    @if (loading) {
      <p class="empty-state">Cargando...</p>
    } @else if (users.length === 0) {
      <p class="empty-state">No hay usuarios registrados.</p>
    } @else {
      <table class="simple-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Usuario</th>
          <th>Email</th>
          <th>Rol</th>
        </tr>
        </thead>
        <tbody>
          @for (user of users; track user.id) {
            <tr>
              <td>{{ user.id }}</td>
              <td>{{ user.username }}</td>
              <td>{{ user.email }}</td>
              <td><span class="badge">{{ user.role }}</span></td>
            </tr>
          }
        </tbody>
      </table>
    }
  `,
})
export class UserList implements OnInit {
  users: UserResponse[] = [];
  loading = true;

  constructor(private svc: UserService, private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.svc.getAll().subscribe({
      next: data => {
        this.users = data;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }
}
