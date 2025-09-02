package com.learn.demostudentpay.services.ServiceImpl;

import com.learn.demostudentpay.entites.Student;
import com.learn.demostudentpay.repositorys.PaymentRepository;
import com.learn.demostudentpay.repositorys.StudentRepository;
import com.learn.demostudentpay.services.serviceInterface.StudentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentServiceInterface {

    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, PaymentRepository paymentRepository) {
        this.studentRepository = studentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Student> getAllStudentsImpl() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentByCodeImpl(String code) {
        return studentRepository.findByCode(code);
    }

    @Override
    public List<Student> getStudentByProgramIdImpl(String programId) {
        return studentRepository.findByProgramID(programId);
    }

    @Override
    public ResponseEntity<Resource> getImageByCodeImpl(String code) throws IOException {
        Student student = studentRepository.findByCode(code);

        Path filePath = Paths.get(System.getProperty("user.home"), "edd-demo-yousfiData", "users-photos");
        if (!Files.exists(filePath)) Files.createDirectory(filePath);
        //System.out.println(filePath.toString());

        Path PathFinal = Paths.get(filePath.resolve(student.getPhoto()).toUri());
        //System.out.println(PathFinal+" photoID "+student.getPhoto());

        if (!Files.exists(PathFinal)) {
            throw new FileNotFoundException("Image not found  " + PathFinal);
        }
        Resource resource = new UrlResource(PathFinal.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("Image not found  " + PathFinal);
        }

        String contentType = Files.probeContentType(PathFinal);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @Override
    public Student updateProgramByCodeImpl(String programId, String code) {
        Student student = studentRepository.findByCode(code);
        student.setProgramID(programId);
        return studentRepository.save(student);
    }

    @Override
    public Student createStudentImpl(MultipartFile file, String firstName, String lastName, String programID) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.home"), "edd-demo-yousfiData", "users-photos");
        if (!Files.exists(filePath)) Files.createDirectory(filePath);

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        //String fileName = studentRepository.findByCode(programID).getCode() +  extension;
        String fileName = UUID.randomUUID() + extension;
        Path destination = filePath.resolve(fileName);
        file.transferTo(destination);

        Student student = Student.builder()
                .Id(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .code("STU" + UUID.randomUUID())
                .programID(programID)
                .Photo(fileName)
                .build();

        return studentRepository.save(student);
    }
}
