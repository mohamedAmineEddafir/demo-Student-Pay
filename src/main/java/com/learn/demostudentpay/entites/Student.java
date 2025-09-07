package com.learn.demostudentpay.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // This tells Jackson to skip proxy's fields.
@Entity
@NoArgsConstructor()
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
public class Student {
    @Id
    private String Id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String code;
    private String programID;
    private String Photo;
}
