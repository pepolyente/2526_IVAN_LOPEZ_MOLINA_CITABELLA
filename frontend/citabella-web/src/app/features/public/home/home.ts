import { Component, OnInit } from '@angular/core';
import { TreatmentService } from '../../../core/services/treatment.service';
import { ProductService } from '../../../core/services/product.service';
import { TreatmentResponse } from '../../../shared/models/treatment.model';
import { ProductPublicResponse } from '../../../shared/models/product.model';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  treatments: TreatmentResponse[] = [];
  products: ProductPublicResponse[] = [];

  constructor(
    private treatmentService: TreatmentService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.treatmentService.getAll().subscribe(data => (this.treatments = data.slice(0, 3)));
    this.productService.getAllActive().subscribe(data => (this.products = data.slice(0, 4)));
  }
}
