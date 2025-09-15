package com.learn.demostudentpay.dtos.StudentDTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class StudentRequestDTO {
    private String firstName;
    private String lastName;
    private String studentIdentity;
    private String programID;
    private String photo;
}
