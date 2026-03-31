# Talablar Hujjati

## Kirish

Ushbu hujjat Angular 21 asosidagi UI komponent kutubxonasi uchun talablarni belgilaydi.
Kutubxona standalone komponentlar, Tailwind CSS 4, TypeScript va Spring Boot backend (JWT autentifikatsiya, `ROLE_ADMIN` / `ROLE_USER` rollari) bilan ishlaydigan loyiha uchun mo'ljallangan.
Mavjud design system: `bg-white` fon, `green-500` aksent rangi, `gray-600/700` matn, `shadow-sm` soya.
Mavjud komponentlar: `Header`, `Footer`, `LoginModal`, `RegisterModal`.

---

## Lug'at

- **UI_Library** — Angular 21 asosidagi qayta ishlatiladigan komponentlar to'plami
- **DynamicForm** — Konfiguratsiya orqali boshqariladigan forma komponenti
- **DynamicTable** — Sahifalash, saralash va filtrlash imkoniyatiga ega jadval komponenti
- **AuthService** — JWT tokenlarini boshqaruvchi va foydalanuvchi rolini saqlovchi servis
- **RoleGuard** — Marshrut darajasida ruxsatni tekshiruvchi Angular route guard
- **PermissionDirective** — Element darajasida ruxsatni boshqaruvchi Angular direktiva
- **NotificationService** — Toast va alert xabarlarini boshqaruvchi servis
- **ModalService** — Modal oynalarni dasturiy ochish/yopishni boshqaruvchi servis
- **ThemeService** — Qoʻngʻir/yorqin mavzu holatini boshqaruvchi servis
- **MenuService** — Backenddan yoki konfiguratsiyadan menyu elementlarini yuklovchi servis
- **ROLE_ADMIN** — Administrator roli
- **ROLE_USER** — Oddiy foydalanuvchi roli
- **ROLE_TEACHER** — O'qituvchi roli
- **JWT** — JSON Web Token — autentifikatsiya uchun ishlatiladigan token formati
- **Standalone Component** — `NgModule` talab qilmaydigan Angular komponenti
- **FieldConfig** — Forma maydonini tavsiflovchi TypeScript interfeysi
- **ColumnConfig** — Jadval ustunini tavsiflovchi TypeScript interfeysi
- **FilterQuery** — Qidiruv va filtrlash parametrlarini ifodalovchi TypeScript interfeysi

---

## Talablar

### Talab 1: Dinamik Forma Tizimi

**Foydalanuvchi tarixi:** Dasturchi sifatida men `FieldConfig[]` massivi orqali formalarni konfiguratsiya qilishni xohlayman, shunda har bir sahifa uchun alohida forma komponenti yozmasdan turib tezda forma yarata olaman.

#### Qabul qilish mezonlari

1. THE **UI_Library** SHALL `input`, `select`, `checkbox`, `textarea`, `radio` maydon turlarini qo'llab-quvvatlovchi `DynamicFormComponent` ni eksport qilishi kerak.
2. WHEN `FieldConfig.required` qiymati `true` bo'lsa, THE **DynamicForm** SHALL maydoni bo'sh qoldirilganda foydalanuvchiga o'zbek tilidagi xato xabarini ko'rsatishi kerak.
3. WHEN `FieldConfig.validators` massivi berilsa, THE **DynamicForm** SHALL har bir validatorni ketma-ket tekshirib, birinchi muvaffaqiyatsiz validatorning xabarini ko'rsatishi kerak.
4. WHEN `FieldConfig.dependsOn` sharti bajarilsa, THE **DynamicForm** SHALL bog'liq maydonni ko'rsatishi kerak; aks holda maydonni yashirishi kerak.
5. WHERE `FieldConfig.roles` massivi berilsa, THE **DynamicForm** SHALL faqat `AuthService.currentRole` mos keladigan hollarda maydoni ko'rsatishi kerak.
6. WHEN forma yuborilsa va barcha validatsiyalar muvaffaqiyatli o'tsa, THE **DynamicForm** SHALL `formSubmit` EventEmitter orqali forma qiymatlarini chiqarishi kerak.
7. IF forma yuborilsa va kamida bitta validatsiya xatosi mavjud bo'lsa, THEN THE **DynamicForm** SHALL formani yubormasdan barcha xato maydonlarini belgilashi kerak.
8. THE **DynamicForm** SHALL `reset()` metodini taqdim etishi kerak, bu metod chaqirilganda barcha maydon qiymatlarini boshlang'ich holatga qaytarishi kerak.

---

### Talab 2: Dinamik Jadval

**Foydalanuvchi tarixi:** Dasturchi sifatida men `ColumnConfig[]` va ma'lumotlar massivi orqali jadval yaratishni xohlayman, shunda sahifalash, saralash va filtrlash funksiyalarini qayta yozmasdan turib ishlata olaman.

#### Qabul qilish mezonlari

1. THE **UI_Library** SHALL `ColumnConfig[]`, `data[]`, `totalCount` va `pageSize` inputlarini qabul qiluvchi `DynamicTableComponent` ni eksport qilishi kerak.
2. WHEN foydalanuvchi ustun sarlavhasiga bosса, THE **DynamicTable** SHALL `sortChange` EventEmitter orqali `{ field: string, direction: 'asc' | 'desc' }` obyektini chiqarishi kerak.
3. WHEN foydalanuvchi sahifa raqamini o'zgartirsa, THE **DynamicTable** SHALL `pageChange` EventEmitter orqali yangi sahifa raqamini chiqarishi kerak.
4. WHEN `ColumnConfig.filterable` qiymati `true` bo'lsa, THE **DynamicTable** SHALL ustun sarlavhasi ostida filtr input maydonini ko'rsatishi kerak.
5. WHEN filtr qiymati o'zgarsa, THE **DynamicTable** SHALL 300ms debounce bilan `filterChange` EventEmitter orqali filtr qiymatini chiqarishi kerak.
6. WHERE `ColumnConfig.template` berilsa, THE **DynamicTable** SHALL standart matn o'rniga berilgan `TemplateRef` ni render qilishi kerak.
7. WHILE ma'lumotlar yuklanayotgan bo'lsa, THE **DynamicTable** SHALL `loading` input `true` bo'lganda skelet yoki spinner ko'rsatishi kerak.
8. IF `data` massivi bo'sh bo'lsa, THEN THE **DynamicTable** SHALL `EmptyStateComponent` ni ko'rsatishi kerak.
9. WHERE `ColumnConfig.roles` berilsa, THE **DynamicTable** SHALL faqat `AuthService.currentRole` mos keladigan ustunlarni ko'rsatishi kerak.

---

### Talab 3: Layout Tizimi

**Foydalanuvchi tarixi:** Dasturchi sifatida men tayyor layout komponentlaridan foydalanishni xohlayman, shunda har bir sahifada CSS grid va flexbox ni qayta yozmasdan turib izchil tartib yarata olaman.

#### Qabul qilish mezonlari

1. THE **UI_Library** SHALL `max-width` va `padding` ni boshqaruvchi `ContainerComponent` ni eksport qilishi kerak.
2. THE **UI_Library** SHALL `cols`, `gap`, `responsive` inputlarini qabul qiluvchi `GridComponent` ni eksport qilishi kerak.
3. THE **UI_Library** SHALL `sidebarWidth`, `collapsible` inputlarini qabul qiluvchi `SidebarLayoutComponent` ni eksport qilishi kerak.
4. WHEN `SidebarLayoutComponent.collapsible` `true` bo'lsa va foydalanuvchi toggle tugmasini bossa, THE **SidebarLayoutComponent** SHALL sidebar kengligini `64px` ga qisqartirishi va ikonkalarni ko'rsatishi kerak.
5. THE **UI_Library** SHALL `title`, `breadcrumbs`, `actions` content projection slotlarini qabul qiluvchi `PageWrapperComponent` ni eksport qilishi kerak.
6. WHILE ekran kengligi `768px` dan kichik bo'lsa, THE **SidebarLayoutComponent** SHALL sidebar ni yashirib, overlay menyu sifatida ko'rsatishi kerak.

---

### Talab 4: Autentifikatsiya va Rol Tizimi

**Foydalanuvchi tarixi:** Dasturchi sifatida men JWT tokenlarini va foydalanuvchi rollarini markazlashgan holda boshqarishni xohlayman, shunda har bir komponentda token tekshiruvini qayta yozmasdan turib rol asosida UI ni boshqara olaman.

#### Qabul qilish mezonlari

1. THE **AuthService** SHALL `login(credentials)` metodini taqdim etishi kerak; bu metod muvaffaqiyatli javob kelganda JWT tokenini `localStorage` ga saqlashi kerak.
2. THE **AuthService** SHALL `logout()` metodini taqdim etishi kerak; bu metod `localStorage` dan tokenni o'chirib, foydalanuvchini login sahifasiga yo'naltirishni amalga oshirishi kerak.
3. THE **AuthService** SHALL `currentUser$` Observable ni taqdim etishi kerak, bu Observable foydalanuvchi holati o'zgarganda yangi qiymat chiqarishi kerak.
4. THE **RoleGuard** SHALL `canActivate` interfeysini implement qilishi kerak; foydalanuvchi roli marshrut `data.roles` massivida bo'lmasa, `/unauthorized` sahifasiga yo'naltirishni amalga oshirishi kerak.
5. THE **UI_Library** SHALL `*appHasRole="['ROLE_ADMIN']"` sintaksisini qo'llab-quvvatlovchi `HasRoleDirective` ni eksport qilishi kerak.
6. WHEN `HasRoleDirective` ga berilgan rol `AuthService.currentRole` ga mos kelmasa, THE **HasRoleDirective** SHALL elementni DOM dan olib tashlashi kerak.
7. IF JWT token muddati tugagan bo'lsa, THEN THE **AuthService** SHALL `401` xatosini ushlab, foydalanuvchini avtomatik ravishda tizimdan chiqarishi kerak.
8. THE **AuthService** SHALL `isAuthenticated()` metodini taqdim etishi kerak, bu metod token mavjud va muddati o'tmagan bo'lsa `true` qaytarishi kerak.

---

### Talab 5: Forma Input Komponentlari (Atomik)

**Foydalanuvchi tarixi:** Dasturchi sifatida men `ControlValueAccessor` interfeysini implement qilgan tayyor input komponentlaridan foydalanishni xohlayman, shunda ularni `ReactiveFormsModule` bilan to'g'ridan-to'g'ri ishlatа olaman.

#### Qabul qilish mezonlari

1. THE **UI_Library** SHALL `label`, `placeholder`, `type`, `disabled`, `errorMessage` inputlarini qabul qiluvchi `InputComponent` ni eksport qilishi kerak.
2. THE **UI_Library** SHALL `options: {label, value}[]`, `multiple`, `searchable` inputlarini qabul qiluvchi `SelectComponent` ni eksport qilishi kerak.
3. THE **UI_Library** SHALL `minDate`, `maxDate`, `format` inputlarini qabul qiluvchi `DatepickerComponent` ni eksport qilishi kerak.
4. THE **UI_Library** SHALL `accept`, `maxSizeMb`, `multiple` inputlarini qabul qiluvchi `FileUploadComponent` ni eksport qilishi kerak.
5. WHEN `FileUploadComponent` ga ruxsat etilmagan fayl turi yuklansa, THE **FileUploadComponent** SHALL faylni rad etib, xato xabarini ko'rsatishi kerak.
6. WHEN `FileUploadComponent` ga `maxSizeMb` dan katta fayl yuklansa, THE **FileUploadComponent** SHALL faylni rad etib, hajm cheklovini ko'rsatuvchi xato xabarini ko'rsatishi kerak.
7. THE **InputComponent**, **SelectComponent**, **DatepickerComponent**, **FileUploadComponent** SHALL Angular `ControlValueAccessor` interfeysini implement qilishi kerak.
8. WHEN `FormControl.disabled` holati o'zgarsa, THE **InputComponent** SHALL `setDisabledState` metodi orqali vizual disabled holatini yangilashi kerak.

---

### Talab 6: Modal Tizimi

**Foydalanuvchi tarixi:** Dasturchi sifatida men `ModalService` orqali dasturiy tarzda modal oynalarni ochishni xohlayman, shunda har bir sahifada modal HTML ni qayta yozmasdan turib foydalana olaman.

#### Qabul qilish mezonlari

1. THE **ModalService** SHALL `open(component, config)` metodini taqdim etishi kerak, bu metod berilgan komponentni modal ichida dinamik render qilishi kerak.
2. THE **ModalService** SHALL `confirm(config)` metodini taqdim etishi kerak, bu metod `Promise<boolean>` qaytarishi kerak.
3. WHEN `ConfirmModal` da "Tasdiqlash" tugmasi bosilsa, THE **ModalService** SHALL `confirm()` Promise ni `true` bilan hal qilishi kerak.
4. WHEN `ConfirmModal` da "Bekor qilish" tugmasi bosilsa yoki backdrop bosilsa, THE **ModalService** SHALL `confirm()` Promise ni `false` bilan hal qilishi kerak.
5. THE **UI_Library** SHALL `title`, `size: 'sm'|'md'|'lg'|'xl'`, `closable` inputlarini qabul qiluvchi `ModalComponent` ni eksport qilishi kerak.
6. WHEN `ModalComponent.closable` `true` bo'lsa va foydalanuvchi `Escape` tugmasini bossa, THE **ModalComponent** SHALL modalni yopishi kerak.
7. WHILE modal ochiq bo'lsa, THE **ModalComponent** SHALL sahifa asosiy kontentining scrollini bloklashi kerak.
8. THE **UI_Library** SHALL `DynamicForm` ni modal ichida ko'rsatuvchi `FormModalComponent` ni eksport qilishi kerak.

---

### Talab 7: Bildirishnoma Tizimi

**Foydalanuvchi tarixi:** Dasturchi sifatida men `NotificationService` orqali toast va alert xabarlarini ko'rsatishni xohlayman, shunda foydalanuvchiga amallar natijasini tezda bildira olaman.

#### Qabul qilish mezonlari

1. THE **NotificationService** SHALL `success(message)`, `error(message)`, `warning(message)`, `info(message)` metodlarini taqdim etishi kerak.
2. WHEN `NotificationService.success()` chaqirilsa, THE **NotificationService** SHALL ekranning yuqori o'ng burchagida yashil rangdagi toast xabarini ko'rsatishi kerak.
3. WHEN toast xabari ko'rsatilsa, THE **NotificationService** SHALL 4000ms dan so'ng xabarni avtomatik yopishi kerak.
4. THE **UI_Library** SHALL `type: 'success'|'error'|'warning'|'info'`, `message`, `dismissible` inputlarini qabul qiluvchi `AlertComponent` ni eksport qilishi kerak.
5. WHEN `AlertComponent.dismissible` `true` bo'lsa va foydalanuvchi yopish tugmasini bossa, THE **AlertComponent** SHALL `dismissed` EventEmitter ni chiqarishi kerak.
6. THE **UI_Library** SHALL `message`, `retryAction` inputlarini qabul qiluvchi `ErrorMessageComponent` ni eksport qilishi kerak.
7. WHEN `ErrorMessageComponent.retryAction` berilsa va foydalanuvchi "Qayta urinish" tugmasini bossa, THE **ErrorMessageComponent** SHALL `retryAction` funksiyasini chaqirishi kerak.
8. THE **NotificationService** SHALL bir vaqtda maksimal 5 ta toast ko'rsatishi kerak; yangi toast kelganda eng eskisini olib tashlashi kerak.

---

### Talab 8: Filtr va Qidiruv Tizimi

**Foydalanuvchi tarixi:** Dasturchi sifatida men tayyor qidiruv va filtr komponentlaridan foydalanishni xohlayman, shunda backend API ga to'g'ri `FilterQuery` parametrlarini yuborishni osonlashtira olaman.

#### Qabul qilish mezonlari

1. THE **UI_Library** SHALL `placeholder`, `debounceMs` inputlarini qabul qiluvchi `SearchInputComponent` ni eksport qilishi kerak.
2. WHEN `SearchInputComponent` da matn kiritilsa, THE **SearchInputComponent** SHALL `debounceMs` (standart: 300ms) kutib, `searchChange` EventEmitter orqali qidiruv qatorini chiqarishi kerak.
3. THE **UI_Library** SHALL `FilterConfig[]` inputini qabul qiluvchi `FilterPanelComponent` ni eksport qilishi kerak.
4. WHEN `FilterPanelComponent` da filtr qiymatlari o'zgarsa, THE **FilterPanelComponent** SHALL `filterChange` EventEmitter orqali `FilterQuery` obyektini chiqarishi kerak.
5. THE **FilterPanelComponent** SHALL `reset()` metodini taqdim etishi kerak, bu metod barcha filtr qiymatlarini boshlang'ich holatga qaytarishi kerak.
6. WHERE `FilterConfig.type` `'date-range'` bo'lsa, THE **FilterPanelComponent** SHALL boshlanish va tugash sanalarini tanlash imkonini beruvchi ikki `DatepickerComponent` ni ko'rsatishi kerak.
7. THE **FilterPanelComponent** SHALL `SearchInputComponent` bilan birgalikda ishlatilganda umumiy `FilterQuery` obyektini birlashtirishi kerak.

---

### Talab 9: Dinamik Menyu Tizimi (Rol Asosida)

**Foydalanuvchi tarixi:** Dasturchi sifatida men menyu elementlarini konfiguratsiya yoki backenddan yuklab, foydalanuvchi roliga qarab ko'rsatishni xohlayman, shunda har bir rol uchun alohida menyu komponenti yozmasdan turib boshqara olaman.

#### Qabul qilish mezonlari

1. THE **MenuService** SHALL `getMenuItems(role: string): Observable<MenuItem[]>` metodini taqdim etishi kerak.
2. WHERE `MenuItem.source` `'backend'` bo'lsa, THE **MenuService** SHALL `/api/menu?role={role}` endpointidan menyu elementlarini yuklab olishi kerak.
3. WHERE `MenuItem.source` `'static'` bo'lsa, THE **MenuService** SHALL konfiguratsiya faylidan menyu elementlarini qaytarishi kerak.
4. THE **UI_Library** SHALL `MenuService` dan olingan `MenuItem[]` ni render qiluvchi `SidebarMenuComponent` ni eksport qilishi kerak.
5. WHEN `AuthService.currentRole` o'zgarsa, THE **SidebarMenuComponent** SHALL menyu elementlarini yangilab, faqat yangi rolga mos elementlarni ko'rsatishi kerak.
6. WHERE `MenuItem.roles` berilsa, THE **SidebarMenuComponent** SHALL faqat `AuthService.currentRole` mos keladigan elementlarni ko'rsatishi kerak.
7. WHEN `MenuItem.children` massivi mavjud bo'lsa, THE **SidebarMenuComponent** SHALL kengaytiriladigan submenu ko'rsatishi kerak.
8. IF `MenuService` backend so'rovi muvaffaqiyatsiz bo'lsa, THEN THE **MenuService** SHALL statik konfiguratsiyaga qaytishi kerak.

---

### Talab 10: Holat Komponentlari

**Foydalanuvchi tarixi:** Dasturchi sifatida men yuklanish, bo'sh holat va xato holatlarini standart komponentlar orqali ko'rsatishni xohlayman, shunda har bir sahifada ushbu holatlarni qayta yozmasdan turib izchil UX ta'minlay olaman.

#### Qabul qilish mezonlari

1. THE **UI_Library** SHALL `size: 'sm'|'md'|'lg'`, `text` inputlarini qabul qiluvchi `LoadingComponent` ni eksport qilishi kerak.
2. THE **UI_Library** SHALL `title`, `description`, `icon`, `action` inputlarini qabul qiluvchi `EmptyStateComponent` ni eksport qilishi kerak.
3. THE **UI_Library** SHALL `title`, `message`, `retryAction` inputlarini qabul qiluvchi `ErrorStateComponent` ni eksport qilishi kerak.
4. THE **UI_Library** SHALL `steps: StepConfig[]`, `currentStep` inputlarini qabul qiluvchi `StepperComponent` ni eksport qilishi kerak.
5. WHEN `StepperComponent` da "Keyingi" tugmasi bosilsa, THE **StepperComponent** SHALL joriy qadamni validatsiya qilib, muvaffaqiyatli bo'lsa `stepChange` EventEmitter orqali yangi qadam indeksini chiqarishi kerak.
6. WHEN `StepperComponent` da "Oldingi" tugmasi bosilsa, THE **StepperComponent** SHALL validatsiyasiz oldingi qadamga o'tishi kerak.
7. THE **UI_Library** SHALL `ThemeService` ga bog'langan `ThemeSwitchComponent` ni eksport qilishi kerak.
8. WHEN `ThemeSwitchComponent` bosilsa, THE **ThemeService** SHALL mavzu holatini `'light'` dan `'dark'` ga yoki aksincha o'zgartirishi kerak va `localStorage` ga saqlashi kerak.
9. WHILE `ThemeService.theme$` `'dark'` bo'lsa, THE **ThemeService** SHALL `document.documentElement` ga `dark` klassini qo'shishi kerak.
10. THE **UI_Library** SHALL `PermissionDirective` ni eksport qilishi kerak; bu direktiva `*appHasPermission="'courses:write'"` sintaksisini qo'llab-quvvatlashi kerak.
11. WHEN `PermissionDirective` ga berilgan ruxsat `AuthService.currentPermissions` massivida bo'lmasa, THE **PermissionDirective** SHALL elementni DOM dan olib tashlashi kerak.

---

### Talab 11: Kutubxona Arxitekturasi va Eksport

**Foydalanuvchi tarixi:** Dasturchi sifatida men barcha komponentlarni bitta `index.ts` dan import qilishni xohlayman, shunda kutubxonani loyihaga ulash oson bo'lsin.

#### Qabul qilish mezonlari

1. THE **UI_Library** SHALL barcha komponentlar, servislar va direktivalarni `src/main/webapp/app/shared/ui/index.ts` faylidan eksport qilishi kerak.
2. THE **UI_Library** SHALL har bir komponent uchun TypeScript interfeyslari (`FieldConfig`, `ColumnConfig`, `MenuItem`, `FilterConfig`, `StepConfig`) ni eksport qilishi kerak.
3. THE **UI_Library** SHALL mavjud design system bilan mos kelishi kerak: `bg-white` fon, `green-500` aksent, `gray-600/700` matn, `shadow-sm` soya.
4. THE **UI_Library** SHALL Angular 21 standalone komponent arxitekturasidan foydalanishi kerak; `NgModule` ishlatilmasligi kerak.
5. WHEN kutubxona komponentlari render qilinsa, THE **UI_Library** SHALL Tailwind CSS 4 utility klasslaridan foydalanishi kerak.
