package com.learn.demostudentpay.dtos.StudentDTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class StudentResponseDTO {
    private String studentId;
    private String fullName;
    private String code;
    private String programID;
    private String photo;
}
