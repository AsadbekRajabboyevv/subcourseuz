import {CommonModule} from '@angular/common';
import {Component} from '@angular/core';

@Component({
  selector: 'app-page-wrapper',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './page-wrapper.component.html',
})
class PageWrapperComponent {
}

export default PageWrapperComponent

