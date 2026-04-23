//package uz.asadbek.subcourse.telegram;
//
//import it.tdlight.Init;
//import it.tdlight.client.APIToken;
//import it.tdlight.client.SimpleTelegramClientFactory;
//import it.tdlight.client.TDLibSettings;
//import it.tdlight.util.UnsupportedNativeLibraryException;
//import jakarta.annotation.PostConstruct;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Slf4j
//@Configuration
//public class TelegramConfig {
//
//    @PostConstruct
//    public void init() {
//        try {
//            log.info("Telegram Userbot inicializatsiya qilinmoqda...");
//
//            // 1. Native kutubxonani yuklash
//            it.tdlight.Init.init();
//
//            // 2. MUHIM: Constructorlarni ro'yxatdan o'tkazish
//            // Agar sizda 'detect' metodi bo'lmasa, quyidagi klassni skaner qiling:
//            it.tdlight.ConstructorDetector.getConstructor(it.tdlight.jni.TdApi.UpdateAuthorizationState.class);
//
//            // Agar yuqoridagi ham xato bersa, ushbu klassni ishlatib ko'ring (versiyaga qarab):
//            // it.tdlight.util.ConstructorDetector.detect(it.tdlight.jni.TdApi.class);
//
//            // 3. Shundan keyingina Builder ni ishga tushiring
//            var factory = new SimpleTelegramClientFactory();
//            // ... qolgan kodlar (builder, settings, build)
//
//        } catch (Exception e) {
//            log.error("❌ Telegramni ishga tushirishda xatolik:", e);
//        }
//    }
//    @Value("${telegram.api-id}")
//    private int apiId;
//
//    @Value("${telegram.api-hash}")
//    private String apiHash;
//
//    @Value("${telegram.session-path}")
//    private String sessionPath;
//
//    @Bean
//    public TDLibSettings tdLibSettings() {
//        var settings = TDLibSettings.create(new APIToken(apiId, apiHash));
//        Path sessionFolder = Paths.get(sessionPath);
//        settings.setDatabaseDirectoryPath(sessionFolder);
//        settings.setDownloadedFilesDirectoryPath(sessionFolder.resolve("downloads"));
//
//        return settings;
//    }
//
//    @Bean
//    public SimpleTelegramClientFactory clientFactory() {
//        return new SimpleTelegramClientFactory();
//    }
//
//}
