export interface EmployeeRequest {
  name: string;
  position: string;
}

export interface EmployeeResponse {
  id: number;
  name: string;
  position: string;
  active: boolean;
}
