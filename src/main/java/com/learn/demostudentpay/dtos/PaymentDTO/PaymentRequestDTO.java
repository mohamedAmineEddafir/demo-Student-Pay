package com.learn.demostudentpay.dtos.PaymentDTO;

import lombok.*;

import java.time.LocalDate;

/**
 * <h1>RequestDTO (ce que le client envoie à ton API)</h1>
 * <ul>
 *     <li>Ne contient jamais d’id généré par la DB.</li>
 *     <li>Ne contient pas des infos calculées côté serveur.</li>
 *     <li>Typiquement utilisé pour POST et PUT.</li>
 * </ul>
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
public class PaymentRequestDTO {
    private LocalDate dateOfPayment;
    private double amountOfPayment;
    private String paymentType;
    private String paymentStatus;
    private String paymentFilePdf;
    private String studentId;
}
