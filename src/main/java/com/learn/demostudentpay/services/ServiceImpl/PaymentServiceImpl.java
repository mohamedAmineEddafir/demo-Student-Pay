package com.learn.demostudentpay.services.ServiceImpl;

import com.learn.demostudentpay.entites.Payment;
import com.learn.demostudentpay.entites.PaymentStatus;
import com.learn.demostudentpay.entites.PaymentType;
import com.learn.demostudentpay.entites.Student;
import com.learn.demostudentpay.repositorys.PaymentRepository;
import com.learn.demostudentpay.repositorys.StudentRepository;
import com.learn.demostudentpay.services.serviceInterface.PaymentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

@Service
@Transactional
public class PaymentServiceImpl implements PaymentServiceInterface {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Payment> getPaymentsImpl() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentByIdImpl(Long id) {
        return paymentRepository.findById(id).isPresent() ? paymentRepository.findById(id).get() : null;
    }

    @Override
    public List<Payment> paymentsByStudentCodeImpl(String code) {
        return paymentRepository.findByStudentCode(code);
    }

    @Override
    public List<Payment> paymentsByStatusImpl(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    @Override
    public List<Payment> paymentsByTypeImpl(PaymentType type) {
        return paymentRepository.findByType(type);
    }

    @Override
    public ResponseEntity<Resource> showPaymentFileContentImpl(Long paymentId) throws IOException {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new FileNotFoundException("Payment not found"));
        Path filePath = Paths.get(payment.getFile());

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Payment not found" + filePath);
        }
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("Payment not found" + filePath);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE))
                .body(resource);
    }

    @Override
    public Payment updatePaymentStatusImpl(PaymentStatus paymentStatus, Long id) {
        Payment paymentToUpdate = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        paymentToUpdate.setStatus(paymentStatus);
        return paymentRepository.save(paymentToUpdate);
    }

    @Override
    public Payment updatePaymentTypeImpl(@RequestParam PaymentType paymentType, @PathVariable Long id) {
        Payment paymentToUpdate = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payment not found to modify Type"));
        paymentToUpdate.setType(paymentType);
        return paymentRepository.save(paymentToUpdate);
    }

    @Override
    public Payment savePaymentImpl(MultipartFile file, LocalDate date, double amount,
                                   PaymentType paymentType, PaymentStatus paymentStatus, String studentCode) throws IOException {
        // 1. Create new folder to make of if owr file
        Path folderPath = Paths.get(System.getProperty("user.home"), "edd-demo-yousfiData", "Payments");

        // 2. check if folder is existe
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        // 3. Generate safe file name
        String fileName = UUID.randomUUID() + ".pdf";
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
