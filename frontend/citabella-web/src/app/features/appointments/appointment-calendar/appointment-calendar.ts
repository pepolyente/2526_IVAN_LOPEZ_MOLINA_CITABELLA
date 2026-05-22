import { ChangeDetectorRef, Component, OnInit, HostListener } from '@angular/core';
import {
  CalendarOptions, EventClickArg, DateSelectArg, EventContentArg
} from '@fullcalendar/core';
import dayGridPlugin    from '@fullcalendar/daygrid';
import timeGridPlugin   from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import esLocale         from '@fullcalendar/core/locales/es';
import { AppointmentService }  from '../../../core/services/appointment.service';
import {AppointmentResponse, AppointmentStatus} from '../../../shared/models/appointment.model';

const STATUS_COLORS: Record<string, string> = {
  PENDING:     '#F59E0B',
  CONFIRMED:   '#3B82F6',
  IN_PROGRESS: '#8B5CF6',
  COMPLETED:   '#10B981',
  CANCELLED:   '#EF4444',
  NO_SHOW:     '#6B7280',
};

@Component({
  selector: 'app-appointment-calendar',
  standalone: false,
  templateUrl: './appointment-calendar.html',
  styleUrl: './appointment-calendar.css',
})
export class AppointmentCalendar implements OnInit {
  filterStatus: AppointmentStatus | '' = '';
  selectedAppointment: AppointmentResponse | null = null;
  showDetailModal = false;
  showCreateModal = false;
  selectedSlot: { start: Date; end: Date } | null = null;

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    locale: esLocale,
    initialView: 'timeGridWeek',
    selectable: true,
    selectMirror: true,
    nowIndicator: true,
    headerToolbar: {
      left:   'prev,next today',
      center: 'title',
      right:  'dayGridMonth,timeGridWeek,timeGridDay',
    },
    events: [],
    eventClick:    (info) => this.onEventClick(info),
    select:        (info) => this.onSlotSelect(info),
    eventContent:  (arg)  => this.renderEventContent(arg),
  };

  private readonly mobileView  = 'timeGridDay';
  private readonly tabletView  = 'timeGridWeek';
  private readonly desktopView = 'timeGridWeek';
  readonly statuses: AppointmentStatus[] = [
    'PENDING', 'CONFIRMED', 'IN_PROGRESS', 'CANCELLED', 'COMPLETED', 'NO_SHOW',
  ];
  constructor(
    private appointmentSvc: AppointmentService,
    private cdr: ChangeDetectorRef,
  ) {}

  @HostListener('window:resize')
  onResize(): void { this.adjustView(); }

  ngOnInit(): void {
    this.loadAppointments();
    this.adjustView();
  }

  // ── Modal: detalle ──────────────────────────────────────────────────

  closeDetailModal(): void {
    this.showDetailModal    = false;
    this.selectedAppointment = null;
  }

  onAppointmentUpdated(): void {
    this.closeDetailModal();
    this.loadAppointments(this.filterStatus);
  }

  // ── Modal: crear desde hueco ────────────────────────────────────────

  closeCreateModal(): void {
    this.showCreateModal = false;
    this.selectedSlot    = null;
  }

  onAppointmentCreated(): void {
    this.closeCreateModal();
    this.loadAppointments(this.filterStatus);
  }

  // ── Handlers de FullCalendar ────────────────────────────────────────

  onEventClick(info: EventClickArg): void {
    const id = Number(info.event.id);
    const found = (this.calendarOptions.events as any[])
      .find((e: any) => e.id === String(id));

    if (found?.extendedProps?.['raw']) {
      this.selectedAppointment = found.extendedProps['raw'] as AppointmentResponse;
      this.showDetailModal = true;
      this.cdr.detectChanges();
    }
  }

  onSlotSelect(info: DateSelectArg): void {
    this.selectedSlot    = { start: info.start, end: info.end };
    this.showCreateModal = true;
    this.cdr.detectChanges();
  }

  renderEventContent(arg: EventContentArg): { html: string } {
    const ap: AppointmentResponse | undefined = arg.event.extendedProps?.['raw'];
    const clientName   = ap?.client?.name   ?? '—';
    const employeeName = ap?.employee?.name ?? '—';
    const overlap      = ap?.hasOverlap ? '<span class="overlap-icon">⚠️</span>' : '';
    return {
      html: `
        <div class="cb-event-inner">
          <div class="cb-event-title">${clientName}${overlap}</div>

          <div class="cb-event-employee">${employeeName}</div>
        </div>
      `,
    };
  }

  // ── Carga de datos ──────────────────────────────────────────────────
  loadAppointments(status?: AppointmentStatus | ''): void {
    const params: any = { page: 0, size: 200 };
    if (status) {
      params.status = status;
    }
    this.appointmentSvc.getAll(params).subscribe({
      next: page => {
        const events = page.content.map(ap => ({
          id:    String(ap.id),
          title: ap.treatments?.map(t => t.name).join(', ') ?? '',
          start: ap.startAt,
          end:   ap.endAt,
          classNames: [
            `fc-status-${ap.status.toLowerCase()}`,
            ...(ap.hasOverlap ? ['event-overlap'] : [])
          ],
          extendedProps: { raw: ap },
        }));
        this.calendarOptions = { ...this.calendarOptions, events };
        this.cdr.detectChanges();
      },
      error: () => {  },
    });
  }

  // ── Vista responsive ────────────────────────────────────────────────

  private adjustView(): void {
    const width = window.innerWidth;
    let newView: string;
    let headerRight: string;

    if (width < 768) {
      newView     = this.mobileView;
      headerRight = 'timeGridDay';
    } else if (width < 1024) {
      newView     = this.tabletView;
      headerRight = 'timeGridWeek,timeGridDay';
    } else {
      newView     = this.desktopView;
      headerRight = 'dayGridMonth,timeGridWeek,timeGridDay';
    }

    this.calendarOptions = {
      ...this.calendarOptions,
      initialView:  newView,
      headerToolbar: {
        left:   'prev,next today',
        center: 'title',
        right:  headerRight,
      },
    };
    this.cdr.detectChanges();
  }

  applyFilter(): void {
    this.loadAppointments(this.filterStatus);
  }
}
