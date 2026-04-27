import {
  ApplicationConfig,
  importProvidersFrom,
  provideZoneChangeDetection
} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  provideRouter,
  withRouterConfig,
  withInMemoryScrolling,
} from '@angular/router';
import { provideHttpClient, withInterceptors} from '@angular/common/http';
import { routes } from 'app/app.routes';
import { authInterceptor } from './common/auth/auth.interceptor';
import { errorInterceptor } from "./common/error/error.interceptor";
import { provideMarkdown } from 'ngx-markdown';
import { HttpClient } from '@angular/common/http';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import { forkJoin, map, Observable } from 'rxjs';

export class MultiTranslateLoader implements TranslateLoader {
  constructor(private http: HttpClient, private resources: { prefix: string, suffix: string }[]) {}

  public getTranslation(lang: string): Observable<any> {
    const requests = this.resources.map(resource => {
      return this.http.get(`${resource.prefix}${lang}${resource.suffix}`);
    });

    return forkJoin(requests).pipe(
      map(response => {
        return response.reduce((acc, current) => {
          return { ...acc, ...current };
        }, {});
      })
    );
  }
}

export function HttpLoaderFactory(http: HttpClient) {
  return new MultiTranslateLoader(http, [
    { prefix: './assets/i18n/auth/', suffix: '.json' },
    { prefix: './assets/i18n/footer/', suffix: '.json' },
    { prefix: './assets/i18n/common/', suffix: '.json' },
    { prefix: './assets/i18n/home/', suffix: '.json' },
    { prefix: './assets/i18n/header/', suffix: '.json' },
    { prefix: './assets/i18n/course/', suffix: '.json' },
    { prefix: './assets/i18n/profile/', suffix: '.json' },
    { prefix: './assets/i18n/admin/', suffix: '.json' }
  ]);
}
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(
      routes,
      withRouterConfig({
        onSameUrlNavigation: 'reload'
      }),
      withInMemoryScrolling({
        scrollPositionRestoration: 'enabled',
        anchorScrolling: 'enabled'
      })
    ),

    provideHttpClient(
      withInterceptors([
        authInterceptor,
        errorInterceptor
      ])
    ),

    provideZoneChangeDetection({ eventCoalescing: true }),

    importProvidersFrom(
      BrowserAnimationsModule,
      TranslateModule.forRoot({
        loader: {
          provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [HttpClient]
        },
        defaultLanguage: 'uz',
      })
    ),

    provideMarkdown(),
  ]
};
