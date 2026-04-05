package com.lp3.elearning.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.StatusEnrollment;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.exception.BusinessRuleException;

@Service
public class CertificateService {

    private final EnrollmentService enrollmentService;

    public CertificateService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public byte[] generateCertificatePdf(Long enrollmentId, Student student) {
        Enrollment enrollment = enrollmentService.findById(enrollmentId);

        if(!enrollment.getStudent().getId().equals(student.getId())){
            throw new BusinessRuleException("Acesso negado: Este certificado pertence a outro aluno.");
        }
        
        if (enrollment.getStatus() != StatusEnrollment.COMPLETED && enrollment.getOverallProgress() < 1.0) {
            throw new BusinessRuleException("O certificado só pode ser emitido após 100% de conclusão do curso.");
        }

        Document document = new Document(PageSize.A4.rotate()); 
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            
            // ... (Layout do PDF mantido igual ao seu original) ...
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 30,  new java.awt.Color(0, 102, 204));
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 18);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

            Paragraph title = new Paragraph("CERTIFICADO DE CONCLUSÃO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(50);
            document.add(title);

            Paragraph body = new Paragraph();
            body.setAlignment(Element.ALIGN_CENTER);
            body.setSpacingBefore(80);
            body.setLeading(30f);
            
            body.add(new Chunk("Certificamos que ", bodyFont));
            body.add(new Chunk(enrollment.getStudent().getName().toUpperCase(), boldFont));
            body.add(new Chunk("\nconcluiu com êxito o curso de\n", bodyFont));
            body.add(new Chunk(enrollment.getCourse().getTitle().toUpperCase(), boldFont));
            body.add(new Chunk("\n\ncom carga horária de " + enrollment.getCourse().getWorkload() + " horas.", bodyFont));
            document.add(body);

            String dataConclusao = enrollment.getEnrollmentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph date = new Paragraph("Data de Emissão: " + dataConclusao, FontFactory.getFont(FontFactory.HELVETICA, 12));
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingBefore(100);
            document.add(date);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF do certificado", e);
        }

        return out.toByteArray();
    }
}