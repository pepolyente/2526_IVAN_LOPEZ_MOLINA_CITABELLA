import { HttpParams } from '@angular/common/http';

export function buildHttpParams(params?: Record<string, any>): HttpParams {
  let httpParams = new HttpParams();

  if (!params) {
    return httpParams;
  }

  Object.entries(params).forEach(([key, value]) => {
    if (
      value === undefined ||
      value === null ||
      value === ''
    ) {
      return;
    }

    if (Array.isArray(value)) {
      value.forEach(v => {
        httpParams = httpParams.append(key, String(v));
      });

      return;
    }

    httpParams = httpParams.set(key, String(value));
  });

  return httpParams;
}
