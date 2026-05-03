import { ClientResponse } from './client.model';
import { EmployeeResponse } from './employee.model';
import { TreatmentResponse } from './treatment.model';

export type AppointmentStatus =
  | 'PENDING'
  | 'CONFIRMED'
  | 'IN_PROGRESS'
  | 'CANCELLED'
  | 'COMPLETED'
  | 'NO_SHOW';

export interface CreateAppointmentRequest {
  clientId?: number; //it should always be in it
  employeeId?: number; //it should always be in it
  treatmentsIds: number[];
  startAt: string;          // ISO 8601: "2025-04-01T10:00:00"
  endAt: string;
  notes?: string;
}

export interface AppointmentResponse {
  id: number;
  startAt: string;
  endAt: string;
  status: AppointmentStatus;
  notes?: string;
  hasOverlap: boolean;
  client: ClientResponse;
  employee: EmployeeResponse;
  treatments: TreatmentResponse[];
}
export interface RescheduleAppointmentRequest {
  id: number;
  employeeId: number;
  treatmentsIds: [number];
  startAt: number;
  endAt: number;
  notes: string;
}
