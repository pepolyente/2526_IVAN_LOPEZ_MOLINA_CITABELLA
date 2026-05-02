export interface TreatmentRequest {
  name: string;
  description?: string;
  minimumDuration: number;
  maximumDuration?: number;
  price: number;
}

export interface TreatmentResponse {
  id: number;
  name: string;
  minimumDuration: number;
  price: number;
  active: boolean;
}

export interface TreatmentDetailedResponse {
  id: number;
  name: string;
  description: string;
  minimumDuration: number;
  maximumDuration: number;
  price: number;
  active: boolean;
}
