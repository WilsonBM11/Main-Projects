package dev.mail_service.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${email.sender}")
    private String emailUser;
    private String message;
    private String subject;
    private String toUser;

    @Autowired
    private JavaMailSender javaMailSender;

    @JmsListener(destination = "message")
    public void messageListener(String message) {
        this.message = message;
    }
    @JmsListener(destination = "subject")
    public void subjectListener(String subject) {
        this.subject = subject;
    }
    @JmsListener(destination = "toUser")
    public void listener(String toUser) {
        sendEmail(toUser, subject, message);
    }

    public void sendEmail(String toUser, String subject, String message) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            if (toUser == null || toUser.trim().isEmpty()) {
                throw new IllegalArgumentException("El campo 'toUser' no puede ser nulo o vacío");
            }

            helper.setTo(toUser);
            helper.setSubject(subject);
            helper.setText(message, true);

            javaMailSender.send(mimeMessage);

            System.out.println("Correo enviado exitosamente a " + toUser);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }




}
