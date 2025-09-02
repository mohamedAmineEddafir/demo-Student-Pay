package com.learn.demostudentpay.services.serviceInterface;

import com.learn.demostudentpay.entites.Payment;
import com.learn.demostudentpay.entites.PaymentStatus;
import com.learn.demostudentpay.entites.PaymentType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public interface PaymentServiceInterface {

    List<Payment> getPaymentsImpl();

    Payment getPaymentByIdImpl(Long id);

    List<Payment> paymentsByStudentCodeImpl(String code);

    List<Payment> paymentsByStatusImpl(PaymentStatus status);

    List<Payment> paymentsByTypeImpl(PaymentType type);

    ResponseEntity<Resource> showPaymentFileContentImpl(Long paymentId) throws IOException;

    Payment updatePaymentStatusImpl(PaymentStatus paymentStatus, Long id);

    Payment updatePaymentTypeImpl(PaymentType paymentType, Long id);

    Payment savePaymentImpl(MultipartFile file,
                        LocalDate date,
                        double amount,
                        PaymentType paymentType,
                        PaymentStatus paymentStatus,
                        String studentCode) throws IOException;
}
