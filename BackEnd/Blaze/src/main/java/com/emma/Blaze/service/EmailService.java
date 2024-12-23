package com.emma.Blaze.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Bienvenido a Blaze");
        message.setText("Hola " + userName + ",\n\n" +
                "¡Gracias por registrarte en Blaze! Estamos emocionados de que te unas a nuestra comunidad.\n\n" +
                "Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos.\n\n" +
                "Saludos,\nEl equipo de Blaze");

        mailSender.send(message);
    }
}
