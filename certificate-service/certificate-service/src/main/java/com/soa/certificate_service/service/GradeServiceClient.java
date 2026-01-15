package com.soa.certificate_service.service;

import com.soa.certificate_service.model.GradeDTO;
import com.soa.certificate_service.model.AverageResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GradeServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${grade.service.url}")
    private String gradeServiceUrl;


    // Récupérer les notes d'un étudiant pour un semestre
    public List<GradeDTO> getGradesByStudentAndSemester(Long studentId, String semester) {
        String url = gradeServiceUrl + "/api/grades/student/" + studentId + "/semester/" + semester;
        System.out.println("Appel REST vers Grade Service : " + url);

        try {
            ResponseEntity<List<GradeDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<GradeDTO>>() {}
            );
            List<GradeDTO> grades = response.getBody();
            System.out.println("Notes récupérées : " + (grades != null ? grades.size() : 0) + " notes");
            return grades;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à Grade Service : " + e.getMessage());
            return null;
        }
    }



    // Récupérer la moyenne d'un étudiant pour un semestre
    public AverageResultDTO getAverage(Long studentId, String semester) {
        String url = gradeServiceUrl + "/api/grades/student/" + studentId + "/semester/" + semester + "/average";
        System.out.println("Appel REST vers Grade Service pour moyenne : " + url);

        try {
            AverageResultDTO average = restTemplate.getForObject(url, AverageResultDTO.class);
            System.out.println("Moyenne récupérée : " + (average != null ? average.getAverage() : "N/A"));
            return average;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à Grade Service : " + e.getMessage());
            return null;
        }
    }


}
