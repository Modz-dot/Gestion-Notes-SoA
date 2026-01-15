package com.soa.certificate_service.controller;

import com.soa.certificate_service.model.Certificate;
import com.soa.certificate_service.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Value("C:/Users/lapto/Desktop/L3/S5/Archi_SoA/gestion-notes-SoA(Devoir)/certificate-service/certificat-genere")
    private String storagePath;


    // POST : Générer un relevé de notes
    @PostMapping("/generate")
    public ResponseEntity<?> generateCertificate(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            String semester = request.get("semester").toString();

            Certificate certificate = certificateService.generateCertificate(studentId, semester);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Relevé de notes généré avec succès");
            response.put("certificate", certificate);
            response.put("downloadUrl", "/api/certificates/" + certificate.getId() + "/download");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }


    // GET : Télécharger un relevé PDF
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable Long id) {
        Certificate certificate = certificateService.getCertificateById(id);

        if (certificate == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(storagePath + "/" + certificate.getPdfFileName());

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + certificate.getPdfFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


    // GET : Récupérer tous les certificats
    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates() {
        List<Certificate> certificates = certificateService.getAllCertificates();
        return ResponseEntity.ok(certificates);
    }


    // GET : Récupérer un certificat par ID
    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getCertificateById(@PathVariable Long id) {
        Certificate certificate = certificateService.getCertificateById(id);
        if (certificate != null) {
            return ResponseEntity.ok(certificate);
        }
        return ResponseEntity.notFound().build();
    }


    // GET : Récupérer les certificats d'un étudiant
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Certificate>> getCertificatesByStudent(@PathVariable Long studentId) {
        List<Certificate> certificates = certificateService.getCertificatesByStudent(studentId);
        return ResponseEntity.ok(certificates);
    }

}
