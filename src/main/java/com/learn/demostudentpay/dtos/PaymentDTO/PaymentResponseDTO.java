package com.learn.demostudentpay.dtos.PaymentDTO;

import lombok.*;

import java.time.LocalDate;

/**
 * <h1>ResponseDTO (ce que ton API renvoie au client)</h1>
 * <ul>
 *     <li>Contient l’id et les infos enrichies (ex: studentName, createdAt, updatedAt, etc.).</li>
 *     <li>Tu peux cacher certaines données sensibles.</li>
 *     <li>Typiquement utilisé pour GET.</li>
 * </ul>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class PaymentResponseDTO {
    private Long id;
    private LocalDate date;
    private double amount;
    private String type;
    private String status;
    private String file;
    private String studentFullName;
}