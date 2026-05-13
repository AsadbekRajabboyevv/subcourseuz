import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-privacy-policy',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="max-w-4xl mx-auto px-4 sm:px-6 py-8 sm:py-12 font-serif">
      <!-- Header -->
      <header class="border-b-2 border-emerald-600 pb-6 mb-8">
        <div class="inline-block bg-emerald-50 text-emerald-700 font-mono text-[11px] tracking-wider px-3 py-1.5 rounded-md mb-4 uppercase">
          <span i18n="@@privacy.badge">Rasmiy hujjat</span>
        </div>
        <h1 class="text-3xl sm:text-4xl font-bold tracking-tight text-gray-900 dark:text-white mb-3">
          <span i18n="@@privacy.title">Maxfiylik Siyosati</span>
        </h1>
        <p class="font-mono text-sm text-gray-500 dark:text-gray-400">
          <span i18n="@@privacy.last_updated">Oxirgi yangilanish</span>: 11 May 2026
        </p>
      </header>

      <!-- Section 1 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section1.title">1. Umumiy ma'lumot</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed" i18n="@@privacy.section1.text1">
          Ushbu Maxfiylik Siyosati veb-ilovamiz foydalanuvchilarining shaxsiy ma'lumotlarini qanday yig'ish, saqlash, qayta ishlash va himoya qilishimizni belgilaydi. Ilovadan foydalanish orqali siz ushbu siyosat shartlarini qabul qilasiz.
        </p>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@privacy.section1.text2">
          Ilovamiz barcha yoshdagi foydalanuvchilarga mo'ljallangan bo'lib, 18 yoshdan kichik foydalanuvchilar ota-onasi yoki qonuniy vasiysi roziligini olgan holda foydalanishlari mumkin.
        </p>
      </section>

      <!-- Section 2 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section2.title">2. To'planadigan ma'lumotlar</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed" i18n="@@privacy.section2.text">
          Ro'yxatdan o'tish va ilova xizmatlari doirasida quyidagi ma'lumotlar to'planadi:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300">
          <li><strong i18n="@@privacy.section2.item1_title">Shaxsiy ma'lumotlar:</strong> <span i18n="@@privacy.section2.item1_text">Ism, familiya, elektron pochta manzili, telefon raqami, tug'ilgan sana</span></li>
          <li><strong i18n="@@privacy.section2.item2_title">Profil ma'lumotlari:</strong> <span i18n="@@privacy.section2.item2_text">Bio (qisqacha ma'lumot), lavozim (talaba, o'qituvchi, o'quvchi, boshqa)</span></li>
          <li><strong i18n="@@privacy.section2.item3_title">Avtorizatsiya ma'lumotlari:</strong> <span i18n="@@privacy.section2.item3_text">Google OAuth2 orqali kirish ma'lumotlari (faqat Googlening maxfiylik siyosati doirasida)</span></li>
          <li><strong i18n="@@privacy.section2.item4_title">To'lov ma'lumotlari:</strong> <span i18n="@@privacy.section2.item4_text">Admin kartasiga o'tkazilgan to'lov cheki skrinshoti (to'lov tizimi integratsiya qilinmagan, manual tekshiruv)</span></li>
          <li><strong i18n="@@privacy.section2.item5_title">Texnik ma'lumotlar:</strong> <span i18n="@@privacy.section2.item5_text">IP-manzil, brauzer turi, qurilma ma'lumotlari, tizimga kirish vaqti</span></li>
        </ul>
      </section>

      <!-- Section 3 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section3.title">3. Ma'lumotlarni saqlash va himoya</span>
        </h2>
        <div class="bg-emerald-50 dark:bg-emerald-950/30 border-l-4 border-emerald-600 rounded-r-lg p-4 my-4 text-emerald-800 dark:text-emerald-300">
          <span i18n="@@privacy.section3.callout">Barcha parollar va maxfiy ma'lumotlar <strong>BCrypt</strong> shifrlash algoritmi orqali kodlanadi. Hech qanday parol ochiq ko'rinishda saqlanmaydi.</span>
        </div>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed" i18n="@@privacy.section3.text1">
          Ma'lumotlaringiz faqat ilovamizning xavfsiz serverlarida saqlanadi. Serverlar ruxsatsiz kirishdan himoyalangan.
        </p>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@privacy.section3.text2">
          Email tasdiqlash tokeni orqali hisobingiz faollashtiriladi — bu email manzilingizning haqiqiyligini ta'minlaydi.
        </p>
      </section>

      <!-- Section 4 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section4.title">4. Ma'lumotlardan foydalanish maqsadi</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed" i18n="@@privacy.section4.text">
          To'plangan ma'lumotlar quyidagi maqsadlarda ishlatiladi:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300">
          <li i18n="@@privacy.section4.item1">Foydalanuvchi hisobini boshqarish va autentifikatsiya</li>
          <li i18n="@@privacy.section4.item2">Hisobni to'ldirish so'rovlarini ko'rib chiqish (admin tomonidan)</li>
          <li i18n="@@privacy.section4.item3">Video kurslar va ilova xizmatlarini taqdim etish</li>
          <li i18n="@@privacy.section4.item4">Texnik yordam va xavfsizlik monitoringi</li>
          <li i18n="@@privacy.section4.item5">O'zbekiston Respublikasi qonunchiligiga muvofiq huquqiy majburiyatlarni bajarish</li>
        </ul>
      </section>

      <!-- Section 5 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section5.title">5. Uchinchi shaxslarga ma'lumot bermaslik</span>
        </h2>
        <div class="bg-red-50 dark:bg-red-950/30 border-l-4 border-red-600 rounded-r-lg p-4 my-4">
          <strong class="text-red-800 dark:text-red-400"><span i18n="@@privacy.section5.callout_title">Qat'iy taqiq</span>:</strong>
          <span class="text-red-700 dark:text-red-300" i18n="@@privacy.section5.callout_text"> Foydalanuvchilarning shaxsiy ma'lumotlari hech qanday uchinchi shaxsga sotilmaydi, o'tkazilmaydi yoki tarqatilmaydi. Bu ilova siyosatining o'zgarmas qoidasidir.</span>
        </div>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@privacy.section5.text">
          Istisno hollar: O'zbekiston Respublikasi qonunchiligiga asosan sud yoki huquqni muhofaza qiluvchi organlarning rasmiy talabi bo'lsa, zarur ma'lumotlar taqdim etilishi mumkin.
        </p>
      </section>

      <!-- Section 6 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section6.title">6. Hisob to'ldirish va to'lov ma'lumotlari</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed" i18n="@@privacy.section6.text">
          Ilovamizda to'lov tizimi bevosita integratsiya qilinmagan. Hisob to'ldirish jarayoni:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300">
          <li i18n="@@privacy.section6.item1">Foydalanuvchi admin kartasiga har qanday to'lov vositasi orqali pul o'tkazadi</li>
          <li i18n="@@privacy.section6.item2">To'lov chekining skrinshoti tizimga yuklanadi</li>
          <li i18n="@@privacy.section6.item3">Admin <strong>1 soat ichida</strong> to'lovni ko'rib chiqadi va tasdiqlaydi</li>
          <li i18n="@@privacy.section6.item4">Tasdiqlangan to'lov miqdori foydalanuvchi hisobiga o'tkaziladi</li>
        </ul>
        <div class="bg-amber-50 dark:bg-amber-950/30 border-l-4 border-amber-600 rounded-r-lg p-4 my-4">
          <strong class="text-amber-800 dark:text-amber-400"><span i18n="@@privacy.section6.warning_title">Ogohlantirish</span>:</strong>
          <span class="text-amber-700 dark:text-amber-300" i18n="@@privacy.section6.warning_text"> Soxta (fake) to'lov skrinshoti yuklash hisobning bloklanishiga olib keladi. Bunday harakatlar aldamchilik sifatida qaraladi va huquqiy javobgarlikni keltirib chiqarishi mumkin.</span>
        </div>
      </section>

      <!-- Section 7 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section7.title">7. Hisobni boshqarish xavfsizligi</span>
        </h2>
        <div class="bg-red-50 dark:bg-red-950/30 border-l-4 border-red-600 rounded-r-lg p-4 my-4">
          <strong class="text-red-800 dark:text-red-400"><span i18n="@@privacy.section7.callout_title">Taqiq</span>:</strong>
          <span class="text-red-700 dark:text-red-300" i18n="@@privacy.section7.callout_text"> Hisobingizni boshqa shaxslarga berish (parol, kirish ma'lumotlarini ulashish) ilovamiz tomonidan qat'iyan man etiladi. Bunday holat aniqlanganda hisob bloklanadi.</span>
        </div>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@privacy.section7.text">
          Har bir foydalanuvchi o'z hisobi xavfsizligi uchun mas'uldur. Parolni muntazam yangilash, ishonchli parol tanlash va uni hech kimga bermaslik tavsiya etiladi.
        </p>
      </section>

      <!-- Section 8 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section8.title">8. Cookie va texnik ma'lumotlar</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed" i18n="@@privacy.section8.text1">
          Ilova tizimga kirish sessiyasini saqlash uchun cookie-fayllardan foydalanadi. Cookie-fayllar brauzer sozlamalarida o'chirib qo'yilishi mumkin, ammo bu ilovaning ishlashiga ta'sir qilishi mumkin.
        </p>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@privacy.section8.text2">
          Google OAuth2 orqali kirish uchun Google'ning o'z maxfiylik siyosati ham qo'llaniladi. Batafsil:
          <a href="https://policies.google.com/privacy" target="_blank" class="text-emerald-600 hover:text-emerald-700 dark:text-emerald-400 underline">Google Privacy Policy</a>.
        </p>
      </section>

      <!-- Section 9 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section9.title">9. Foydalanuvchi huquqlari</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed" i18n="@@privacy.section9.text">
          Siz quyidagi huquqlarga egasiz:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300">
          <li i18n="@@privacy.section9.item1">Shaxsiy ma'lumotlaringizni ko'rish va yangilash (profil sozlamalari orqali)</li>
          <li i18n="@@privacy.section9.item2">Hisobingizni o'chirish so'rovini yuborish</li>
          <li i18n="@@privacy.section9.item3">Ma'lumotlaringiz qayta ishlashiga e'tiroz bildirish</li>
          <li i18n="@@privacy.section9.item4">Texnik yordam xizmati orqali savollar yuborish</li>
        </ul>
      </section>

      <!-- Section 10 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section10.title">10. Siyosatga o'zgartirishlar</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@privacy.section10.text">
          Ushbu Maxfiylik Siyosati vaqt o'tishi bilan yangilanishi mumkin. Muhim o'zgarishlar ro'yxatdan o'tgan email manzilingizga bildiriladi. Ilovadan foydalanishni davom ettirish yangi shartlarga rozilikni bildiradi.
        </p>
      </section>

      <!-- Section 11 -->
      <section class="mb-8">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-emerald-600">
          <span i18n="@@privacy.section11.title">11. Bog'lanish</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@privacy.section11.text">
          Maxfiylik siyosatiga oid savollaringiz bo'lsa, ilovadagi yordam markazi yoki rasmiy email orqali murojaat qilishingiz mumkin.
        </p>
      </section>

      <!-- Footer -->
      <footer class="mt-12 pt-6 border-t border-gray-200 dark:border-gray-800 text-xs font-mono text-gray-500 dark:text-gray-400">
        <p>© {{ currentYear }} <span i18n="@@privacy.footer_text">Ilova. Barcha huquqlar himoyalangan. | O'zbekiston Respublikasi qonunchiligi asosida.</span></p>
      </footer>
    </div>
  `
})
export class PrivacyPolicyComponent {
  currentYear: number = new Date().getFullYear();
}
