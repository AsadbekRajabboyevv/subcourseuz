import {
  ApplicationRef,
  ComponentRef,
  Injectable,
  Type,
  createComponent,
  inject,
} from '@angular/core';
import { ConfirmConfig, ModalConfig } from '../interfaces';

interface ActiveModal {
  id: string;
  ref: ComponentRef<unknown>;
  hostElement: HTMLElement;
}

@Injectable({ providedIn: 'root' })
export class ModalService {
  private readonly appRef = inject(ApplicationRef);
  private readonly activeModals: ActiveModal[] = [];

  open<T>(component: Type<T>, config?: ModalConfig): ComponentRef<T> {
    const hostElement = document.createElement('div');
    document.body.appendChild(hostElement);

    const ref = createComponent(component, {
      environmentInjector: this.appRef.injector,
      hostElement,
    });

    // Pass config inputs if the component accepts them
    if (config) {
      const instance = ref.instance as Record<string, unknown>;
      if (config.title !== undefined) instance['title'] = config.title;
      if (config.size !== undefined) instance['size'] = config.size;
      if (config.closable !== undefined) instance['closable'] = config.closable;
      if (config.data !== undefined) instance['data'] = config.data;
    }

    this.appRef.attachView(ref.hostView);
    ref.changeDetectorRef.detectChanges();

    const id = crypto.randomUUID();
    this.activeModals.push({ id, ref: ref as ComponentRef<unknown>, hostElement });

    return ref;
  }

  close(id?: string): void {
    if (id) {
      const index = this.activeModals.findIndex(m => m.id === id);
      if (index !== -1) {
        this.destroyModal(this.activeModals[index]);
        this.activeModals.splice(index, 1);
      }
    } else {
      // Close the last opened modal
      const last = this.activeModals.pop();
      if (last) this.destroyModal(last);
    }
  }

  confirm(config: ConfirmConfig): Promise<boolean> {
    return new Promise<boolean>(resolve => {
      const hostElement = document.createElement('div');
      document.body.appendChild(hostElement);

      // Inline minimal confirm dialog using DOM
      const overlay = document.createElement('div');
      overlay.className =
        'fixed inset-0 z-50 flex items-center justify-center bg-black/50';

      const dialog = document.createElement('div');
      dialog.className =
        'bg-white rounded-lg shadow-lg p-6 max-w-sm w-full mx-4';

      const title = document.createElement('h2');
      title.className = 'text-lg font-semibold text-gray-700 mb-2';
      title.textContent = config.title;

      const message = document.createElement('p');
      message.className = 'text-gray-600 mb-6';
      message.textContent = config.message;

      const actions = document.createElement('div');
      actions.className = 'flex justify-end gap-3';

      const cancelBtn = document.createElement('button');
      cancelBtn.className =
        'px-4 py-2 rounded border border-gray-300 text-gray-700 hover:bg-gray-50';
      cancelBtn.textContent = config.cancelLabel ?? 'Bekor qilish';

      const confirmBtn = document.createElement('button');
      confirmBtn.className =
        'px-4 py-2 rounded bg-green-500 text-white hover:bg-green-600';
      confirmBtn.textContent = config.confirmLabel ?? 'Tasdiqlash';

      const cleanup = () => {
        document.body.removeChild(overlay);
      };

      cancelBtn.addEventListener('click', () => { cleanup(); resolve(false); });
      confirmBtn.addEventListener('click', () => { cleanup(); resolve(true); });
      overlay.addEventListener('click', (e) => {
        if (e.target === overlay) { cleanup(); resolve(false); }
      });

      actions.appendChild(cancelBtn);
      actions.appendChild(confirmBtn);
      dialog.appendChild(title);
      dialog.appendChild(message);
      dialog.appendChild(actions);
      overlay.appendChild(dialog);
      document.body.appendChild(overlay);
    });
  }

  private destroyModal(modal: ActiveModal): void {
    this.appRef.detachView(modal.ref.hostView);
    modal.ref.destroy();
    if (document.body.contains(modal.hostElement)) {
      document.body.removeChild(modal.hostElement);
    }
  }
}
