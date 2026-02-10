export interface Appointment {
  //todo cambiar en base a AppointmentResponse de SpringBoot
  id: number;
  date: string;
  clientName: string;
  serviceName: string;
  status: string;
}

