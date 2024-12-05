package lk.ijse.Green_Shadow_Backend.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.Green_Shadow_Backend.customeObj.MailBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    // Method to load the HTML template and replace multiple placeholders
    private String loadHtmlTemplate(String templateName, Map<String, String> replacements) throws IOException {
        String templatePath = "src/main/resources/templates/" + templateName + ".html";
        String content = new String(Files.readAllBytes(Paths.get(templatePath)));
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String placeholder = "\\{" + entry.getKey() + "\\}";
            content = content.replaceAll(placeholder, entry.getValue());
        }
        return content;
    }
    // Send an HTML email with dynamic values from a template
    public void sendHtmlMessage(MailBody mailBody) throws MessagingException, IOException {
        String htmlContent = loadHtmlTemplate(mailBody.templateName(), mailBody.replacements());
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null; // true indicates multipart
        helper = new MimeMessageHelper(message, true);
        helper.setTo(mailBody.to());
        helper.setFrom(fromEmail);
        helper.setSubject(mailBody.subject());
        helper.setText(htmlContent, true); // true indicates HTML
        javaMailSender.send(message);
        log.info("HTML Mail Sent");
    }
    public Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}