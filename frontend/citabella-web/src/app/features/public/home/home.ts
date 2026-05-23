import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TreatmentService } from '../../../core/services/treatment.service';
import { ProductService } from '../../../core/services/product.service';
import { TreatmentResponse } from '../../../shared/models/treatment.model';
import { ProductPublicResponse } from '../../../shared/models/product.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  treatments: TreatmentResponse[] = [];
  products: ProductPublicResponse[] = [];
  readonly placeholder = '/images/citabella.jpg';

  constructor(
    private treatmentService: TreatmentService,
    private productService: ProductService,
    public auth: AuthService,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.treatmentService.getAll().subscribe({
      next: data => {
        this.treatments = data.content.slice(0, 4);
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.treatments = [];
      }
    });

    this.productService.getAllActive().subscribe({
      next: data => {
        this.products = data.slice(0, 4);
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.products = [];
      }
    });
  }
  getProductImage(imageKey?: string): string {

    if (!imageKey) {
      return this.placeholder;
    }

    return `/images/${imageKey}.webp`;
  }
}
