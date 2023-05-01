package global;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @auther kul.paudel
 * @created at 2023-05-01
 */

@Component
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendPinCodeEmail(String recipient,String pinCode) throws MessagingException {
        // Compose the email message
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient);
        helper.setSubject("Your PIN code");
        helper.setText("Your PIN code is " + pinCode);

        // Send the email message
        emailSender.send(message);
    }

}
