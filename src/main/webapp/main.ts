/// <reference types="@angular/localize" />

import { LOCALE_ID, importProvidersFrom } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeEn from '@angular/common/locales/en';
import localeRu from '@angular/common/locales/ru';
import localeUz from '@angular/common/locales/uz';
import localeUzCyrl from '@angular/common/locales/uz-Cyrl';
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

registerLocaleData(localeEn, 'en');
registerLocaleData(localeRu, 'ru');
registerLocaleData(localeUz, 'uz');
registerLocaleData(localeUzCyrl, 'uz-Cyrl');

bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [
    ...(appConfig.providers ?? []),
    {
      provide: LOCALE_ID,
      useFactory: () => document.documentElement.lang || 'uz',
    },
  ],
}).catch(err => console.error(err));
