package com.learn.demostudentpay.controllers;

import com.learn.demostudentpay.entites.Payment;
import com.learn.demostudentpay.entites.PaymentStatus;
import com.learn.demostudentpay.entites.PaymentType;
import com.learn.demostudentpay.entites.Student;
import com.learn.demostudentpay.repositorys.PaymentRepository;
import com.learn.demostudentpay.repositorys.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentRestController {
    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentRestController(StudentRepository studentRepository, PaymentRepository paymentRepository) {
        this.studentRepository = studentRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/all")
    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id).isPresent() ? paymentRepository.findById(id).get() : null;
    }

    @GetMapping("/student/{code}/payments")
    public List<Payment> paymentsByStudentCode(@PathVariable String code) {
        return paymentRepository.findByStudentCode(code);
    }

    @GetMapping("/status")
    public List<Payment> paymentsByStatus(@RequestParam PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }


    @GetMapping("/type")
    public List<Payment> paymentsByType(@RequestParam PaymentType type) {
        return paymentRepository.findByType(type);
    }

    @GetMapping(path = "/paymentFile/{paymentId}")
    public ResponseEntity<Resource> getPaymentFile(@PathVariable Long paymentId) throws IOException {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new FileNotFoundException("Payment not found"));
        Path filePath = Paths.get(payment.getFile());

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Payment not found"+ filePath.toString());
        }
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("Payment not found"+ filePath.toString());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE))
                .body(resource);
    }

    /*
    * Put Methods
    * */
    @PutMapping("/status/update/{id}")
    public Payment updatePaymentById(@RequestParam PaymentStatus paymentStatus, @PathVariable Long id) {
        Payment paymentToUpdate = paymentRepository.findById(id).get();
        paymentToUpdate.setStatus(paymentStatus);
        return paymentRepository.save(paymentToUpdate);
    }

    @PutMapping("/type/update/{id}")
    public Payment updatePaymentTypeById(@RequestParam PaymentType paymentType, @PathVariable Long id) {
        Payment paymentToUpdate = paymentRepository.findById(id).get();
        paymentToUpdate.setType(paymentType);
        return paymentRepository.save(paymentToUpdate);
    }

    /*
    * Post Methods
    * */
    @PostMapping(path = "/newPayment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment createPayment(@RequestParam MultipartFile file, LocalDate date, double amount,
                                 PaymentType paymentType, PaymentStatus paymentStatus, String studentCode) throws IOException {
        // 1. Create new folder to make of if owr file
        Path folderPath = Paths.get(System.getProperty("user.home"), "edd-demo-yousfiData", "Payments");

        // 2. check if folder is existe
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        // 3. Generate safe file name
        String fileName = UUID.randomUUID().toString() + ".pdf";
        Path filePath = folderPath.resolve(fileName);

        // 4. Save file safely
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 5. Retrieve studen
        Student student = studentRepository.findByCode(studentCode);

        // 6. Create my New payment with (date, amount, paymentType, paymentStatus, studentCode)
        Payment payment = Payment.builder()
                .date(date)
                .amount(amount)
                .type(paymentType)
                .status(paymentStatus)
                .student(student)
                .file(filePath.toString())
                .build();

        // 7. save this new payment to DB
        return paymentRepository.save(payment);
    }
}
