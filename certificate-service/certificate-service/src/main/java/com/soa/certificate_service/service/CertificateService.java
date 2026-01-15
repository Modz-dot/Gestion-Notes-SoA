package com.soa.certificate_service.service;

// IMPORTS CORRECTS pour iText7
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import com.soa.certificate_service.model.*;
import com.soa.certificate_service.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private StudentServiceClient studentServiceClient;

    @Autowired
    private GradeServiceClient gradeServiceClient;

    @Value("C:/Users/lapto/Desktop/L3/S5/Archi_SoA/gestion-notes-SoA(Devoir)/certificate-service/certificat-genere")
    private String storagePath;


    // GÉNÉRER UN RELEVÉ DE NOTES (LA FONCTION PRINCIPALE)
    public Certificate generateCertificate(Long studentId, String semester) {
        System.out.println("Génération du relevé de notes pour étudiant " + studentId + ", semestre " + semester);

        // 1. Appeler Student Service pour récupérer l'étudiant
        StudentDTO student = studentServiceClient.getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("Étudiant non trouvé avec ID: " + studentId);
        }

        // 2. Appeler Grade Service pour récupérer les notes
        List<GradeDTO> grades = gradeServiceClient.getGradesByStudentAndSemester(studentId, semester);
        if (grades == null || grades.isEmpty()) {
            throw new RuntimeException("Aucune note trouvée pour cet étudiant et ce semestre");
        }

        // 3. Appeler Grade Service pour récupérer la moyenne
        AverageResultDTO averageResult = gradeServiceClient.getAverage(studentId, semester);
        if (averageResult == null) {
            throw new RuntimeException("Impossible de calculer la moyenne");
        }



        // 4. Générer le PDF
        String pdfFileName = generatePDF(student, grades, averageResult, semester);

        // 5. Sauvegarder le certificat en base
        Certificate certificate = new Certificate();
        certificate.setStudentId(studentId);
        certificate.setSemester(semester);
        certificate.setPdfFileName(pdfFileName);
        certificate.setAverage(averageResult.getAverage());
        certificate.setStatus(averageResult.getStatus());
        certificate.setGeneratedDate(LocalDateTime.now());

        Certificate saved = certificateRepository.save(certificate);
        System.out.println("Certificat généré et sauvegardé avec ID: " + saved.getId());

        return saved;
    }



    // GÉNÉRER LE PDF
    private String generatePDF(StudentDTO student, List<GradeDTO> grades,
                               AverageResultDTO averageResult, String semester) {
        try {
            // Créer le dossier s'il n'existe pas
            File directory = new File(storagePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Nom du fichier PDF
            String fileName = "releve_" + student.getStudentNumber() + "_" + semester + "_"
                    + System.currentTimeMillis() + ".pdf";
            String filePath = storagePath + "/" + fileName;

            // Créer le PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);


            // Créer les fonts (CORRECT pour iText7)
            PdfFont boldFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);


            // Titre
            Paragraph title = new Paragraph("RELEVÉ DE NOTES")
                    .setFont(boldFont)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Ligne de séparation
            document.add(new Paragraph("\n"));


            // Informations étudiant
            document.add(new Paragraph("Informations Étudiant")
                    .setFont(boldFont)
                    .setFontSize(14));

            document.add(new Paragraph("Nom : " + student.getFirstName() + " " + student.getLastName())
                    .setFont(normalFont));
            document.add(new Paragraph("Numéro étudiant : " + student.getStudentNumber())
                    .setFont(normalFont));
            document.add(new Paragraph("Email : " + student.getEmail())
                    .setFont(normalFont));
            document.add(new Paragraph("Département : " + student.getDepartment())
                    .setFont(normalFont));
            document.add(new Paragraph("Semestre : " + semester)
                    .setFont(normalFont));
            document.add(new Paragraph("Date de génération : " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")))
                    .setFont(normalFont));

            document.add(new Paragraph("\n"));

            // Tableau des notes
            document.add(new Paragraph("Détail des Notes")
                    .setFont(boldFont)
                    .setFontSize(14));

            document.add(new Paragraph("\n"));


            Table table = new Table(4);
            // En-têtes du tableau (avec fond gris et texte en gras)
            table.addHeaderCell(new Cell().add(new Paragraph("Matière").setFont(boldFont))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Note /20").setFont(boldFont))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Coefficient").setFont(boldFont))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Points").setFont(boldFont))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));


            for (GradeDTO grade : grades) {
                table.addCell(grade.getSubject());
                table.addCell(String.valueOf(grade.getScore()));
                table.addCell(String.valueOf(grade.getCoefficient()));
                table.addCell(String.valueOf(grade.getScore() * grade.getCoefficient()));
            }

            document.add(table);

            document.add(new Paragraph("\n"));


            // Résultat
            document.add(new Paragraph("Résultat")
                    .setFont(boldFont)
                    .setFontSize(14));

            document.add(new Paragraph("\n"));


            document.add(new Paragraph("MOYENNE GÉNÉRALE : " + averageResult.getAverage() + " / 20")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));


            // Statut
            String statusText = averageResult.getStatus().equals("PASSED") ? "ADMIS" : "AJOURNÉ";
            document.add(new Paragraph("RÉSULTAT : " + statusText)
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();


            System.out.println("PDF généré : " + filePath);
            return fileName;



        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }



    // Récupérer tous les certificats
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    // Récupérer un certificat par ID
    public Certificate getCertificateById(Long id) {
        return certificateRepository.findById(id).orElse(null);
    }

    // Récupérer les certificats d'un étudiant
    public List<Certificate> getCertificatesByStudent(Long studentId) {
        return certificateRepository.findByStudentId(studentId);
    }

}
