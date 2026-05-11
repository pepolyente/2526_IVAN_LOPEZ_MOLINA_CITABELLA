export type Gender = 'MALE' | 'FEMALE' | 'OTHER';

export interface ClientRequest {
  name: string;
  phoneNumber: string;
  birthday?: string;   // format: "YYYY-MM-DD"
  gender?: Gender;
}

export interface ClientResponse {
  id: number;
  name: string;
  phoneNumber: string;
  gender?: Gender;
  linkedUsername: string | null;
  active: boolean;
}
