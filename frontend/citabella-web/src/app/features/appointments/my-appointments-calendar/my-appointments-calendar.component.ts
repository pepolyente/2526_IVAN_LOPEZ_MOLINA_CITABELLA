import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CalendarOptions, EventClickArg, EventContentArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import esLocale from '@fullcalendar/core/locales/es';
import { AppointmentService } from '../../../core/services/appointment.service';
import { AppointmentResponse } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-my-appointments-calendar',
  standalone: false,
  templateUrl: './my-appointments-calendar.component.html',
  styleUrls: ['./my-appointments-calendar.component.css']
})
export class MyAppointmentsCalendar implements OnInit {
  selectedAppointment: AppointmentResponse | null = null;
  showDetailModal = false;

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    locale: esLocale,
    initialView: 'timeGridWeek',
    selectable: false,
    nowIndicator: true,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay'
    },
    events: [],
    eventClick: (info) => this.onEventClick(info),
    eventContent: (arg) => this.renderEventContent(arg)
  };

  constructor(
    private appointmentSvc: AppointmentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadMyAppointments();
  }

  loadMyAppointments(): void {
    this.appointmentSvc.getMyAppointments({ page: 0, size: 200 }).subscribe({
      next: page => {
        const events = page.content.map(ap => ({
          id: String(ap.id),
          title: ap.treatments?.map(t => t.name).join(', ') ?? '',
          start: ap.startAt,
          end: ap.endAt,
          classNames: [`fc-status-${ap.status.toLowerCase()}`],
          extendedProps: { raw: ap }
        }));
        this.calendarOptions = { ...this.calendarOptions, events };
        this.cdr.detectChanges();
      },
      error: () => {}
    });
  }

  onEventClick(info: EventClickArg): void {
    const id = Number(info.event.id);
    const found = (this.calendarOptions.events as any[]).find(e => e.id === String(id));
    if (found?.extendedProps?.['raw']) {
      this.selectedAppointment = found.extendedProps['raw'];
      this.showDetailModal = true;
      this.cdr.detectChanges();
    }
  }

  renderEventContent(arg: EventContentArg): { html: string } {
    const ap = arg.event.extendedProps?.['raw'];
    const employeeName = ap?.employee?.name ?? '—';
    return {
      html: `
        <div class="cb-event-inner">
          <div class="cb-event-employee">${employeeName}</div>
        </div>
      `
    };
  }

  closeDetailModal(): void {
    this.showDetailModal = false;
    this.selectedAppointment = null;
  }
}
