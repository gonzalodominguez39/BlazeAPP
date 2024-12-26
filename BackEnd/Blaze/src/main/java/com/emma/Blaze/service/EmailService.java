package com.emma.Blaze.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String userName) throws MessagingException {
        String subject = "¡Bienvenido a Blaze!";
        String content = generateWelcomeEmailContent(userName);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true para usar HTML

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true); // true indica que el contenido es HTML

        mailSender.send(message);
    }

    private String generateWelcomeEmailContent(String userName) {
        return "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Bienvenido a Blaze</title><style>body{font-family:Arial,sans-serif;background-color:#f7f7f7;padding:20px;margin:0}.email-container{background-color:black;border-radius:8px;padding:20px;max-width:600px;margin:0 auto;border:2px solid purple;color:white}.email-header{font-size:24px;font-weight:bold;color:white;margin-bottom:20px}.email-body{font-size:16px;color:white;line-height:1.5}.email-body p{margin:15px 0}.btn{display:block;padding:10px 20px;width:200px;font-size:16px;background-color:purple;color:black;text-decoration:none;border-radius:5px;margin-top:20px;text-align:center;margin:0 auto}.footer{font-size:12px;color:white;margin-top:20px;text-align:center}.footer p{margin:5px 0}</style></head><body><div class=\"email-container\"><div class=\"email-header\">¡Bienvenido a Blaze, <span id=\"username\">"+userName+"</span>!</div><div class=\"email-body\"><p>Gracias por registrarte en Blaze. Estamos emocionados de tenerte con nosotros.</p><p>Para comenzar a explorar nuestra plataforma, simplemente haz clic en el siguiente enlace para confirmar tu cuenta:</p><a href=\"[Enlace de Confirmación]\" class=\"btn\">Confirmar Cuenta</a></div><div class=\"footer\"><p>Si tienes alguna pregunta, no dudes en ponerte en contacto con nosotros.</p><p>&copy; 2024 Blaze. Todos los derechos reservados.</p></div></div></body></html>";

    }
}
