package com.learn.demostudentpay.services.ServiceImpl;

import com.learn.demostudentpay.dtos.PaymentDTO.PaymentRequestDTO;
import com.learn.demostudentpay.dtos.PaymentDTO.PaymentResponseDTO;
import com.learn.demostudentpay.entites.Payment;
import com.learn.demostudentpay.entites.PaymentStatus;
import com.learn.demostudentpay.entites.PaymentType;
import com.learn.demostudentpay.entites.Student;
import com.learn.demostudentpay.mappers.PaymentMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentServiceInterface {

    private final PaymentMapper  paymentMapper;
    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, StudentRepository studentRepository,  PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsImpl() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.toResponseDtoList(payments);
    }

    @Override
    public PaymentResponseDTO getPaymentByIdImpl(Long id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public List<PaymentResponseDTO> paymentsByStudentCodeImpl(String code) {
        List<Payment> payment = paymentRepository.findByStudentCode(code);
        return paymentMapper.toResponseDtoList(payment);
    }

    @Override
    public List<PaymentResponseDTO> paymentsByStatusImpl(PaymentStatus status) {
        List<Payment> payment = paymentRepository.findByStatus(status);
        return paymentMapper.toResponseDtoList(payment);
    }

    @Override
    public List<PaymentResponseDTO> paymentsByTypeImpl(PaymentType type) {
        List<Payment> payment = paymentRepository.findByType(type);
        return paymentMapper.toResponseDtoList(payment);
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
    public PaymentResponseDTO updatePaymentStatusImpl(PaymentStatus paymentStatus, Long id) {
        Payment paymentToUpdate = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payment not found to modify Status"));
        paymentToUpdate.setStatus(paymentStatus);
        Payment updatedPayment = paymentRepository.save(paymentToUpdate);
        return paymentMapper.toResponseDto(updatedPayment);
    }

    @Override
    public PaymentResponseDTO updatePaymentTypeImpl(PaymentType paymentType, Long id) {
        Payment paymentToUpdate = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payment not found to modify Type"));
        paymentToUpdate.setType(paymentType);
        Payment updatedPayment = paymentRepository.save(paymentToUpdate);
        return paymentMapper.toResponseDto(updatedPayment);
    }

    @Override
    public PaymentRequestDTO savePaymentImpl(PaymentRequestDTO paymentRequestDTO, MultipartFile file) throws IOException {
        // 1. Create folder if not exists
        Path folderPath = Paths.get(System.getProperty("user.home"), "edd-demo-yousfiData", "Payments");
        if (!Files.exists(folderPath)) { Files.createDirectories(folderPath);}

        // 2. Generate safe file name
        String fileName = UUID.randomUUID() + ".pdf";
        Path filePath = folderPath.resolve(fileName);

        // 3. Save file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 4. Map DTO -> Entity
        Payment payment = paymentMapper.toEntity(paymentRequestDTO);
        payment.setFile(filePath.toString());

        // 5. Retrieve student
        Student student = studentRepository.findByCode(paymentRequestDTO.getStudentId());
        payment.setStudent(student);

/*        // 6. Create my New payment with (date, amount, paymentType, paymentStatus, studentCode)
        Payment payment = Payment.builder()
                .date(date)
                .amount(amount)
                .type(paymentType)
                .status(paymentStatus)
                .student(student)
                .file(filePath.toString())
                .build();*/

        // 6. Save in DB
        Payment savedPayment = paymentRepository.save(payment);

        // 7. Return ResponseDTO
        return paymentMapper.toPaymentRequestDTO(savedPayment);
    }
}
