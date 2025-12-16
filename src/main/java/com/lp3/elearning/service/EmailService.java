package com.lp3.elearning.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Você pode injetar o remetente a partir da configuração do application.properties
    @Value("${spring.mail.username}")
    private String remetente; 

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envia um email simples de texto.
     * @param destinatario O endereço de email do destinatário.
     * @param assunto O assunto do email.
     * @param corpo O corpo (conteúdo) do email.
     */
    public void sendSimpleMail(String destinatario, String assunto, String corpo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            
            message.setFrom(remetente); // Configurado no application.properties
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(corpo);

            mailSender.send(message);
            System.out.println("E-mail enviado com sucesso para: " + destinatario);

        } catch (Exception e) {
            // Logar ou tratar o erro de envio
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
            // Opcional: relançar como uma exceção de serviço personalizada
            throw new RuntimeException("Erro ao enviar e-mail", e); 
        }
    }
}