import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-terms-of-service',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="max-w-4xl mx-auto px-4 sm:px-6 py-8 sm:py-12 font-serif">
      <header class="border-b-2 border-teal-600 pb-6 mb-8">
        <div
          class="inline-block bg-teal-50 text-teal-700 font-mono text-[11px] tracking-wider px-3 py-1.5 rounded-md mb-4 uppercase">
          <span i18n="@@terms.badge">Rasmiy hujjat</span>
        </div>
        <h1
          class="text-3xl sm:text-4xl font-bold tracking-tight text-gray-900 dark:text-white mb-3">
          <span i18n="@@terms.title">Foydalanish Shartlari</span>
        </h1>
        <p class="font-mono text-sm text-gray-500 dark:text-gray-400">
          <span i18n="@@terms.last_updated">Oxirgi yangilanish</span>: 11 May 2026
        </p>
      </header>

      <!-- Section 1 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section1.title">1. Umumiy qoidalar</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section1.text1">
          Ushbu Foydalanish Shartlari veb-ilovamizdan foydalanish tartibi va qoidalarini belgilaydi.
          Ilovadan ro'yxatdan o'tgan yoki foydalanuvchi sifatida kirgan har bir shaxs ushbu
          shartlarni to'liq qabul qilgan hisoblanadi.
        </p>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section1.text2">
          Ilova barcha yoshdagi foydalanuvchilar uchun mo'ljallangan. 18 yoshdan kichik
          foydalanuvchilar ota-onasi yoki qonuniy vakili roziligi asosida foydalanishlari mumkin.
        </p>
        <div
          class="bg-teal-50 dark:bg-teal-950/30 border-l-4 border-teal-600 rounded-r-lg p-4 my-4 text-teal-800 dark:text-teal-300"
          i18n="@@terms.section1.callout">
          Ushbu shartlarga rozi bo'lmasangiz, ilovadan foydalanishni to'xtatishingiz so'raladi.
        </div>
      </section>

      <!-- Section 2 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section2.title">2. Ro'yxatdan o'tish va hisob yaratish</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section2.text1">
          Ro'yxatdan o'tish ikki usulda amalga oshiriladi:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300 mb-3">
          <li><strong i18n="@@terms.section2.item1_title">Google OAuth2 orqali:</strong> <span
            i18n="@@terms.section2.item1_text">Google hisobingiz bilan bir bosishda kirish va ro'yxatdan o'tish</span>
          </li>
          <li><strong i18n="@@terms.section2.item2_title">Elektron pochta orqali:</strong> <span
            i18n="@@terms.section2.item2_text">Email, parol, ism, familiya, tug'ilgan sana, bio, telefon raqami va lavozim (talaba / o'qituvchi / o'quvchi / boshqa) kiritiladi</span>
          </li>
        </ul>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section2.text2">
          Email orqali ro'yxatdan o'tganda elektron pochtangizga tasdiqlash tokeni yuboriladi.
          Hisobingiz faqat ushbu tokenni tasdiqlashingizdan so'ng faollashadi.
        </p>
        <div
          class="bg-amber-50 dark:bg-amber-950/30 border-l-4 border-amber-600 rounded-r-lg p-4 my-4">
          <strong class="text-amber-800 dark:text-amber-400"><span
            i18n="@@terms.section2.warning_title">Muhim</span>:</strong>
          <span class="text-amber-700 dark:text-amber-300" i18n="@@terms.section2.warning_text"> Ro'yxatdan o'tishda to'g'ri va haqiqiy ma'lumotlar kiritilishi shart. Yolg'on yoki boshqa odamning ma'lumotlari bilan ro'yxatdan o'tish taqiqlanadi va hisobning bloklanishiga olib keladi.</span>
        </div>
      </section>

      <!-- Section 3 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section3.title">3. Hisob xavfsizligi va mas'uliyat</span>
        </h2>
        <div class="bg-red-50 dark:bg-red-950/30 border-l-4 border-red-600 rounded-r-lg p-4 my-4">
          <strong class="text-red-800 dark:text-red-400"><span
            i18n="@@terms.section3.callout_title">Qat'iy taqiq</span>:</strong>
          <span class="text-red-700 dark:text-red-300" i18n="@@terms.section3.callout_text"> Hisobingizni (parol, kirish ma'lumotlari) boshqa shaxslarga berish yoki ulashish mutlaqo man etiladi. Bu qoida buzilsa, hisob darhol bloklanadi va huquqiy choralar ko'rilishi mumkin.</span>
        </div>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section3.text1">
          Foydalanuvchi o'z hisobi ostida amalga oshirilgan barcha harakatlar uchun shaxsan
          mas'uldir. Hisobingizga ruxsatsiz kirish holati aniqlansa, darhol ilova ma'muriga xabar
          bering.
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300">
          <li i18n="@@terms.section3.item1">Kuchli va noyob parol tanlang</li>
          <li i18n="@@terms.section3.item2">Parolni muntazam yangilang</li>
          <li i18n="@@terms.section3.item3">Jamoat kompyuterlarida "eslab qol" funksiyasidan
            foydalanmang
          </li>
          <li i18n="@@terms.section3.item4">Parolni hech kimga bermang — ilova xodimlari ham parol
            so'ramaydi
          </li>
        </ul>
      </section>

      <!-- Section 4 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section4.title">4. Hisob to'ldirish qoidalari</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section4.text1">
          Ilova ichidagi xaridlar uchun hisob balansini to'ldirish talab qilinadi. Jarayon:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300 mb-3">
          <li i18n="@@terms.section4.item1">Foydalanuvchi belgilangan admin kartasiga istalgan
            to'lov vositasi orqali pul o'tkazadi
          </li>
          <li i18n="@@terms.section4.item2">To'lov chekining aniq ko'rinadigan skrinshoti tizimga
            yuklanadi
          </li>
          <li i18n="@@terms.section4.item3">Admin so'rovni <strong>eng ko'pi bilan 1 soat
            ichida</strong> ko'rib chiqadi
          </li>
          <li i18n="@@terms.section4.item4">To'lov haqiqiy bo'lsa, shu summa hisobingizga
            qo'shiladi
          </li>
          <li i18n="@@terms.section4.item5">To'lov soxta bo'lsa, hisob bloklanadi va huquqiy chora
            ko'riladi
          </li>
        </ul>
        <div class="bg-red-50 dark:bg-red-950/30 border-l-4 border-red-600 rounded-r-lg p-4 my-4">
          <strong class="text-red-800 dark:text-red-400"><span i18n="@@terms.section4.danger_title">Soxta to'lov — jiddiy qonunbuzarlik</span>:</strong>
          <span class="text-red-700 dark:text-red-300" i18n="@@terms.section4.danger_text"> Tahrirlangan yoki soxta to'lov skrinshoti yuklash aldamchilik hisoblanadi. Bunday hollarda hisob bloklanadi va O'zbekiston Respublikasi jinoyat qonunchiligi asosida javobgarlikka tortiladi.</span>
        </div>
      </section>

      <!-- Section 5 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section5.title">5. Video kurslar va intellektual mulk</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section5.text1">
          Ilovadagi video kurslar resurslar cheklanganligi sababli YouTube platformasida <strong>private
          (maxfiy)</strong> holda joylashtirilgan.
        </p>
        <div class="bg-red-50 dark:bg-red-950/30 border-l-4 border-red-600 rounded-r-lg p-4 my-4">
          <strong class="text-red-800 dark:text-red-400"><span
            i18n="@@terms.section5.callout_title">Qat'iy taqiq — mualliflik huquqi</span>:</strong>
          <ul class="list-disc pl-6 mt-2 space-y-1 text-red-700 dark:text-red-300">
            <li i18n="@@terms.section5.callout_item1">Private video havolalarini boshqalarga
              tarqatish
            </li>
            <li i18n="@@terms.section5.callout_item2">Video kurslarni yuklab olish yoki qayta
              joylash
            </li>
            <li i18n="@@terms.section5.callout_item3">Kurs materiallarini boshqa platformalarga
              ko'chirish
            </li>
            <li i18n="@@terms.section5.callout_item4">Muallif roziligisiz kurs kontentini ulashish
            </li>
          </ul>
        </div>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@terms.section5.text2">
          Ushbu qoidalar O'zbekiston Respublikasining mualliflik huquqi to'g'risidagi qonunchiligi
          bilan birgalikda qo'llaniladi. Qonunbuzarlik aniqlanganda hisob bloklanadi va huquqiy
          chora ko'riladi.
        </p>
      </section>

      <!-- Section 6 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section6.title">6. Taqiqlangan harakatlar</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section6.text1">
          Quyidagi harakatlar qat'iyan taqiqlanadi:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300 mb-3">
          <li i18n="@@terms.section6.item1">Ilovaga ruxsatsiz kirishga urinish (hacking, SQL
            injection, XSS va boshqa hujumlar)
          </li>
          <li i18n="@@terms.section6.item2">Avtomatlashtirilgan botlar yoki skriptlar orqali tizimga
            zo'riqish berish (DDoS)
          </li>
          <li i18n="@@terms.section6.item3">Boshqa foydalanuvchilarning ma'lumotlariga ruxsatsiz
            kirish
          </li>
          <li i18n="@@terms.section6.item4">Ilovaning ishlashiga zarar yetkazuvchi harakatlar</li>
          <li i18n="@@terms.section6.item5">Soxta identifikatsiya yoki boshqani taqlid qilish</li>
          <li i18n="@@terms.section6.item6">Spam, fishing yoki zararli kontent tarqatish</li>
          <li i18n="@@terms.section6.item7">Ilova xodimlarini aldashga urinish</li>
        </ul>
        <div
          class="bg-gray-100 dark:bg-gray-800/50 border border-gray-300 dark:border-gray-700 rounded-lg p-5 my-4">
          <strong class="block mb-2 text-gray-900 dark:text-white"
                  i18n="@@terms.section6.law_title">Huquqiy mas'uliyat:</strong>
          <p class="text-gray-700 dark:text-gray-300 text-sm" i18n="@@terms.section6.law_text">
            Ilovaga nisbatan amalga oshirilgan har qanday kiberhujum, ruxsatsiz penetratsion test
            (pentesting) yoki zarar yetkazuvchi harakatlar O'zbekiston Respublikasi Konstitutsiyasi
            va amaldagi qonunchilik asosida jazoga tortiladi. Zarar yetkazgan shaxs yetkazilgan
            zararni to'liq qoplash majburiyatini oladi.
          </p>
        </div>
      </section>

      <!-- Section 7 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section7.title">7. Hisobni bloklash va tugatish</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section7.text1">
          Quyidagi hollarda foydalanuvchi hisobi ogohlantirmasdan bloklanishi yoki o'chirilishi
          mumkin:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300">
          <li i18n="@@terms.section7.item1">Soxta to'lov skrinshoti yuklash</li>
          <li i18n="@@terms.section7.item2">Hisob ma'lumotlarini uchinchi shaxslarga berish</li>
          <li i18n="@@terms.section7.item3">Taqiqlangan harakatlardan birini sodir etish</li>
          <li i18n="@@terms.section7.item4">Mualliflik huquqini buzish</li>
          <li i18n="@@terms.section7.item5">Tizimga zarar yetkazishga urinish</li>
          <li i18n="@@terms.section7.item6">Boshqa foydalanuvchilarga zarar yetkazish</li>
        </ul>
      </section>

      <!-- Section 8 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section8.title">8. Mas'uliyatni cheklash</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section8.text1">
          Ilova o'z xizmatlarini "mavjud holda" taqdim etadi. Quyidagi holatlar uchun mas'uliyat
          cheklanadi:
        </p>
        <ul class="list-disc pl-6 space-y-2 text-gray-700 dark:text-gray-300 mb-3">
          <li i18n="@@terms.section8.item1">Texnik nosozliklar yoki server to'xtashlari natijasida
            yuzaga kelgan yo'qotishlar
          </li>
          <li i18n="@@terms.section8.item2">Foydalanuvchining o'z hisobini noto'g'ri boshqarishi
            natijasidagi yo'qotishlar
          </li>
          <li i18n="@@terms.section8.item3">Uchinchi tomon (Google, to'lov provayderlari) xizmatlari
            nosozliklari
          </li>
        </ul>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@terms.section8.text2">
          Ilova to'lov jarayoni uchun vositachilik xizmati ko'rsatadi, lekin bank yoki to'lov
          provayderi muammolari uchun mas'ul emas.
        </p>
      </section>

      <!-- Section 9 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section9.title">9. Qo'llaniladigan qonunchilik</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 mb-3 leading-relaxed"
           i18n="@@terms.section9.text1">
          Ushbu Foydalanish Shartlari O'zbekiston Respublikasi qonunchiligi asosida tartibga
          solinadi. Nizolar O'zbekiston Respublikasining vakolatli sudlari orqali hal etiladi.
        </p>
        <div
          class="bg-teal-50 dark:bg-teal-950/30 border-l-4 border-teal-600 rounded-r-lg p-4 my-4 text-teal-800 dark:text-teal-300"
          i18n="@@terms.section9.callout">
          Ilova O'zbekiston Respublikasining "Axborotlashtirish to'g'risida"gi, "Elektron tijorat
          to'g'risida"gi va mualliflik huquqiga oid qonunlarini to'liq hurmat qiladi va ularga rioya
          etadi.
        </div>
      </section>

      <!-- Section 10 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section10.title">10. Shartlarga o'zgartirishlar</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@terms.section10.text1">
          Ushbu Foydalanish Shartlari zarur hollarda yangilanishi mumkin. Muhim o'zgarishlar
          ro'yxatdan o'tgan elektron pochta manzilingizga kamida 7 kun oldin xabar qilinadi.
          Ilovadan foydalanishni davom ettirish yangilangan shartlarni qabul qilishni bildiradi.
        </p>
      </section>

      <!-- Section 11 -->
      <section class="mb-8">
        <h2
          class="text-xl font-bold text-gray-900 dark:text-white mb-3 pl-4 border-l-4 border-teal-600">
          <span i18n="@@terms.section11.title">11. Bog'lanish</span>
        </h2>
        <p class="text-gray-700 dark:text-gray-300 leading-relaxed" i18n="@@terms.section11.text1">
          Foydalanish shartlariga oid savollar, shikoyatlar yoki xabarlar uchun ilova ichidagi
          yordam markazi yoki rasmiy email manzili orqali murojaat qiling. Barcha murojaatlar 24
          soat ichida ko'rib chiqiladi.
        </p>
      </section>

      <!-- Footer -->
      <footer
        class="mt-12 pt-6 border-t border-gray-200 dark:border-gray-800 text-xs font-mono text-gray-500 dark:text-gray-400">
        <p>© {{ currentYear }} <span i18n="@@terms.footer_text">Ilova. Barcha huquqlar himoyalangan. | O'zbekiston Respublikasi qonunchiligi asosida.</span>
        </p>
      </footer>
    </div>
  `
})
export class TermsOfServiceComponent {
  currentYear: number = new Date().getFullYear();
}
