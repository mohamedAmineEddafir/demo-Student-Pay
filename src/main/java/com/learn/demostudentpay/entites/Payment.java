package com.learn.demostudentpay.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private double amount;
    @Enumerated(EnumType.ORDINAL)
    private PaymentType type;
    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus status;
    private String file;
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;
}
