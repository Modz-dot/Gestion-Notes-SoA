package com.soa.certificate_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    @Column(nullable = false)
    private String pdfFileName;

    private Double average;
    private String status;

}
