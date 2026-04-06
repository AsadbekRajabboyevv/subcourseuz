package uz.asadbek.subcourse.user;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
