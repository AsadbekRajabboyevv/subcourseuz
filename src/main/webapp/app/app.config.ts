import { HttpClientModule } from '@angular/common/http';
import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, ExtraOptions, TitleStrategy } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { routes } from 'app/app.routes';
import { CustomTitleStrategy } from 'app/common/title-strategy.injectable';
import { TranslationLoader } from 'app/core/services/translation.loader';
import { TranslationService } from 'app/core/services/translation.service';


const routeConfig: ExtraOptions = {
  onSameUrlNavigation: 'reload',
  scrollPositionRestoration: 'enabled'
};

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(
      RouterModule.forRoot(routes, routeConfig), 
      BrowserAnimationsModule, 
      HttpClientModule,
      TranslateModule.forRoot({
        loader: {
          provide: TranslateLoader,
          useClass: TranslationLoader
        },
        defaultLanguage: 'uz'
      })
    ),
    provideZoneChangeDetection({ eventCoalescing: true }),
    TranslationService,
    {
      provide: TitleStrategy,
      useClass: CustomTitleStrategy
    }
  ]
};
