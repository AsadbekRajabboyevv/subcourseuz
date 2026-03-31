import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { environment } from 'environments/environment';
import {
  AlertComponent, ConfirmModalComponent, ContainerComponent,
  DatepickerComponent, DynamicFormComponent,
  DynamicTableComponent, FileUploadComponent, FilterPanelComponent,
  InputComponent,
  PageWrapperComponent, SearchInputComponent, SelectComponent,
  ThemeSwitchComponent, ToastContainerComponent
} from "../../shared/ui";


@Component({
  selector: 'app-home',
  imports: [CommonModule, PageWrapperComponent, ThemeSwitchComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  environment = environment;

}
