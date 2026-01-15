package com.soa.certificate_service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class AverageResultDTO {
    private Long studentId;
    private String semester;
    private Double average;
    private String status;
    private int totalGrades;
}
