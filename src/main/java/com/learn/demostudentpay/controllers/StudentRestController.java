package com.learn.demostudentpay.controllers;

import com.learn.demostudentpay.entites.Student;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/student")
public class StudentRestController {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentRestController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{code}")
    public Student getStudentByCode(@PathVariable String code) {
        return studentRepository.findByCode(code);
    }

    @GetMapping("/programId")
    public List<Student> getStudentByProgramId(@RequestParam String programId) {
        return studentRepository.findByProgramID(programId);
    }

    @GetMapping(path = "/photo/{code}")
    public ResponseEntity<Resource> getImageByCode(@PathVariable String code) throws IOException {
        Student student = studentRepository.findByCode(code);

        Path filePath = Paths.get(System.getProperty("user.home"), "edd-demo-yousfiData", "users-photos");
        if (!Files.exists(filePath)) Files.createDirectory(filePath);
        //System.out.println(filePath.toString());

        Path PathFinal = Paths.get(filePath.resolve(student.getPhoto()).toUri());
        //System.out.println(PathFinal+" photoID "+student.getPhoto());

        if (!Files.exists(PathFinal)) {
            throw new FileNotFoundException("Image not found  "+ PathFinal);
        }
        Resource resource = new UrlResource(PathFinal.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("Image not found  "+ PathFinal);
        }

        String contentType = Files.probeContentType(PathFinal);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    /*
     * Put Methods
     * */
    @PutMapping("/programId/update/{code}")
    public Student updateProgramByCode(@RequestParam String programId, @PathVariable String code) {
        Student student = studentRepository.findByCode(code);
        student.setProgramID(programId);
        return studentRepository.save(student);
    }

    /*
     * Post Mothed
     * */
    @PostMapping(path = "/newStudent/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Student createStudent(@RequestParam MultipartFile file, String firstName,
                                 String lastName, String programID) throws IOException {

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
