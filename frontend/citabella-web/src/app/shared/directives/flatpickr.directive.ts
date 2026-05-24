import {
  Directive,
  ElementRef,
  Input,
  AfterViewInit,
  OnDestroy
} from '@angular/core';

import flatpickr from 'flatpickr';

import { Instance as FlatpickrInstance } from 'flatpickr/dist/types/instance';

import { Spanish } from 'flatpickr/dist/l10n/es';

@Directive({
  selector: '[appFlatpickr]',
  standalone: false
})
export class FlatpickrDirective implements AfterViewInit, OnDestroy {

  private instance: FlatpickrInstance | null = null;

  @Input() flatpickrConfig: any = {};

  constructor(
    private el: ElementRef<HTMLInputElement>
  ) {}

  ngAfterViewInit(): void {
    const defaultConfig = {
      enableTime: true,
      time_24hr: true,
      allowInput: true,
      locale: Spanish,
      dateFormat: 'Y-m-d\\TH:i:S',
      altInput: true,
      altFormat: 'd-m-Y H:i',
      disableMobile: true,
    };

    const config = { ...defaultConfig, ...this.flatpickrConfig };
    this.instance = flatpickr(this.el.nativeElement, config);
  }

  ngOnDestroy(): void {
    this.instance?.destroy();
  }
}
