import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    static final String user = "lab7votincev@gmail.com";
    static final String host = "smtp.gmail.com";
    static final String password = "var461618";

    public static String sendEmail(String recipient,String token) {
        String response= "";
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Пароль для подтверждения регистрации");
            message.setText(token);
            Transport.send(message);
            response = "Вам отправлено письмо на электронную почту, для подтверждения регистрации введите пароль:";
        } catch (MessagingException e) {
            response ="Не удалось отправить сообщение на вашу почту, возможно вы ввели ее неверно";


        }
        return response;
    }

}
