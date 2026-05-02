export interface ProductPublicResponse {
  id: number;
  name: string;
  category: string;
  salePrice: number;
  imageKey: string;
}

export interface ProductPrivateResponse {
  id: number;
  name: string;
  category?: string;
  purchasePrice?: number;
  salePrice?: number;
  supplier?: string;
  isCritical: boolean;
  active: boolean;
  imageKey?: string;
}

export interface ProductRequest {
  name: string;
  category?: string;
  purchasePrice?: number;
  salePrice?: number;
  usageType?: UsageType;
  supplier?: string;
  isCritical?: boolean;
  imageKey?: string;
}

export type UsageType = 'INTERNAL' | 'SALE' | 'BOTH';
