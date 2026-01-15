package com.soa.certificate_service.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String studentNumber;
    private String department;

}
