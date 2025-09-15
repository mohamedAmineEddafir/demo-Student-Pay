package com.learn.demostudentpay.services.serviceInterface;

import com.learn.demostudentpay.dtos.PaymentDTO.PaymentRequestDTO;
import com.learn.demostudentpay.dtos.PaymentDTO.PaymentResponseDTO;
import com.learn.demostudentpay.entites.PaymentStatus;
import com.learn.demostudentpay.entites.PaymentType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface PaymentServiceInterface {

    List<PaymentResponseDTO> getPaymentsImpl();

    PaymentResponseDTO getPaymentByIdImpl(Long id);

    List<PaymentResponseDTO> paymentsByStudentCodeImpl(String code);

    List<PaymentResponseDTO> paymentsByStatusImpl(PaymentStatus status);

    List<PaymentResponseDTO> paymentsByTypeImpl(PaymentType type);

    ResponseEntity<Resource> showPaymentFileContentImpl(Long paymentId) throws IOException;

    PaymentResponseDTO updatePaymentStatusImpl(PaymentStatus paymentStatus, Long id);

    PaymentResponseDTO updatePaymentTypeImpl(PaymentType paymentType, Long id);

    PaymentRequestDTO savePaymentImpl(PaymentRequestDTO PaymentRequestDTO , MultipartFile file)throws IOException;
}
