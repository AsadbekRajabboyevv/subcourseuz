# Amalga oshirish rejasi: Angular UI Komponent Kutubxonasi

## Umumiy ko'rinish

`src/main/webapp/app/shared/ui/` papkasida Angular 21 standalone komponentlar, Tailwind CSS 4 va JWT autentifikatsiya asosida UI kutubxonasini bosqichma-bosqich yaratish.

## Tasklar

- [x] 1. Foundation — Interfeyslar, Servislar va HTTP Interceptor
  - `interfaces/index.ts` da barcha TypeScript interfeyslarini yozing: `FieldConfig`, `ColumnConfig`, `MenuItem`, `FilterConfig`, `StepConfig`, `UserInfo`, `ToastMessage`, `ModalConfig`, `ConfirmConfig`, `SelectOption`, `SortEvent`, `FilterQuery`, `FileRejectionReason`
  - `AuthService` ni yozing: `signal`/`computed` asosida `currentUser`, `currentRole`, `currentPermissions`; `login()`, `logout()`, `isAuthenticated()`, `getToken()`, `handleUnauthorized()` metodlari; JWT `localStorage` da saqlanadi
  - `ThemeService` ni yozing: `theme` signal, `toggle()`, `setTheme()`, `localStorage` va `document.documentElement` dark klassi
  - `NotificationService` ni yozing: `toasts` signal, `success/error/warning/info/dismiss` metodlari, max 5 ta limit
  - `ModalService` ni yozing: `open<T>()`, `close()`, `confirm()` → `Promise<boolean>`
  - `MenuService` ni yozing: `getMenuItems(role)` → backend yoki static fallback
  - `authInterceptor` funksiyasini yozing: `Authorization: Bearer` header qo'shish, 401 da `handleUnauthorized()` chaqirish
  - _Talablar: 4.1, 4.2, 4.3, 4.7, 4.8, 7.1, 7.3, 7.8, 9.1, 9.2, 9.3, 9.8, 10.8, 10.9_

- [x] 2. Direktivalar va Guard
  - `HasRoleDirective` ni yozing: `*appHasRole="['ROLE_ADMIN']"` sintaksisi, rol mos kelmasa DOM dan olib tashlash
  - `HasPermissionDirective` ni yozing: `*appHasPermission="'courses:write'"` sintaksisi, ruxsat yo'q bo'lsa DOM dan olib tashlash
  - `roleGuard` funksiyasini yozing: `route.data['roles']` tekshirish, mos kelmasa `/unauthorized` ga yo'naltirish
  - _Talablar: 4.4, 4.5, 4.6, 10.10, 10.11_

- [x] 3. Atomik Forma Input Komponentlari
  - `InputComponent` ni yozing: `ControlValueAccessor`, `label/placeholder/type/disabled/errorMessage` inputlari, `setDisabledState` signal orqali
  - `SelectComponent` ni yozing: `ControlValueAccessor`, `options/multiple/searchable` inputlari
  - `DatepickerComponent` ni yozing: `ControlValueAccessor`, `minDate/maxDate/format` inputlari
  - `FileUploadComponent` ni yozing: `ControlValueAccessor`, `accept/maxSizeMb/multiple` inputlari, fayl turi va hajm validatsiyasi, `fileRejected` EventEmitter
  - _Talablar: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8_

- [x] 4. DynamicFormComponent
  - `DynamicFormComponent` ni yozing: `FieldConfig[]` asosida `FormGroup` quriladi, `dependsOn` shartli ko'rsatish, `roles` filtri `AuthService` orqali, validatsiya xatolari o'zbek tilida, `formSubmit` EventEmitter, `reset()` metodi
  - _Talablar: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8_

- [x] 5. DynamicTableComponent
  - `DynamicTableComponent` ni yozing: `columns/data/totalCount/pageSize/loading` inputlari, `sortChange/pageChange/filterChange` EventEmitterlar, 300ms debounce filtr, `ColumnConfig.template` TemplateRef render, `ColumnConfig.roles` filtri, loading spinner, `EmptyStateComponent` integratsiyasi
  - _Talablar: 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9_

- [ ] 6. Layout Komponentlari
  - `ContainerComponent` ni yozing: `maxWidth/padding` inputlari
  - `GridComponent` ni yozing: `cols/gap/responsive` inputlari, Tailwind grid klasslari
  - `SidebarLayoutComponent` ni yozing: `sidebarWidth/collapsible` inputlari, `collapsed` signal, `toggle()`, 768px da overlay rejimi
  - `PageWrapperComponent` ni yozing: `title/breadcrumbs/actions` content projection slotlari
  - _Talablar: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6_

- [ ] 7. Modal Tizimi
  - `ModalComponent` ni yozing: `title/size/closable` inputlari, `closed` EventEmitter, Escape tugmasi handler, scroll bloklash
  - `ConfirmModalComponent` ni yozing: `ConfirmConfig` inputi, "Tasdiqlash"/"Bekor qilish" tugmalari, `ModalService.confirm()` Promise bilan bog'lash
  - `FormModalComponent` ni yozing: `DynamicFormComponent` ni modal ichida render qilish
  - _Talablar: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8_

- [ ] 8. Bildirishnoma UI Komponentlari
  - `ToastContainerComponent` ni yozing: `NotificationService.toasts` signal ga subscribe, ekran yuqori o'ng burchagida, 4000ms auto-dismiss
  - `AlertComponent` ni yozing: `type/message/dismissible` inputlari, `dismissed` EventEmitter
  - `ErrorMessageComponent` ni yozing: `message/retryAction` inputlari, "Qayta urinish" tugmasi
  - _Talablar: 7.2, 7.3, 7.4, 7.5, 7.6, 7.7_

- [ ] 9. Filtr va Qidiruv Komponentlari
  - `SearchInputComponent` ni yozing: `placeholder/debounceMs` inputlari, `searchChange` EventEmitter, RxJS `debounceTime + distinctUntilChanged`
  - `FilterPanelComponent` ni yozing: `FilterConfig[]` inputi, `filterChange` EventEmitter, `reset()` metodi, `date-range` turi uchun ikki `DatepickerComponent`, `SearchInputComponent` bilan birlashgan `FilterQuery`
  - _Talablar: 8.1, 8.2, 8.3, 8.4, 8.5, 8.6, 8.7_

- [ ] 10. Holat va Menyu Komponentlari
  - `LoadingComponent` ni yozing: `size/text` inputlari
  - `EmptyStateComponent` ni yozing: `title/description/icon/action` inputlari
  - `ErrorStateComponent` ni yozing: `title/message/retryAction` inputlari
  - `StepperComponent` ni yozing: `steps/currentStep` inputlari, `stepChange` EventEmitter, `next()` validatsiya bilan, `previous()` validatsiyasiz
  - `ThemeSwitchComponent` ni yozing: `ThemeService.toggle()` ga bog'lash
  - `SidebarMenuComponent` ni yozing: `MenuService` dan `MenuItem[]` yuklash, rol filtri, submenu kengaytirish, `AuthService.currentRole` o'zgarganda yangilash
  - _Talablar: 9.4, 9.5, 9.6, 9.7, 10.1, 10.2, 10.3, 10.4, 10.5, 10.6, 10.7, 10.8_

<!-- - [ ] 11. Checkpoint — Barcha testlar o'tishini tekshiring
  - Barcha testlar o'tishini tekshiring, savollar bo'lsa foydalanuvchiga murojaat qiling. -->

- [ ] 11. Barrel Export (index.ts)
  - `src/main/webapp/app/shared/ui/index.ts` faylida barcha komponentlar, servislar, direktivalar, guard va interfeyslarni eksport qiling
  - _Talablar: 11.1, 11.2, 11.3, 11.4, 11.5_

## Eslatmalar

- `*` bilan belgilangan sub-tasklar ixtiyoriy, tezroq MVP uchun o'tkazib yuborilishi mumkin
- Har bir task oldingi tasklarga tayanadi — tartibda bajaring
- Barcha komponentlar Angular 21 standalone, `NgModule` ishlatilmaydi
- `inject()` pattern ishlatiladi, konstruktor injection emas
- Design system: `bg-white`, `green-500` aksent, `gray-600/700` matn, `shadow-sm`
