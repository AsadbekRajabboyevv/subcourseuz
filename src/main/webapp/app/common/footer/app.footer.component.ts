import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LucideAngularModule} from "lucide-angular";

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './app.footer.component.html'
})
export class FooterComponent {

  @Input() appTitle: string = 'My App';

  year: number = new Date().getFullYear();

  address: string = "Urganch, Al-Xorazmiy shoh ko‘chasi, 110-uy";
  phone: string = "+998 99 159-49-46";
  email: string = "asadrajab28@gmail.com";

  payments = [1, 2, 3];

  socials = [
    { icon: 'send', link: '#' },        // facebook
    { icon: 'message-circle-check', link: '#' },       // instagram
    { icon: 'mail', link: '#' },
    { icon: 'globe', link: '#' }        // linkedin
  ];
}
