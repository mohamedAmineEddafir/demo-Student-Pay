package com.learn.demostudentpay.controllers;

import com.learn.demostudentpay.dtos.PaymentDTO.PaymentRequestDTO;
import com.learn.demostudentpay.dtos.PaymentDTO.PaymentResponseDTO;
import com.learn.demostudentpay.entites.Payment;
import com.learn.demostudentpay.entites.PaymentStatus;
import com.learn.demostudentpay.entites.PaymentType;
import com.learn.demostudentpay.services.ServiceImpl.PaymentServiceImpl;
import com.learn.demostudentpay.services.serviceInterface.PaymentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentRestController {

    private final PaymentServiceInterface paymentService;

    @Autowired
    public PaymentRestController(PaymentServiceImpl paymentServiceImpl) {
        this.paymentService = paymentServiceImpl;
    }

    @GetMapping("/all")
    public List<PaymentResponseDTO> getPayments() {
        return paymentService.getPaymentsImpl();
    }
    @GetMapping("/{id}")
    public PaymentResponseDTO getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentByIdImpl(id);
    }

    @GetMapping("/student/{code}/payments")
    public List<PaymentResponseDTO> paymentsByStudentCode(@PathVariable String code) {
        return paymentService.paymentsByStudentCodeImpl(code);
    }

    @GetMapping("/status")
    public List<PaymentResponseDTO> paymentsByStatus(@RequestParam PaymentStatus status) {
        return paymentService.paymentsByStatusImpl(status);
    }

    @GetMapping("/type")
    public List<PaymentResponseDTO> paymentsByType(@RequestParam PaymentType type) {
        return paymentService.paymentsByTypeImpl(type);
    }

    @GetMapping(path = "/paymentFile/{paymentId}")
    public ResponseEntity<Resource> getPaymentFile(@PathVariable Long paymentId) throws IOException {
        return this.paymentService.showPaymentFileContentImpl(paymentId);
    }

    /*
        Put Methods
     */
    @PutMapping("/status/update/{id}")
    public PaymentResponseDTO updatePaymentById(@RequestParam PaymentStatus paymentStatus, @PathVariable Long id) {
        return this.paymentService.updatePaymentStatusImpl(paymentStatus, id);
    }

    @PutMapping("/type/update/{id}")
    public PaymentResponseDTO updatePaymentTypeById(@RequestParam PaymentType paymentType, @PathVariable Long id) {
        return this.paymentService.updatePaymentTypeImpl(paymentType, id);
    }

    /*
        Post Methods
    */
    @PostMapping(path = "/newPayment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PaymentRequestDTO createPayment(@RequestParam PaymentRequestDTO paymentRequestDTO, @RequestParam MultipartFile file) throws IOException {

        return this.paymentService.savePaymentImpl(paymentRequestDTO ,file);
    }
}
