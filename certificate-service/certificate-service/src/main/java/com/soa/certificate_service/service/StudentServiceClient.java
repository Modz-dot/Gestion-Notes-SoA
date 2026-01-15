package com.soa.certificate_service.service;

import com.soa.certificate_service.model.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StudentServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${student.service.url}")
    private String studentServiceUrl;


    // Appeler Student Service pour récupérer un étudiant
    public StudentDTO getStudentById(Long studentId) {
        String url = studentServiceUrl + "/api/students/" + studentId;
        System.out.println("Appel REST vers Student Service : " + url);

        try {
            StudentDTO student = restTemplate.getForObject(url, StudentDTO.class);
            System.out.println("Étudiant récupéré : " + student.getFirstName() + " " + student.getLastName());
            return student;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à Student Service : " + e.getMessage());
            return null;
        }
    }


}
