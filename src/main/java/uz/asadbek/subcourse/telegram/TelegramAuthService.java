//package uz.asadbek.subcourse.telegram;
//
//import it.tdlight.client.AuthenticationSupplier;
//import it.tdlight.client.SimpleTelegramClient;
//import it.tdlight.client.SimpleTelegramClientFactory;
//import it.tdlight.client.TDLibSettings;
//import it.tdlight.jni.TdApi;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.Scanner;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class TelegramAuthService {
//
//    private final SimpleTelegramClientFactory factory;
//    private final TDLibSettings settings;
//
//    @Value("${telegram.phone}")
//    private String phoneNumber;
//
//    private SimpleTelegramClient client;
//
//    private final Scanner scanner = new Scanner(System.in);
//
//    @PostConstruct
//    public void init() {
//        log.info("🚀 Telegram Userbot ishga tushmoqda...");
//
//        try {
//            this.client = factory.builder(settings)
//                .build(AuthenticationSupplier.user(phoneNumber));
//
//            setupAuthHandler();
//
//            log.info("⏳ Login jarayoni boshlandi...");
//
//        } catch (Exception e) {
//            log.error("❌ Telegram init xato: ", e);
//        }
//    }
//
//    private void setupAuthHandler() {
//        client.addUpdateHandler(TdApi.UpdateAuthorizationState.class, update -> {
//
//            TdApi.AuthorizationState state = update.authorizationState;
//
//            // 📱 PHONE
//            if (state instanceof TdApi.AuthorizationStateWaitPhoneNumber) {
//                log.info("📱 Telefon yuborilmoqda: {}", phoneNumber);
//
//                client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null));
//            }
//
//            else if (state instanceof TdApi.AuthorizationStateWaitCode) {
//                log.warn("🔐 SMS kod kiriting:");
//
//                new Thread(() -> {
//                    try {
//                        String code = scanner.nextLine();
//                        client.send(new TdApi.CheckAuthenticationCode(code));
//                    } catch (Exception e) {
//                        log.error("Kod yuborishda xatolik", e);
//                    }
//                }).start();
//            }
//
//            else if (state instanceof TdApi.AuthorizationStateWaitPassword) {
//                log.warn("🔑 2FA parol kiriting:");
//
//                new Thread(() -> {
//                    try {
//                        String password = scanner.nextLine();
//                        client.send(new TdApi.CheckAuthenticationPassword(password));
//                    } catch (Exception e) {
//                        log.error("Password yuborishda xatolik", e);
//                    }
//                }).start();
//            }
//
//            else if (state instanceof TdApi.AuthorizationStateReady) {
//                log.info("✅ Telegramga muvaffaqiyatli kirdingiz!");
//            }
//
//            // ❌ CLOSED
//            else if (state instanceof TdApi.AuthorizationStateClosed) {
//                log.warn("❌ Session yopildi");
//            }
//        });
//    }
//
//    public SimpleTelegramClient getClient() {
//        if (client == null) {
//            throw new IllegalStateException("Telegram client hali tayyor emas!");
//        }
//        return client;
//    }
//
//    @PreDestroy
//    public void stop() {
//        if (client != null) {
//            try {
//                log.info("🛑 Telegram client yopilmoqda...");
//                client.close();
//            } catch (Exception e) {
//                log.error("Yopishda xatolik", e);
//            }
//        }
//    }
//}
