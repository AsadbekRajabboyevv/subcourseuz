import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { StepConfig } from '../interfaces';

@Component({
  selector: 'app-stepper',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stepper.component.html',
})
export class StepperComponent {
  @Input() steps: StepConfig[] = [];
  @Input() currentStep = 0;

  @Output() stepChange = new EventEmitter<number>();

  next(): void {
    const nextIndex = this.currentStep + 1;
    if (nextIndex >= this.steps.length) return;
    const current = this.steps[this.currentStep];
    if (current && current.valid === false) {
      return;
    }
    this.currentStep = nextIndex;
    this.stepChange.emit(this.currentStep);
  }

  previous(): void {
    const prevIndex = this.currentStep - 1;
    if (prevIndex < 0) return;
    this.currentStep = prevIndex;
    this.stepChange.emit(this.currentStep);
  }

  protected goTo(index: number): void {
    if (index < 0 || index >= this.steps.length) return;
    this.currentStep = index;
    this.stepChange.emit(this.currentStep);
  }
}

