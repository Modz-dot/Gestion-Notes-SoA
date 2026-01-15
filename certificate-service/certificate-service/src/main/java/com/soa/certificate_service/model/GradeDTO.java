package com.soa.certificate_service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class GradeDTO {
    private Long id;
    private Long studentId;
    private String subject;
    private Double score;
    private Double coefficient;
    private String semester;
    private String examType;

}
