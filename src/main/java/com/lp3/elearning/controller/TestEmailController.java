// Exemplo de um Controller de Teste
package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.service.EmailService;

@RestController
public class TestEmailController {

    private final EmailService emailService;

    public TestEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> enviarEmailTeste(@RequestParam("destinatario") String destinatario) {
        
        String assunto = "Teste de Envio de Email com Spring Boot";
        String corpo = "Olá,\n\nEste é um email de teste enviado da sua aplicação Spring Boot!\n\nAtenciosamente,\nSeu Sistema";

        emailService.sendSimpleMail(destinatario, assunto, corpo);

        return ResponseEntity.ok("Solicitação de envio de e-mail iniciada.");
    }
}
