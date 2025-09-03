package com.learn.demostudentpay.controllers;

import com.learn.demostudentpay.entites.Student;
import com.learn.demostudentpay.services.serviceInterface.StudentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentRestController {

    private final StudentServiceInterface studentService;

    @Autowired
    public StudentRestController(StudentServiceInterface studentServiceImpl) {
        this.studentService = studentServiceImpl;
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentService.getAllStudentsImpl();
    }

    @GetMapping("/{code}")
    public Student getStudentByCode(@PathVariable String code) {
        return studentService.getStudentByCodeImpl(code);
    }

    @GetMapping("/programId")
    public List<Student> getStudentByProgramId(@RequestParam String programId) {
        return studentService.getStudentByProgramIdImpl(programId);
    }

    @GetMapping(path = "/photo/{code}")
    public ResponseEntity<Resource> getImageByCode(@PathVariable String code) throws IOException {
        return studentService.getImageByCodeImpl(code);
    }

    /*
     * Put Methods
     * */
    @PutMapping("/programId/update/{code}")
    public Student updateProgramByCode(@RequestParam String programId, @PathVariable String code) {
        return studentService.updateProgramByCodeImpl(programId, code);
    }

    /*
     * Post Methods
     * */
    @PostMapping(path = "/newStudent/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Student createStudent(@RequestParam MultipartFile file, String firstName,
                                 String lastName, String programID) throws IOException {
        return studentService.createStudentImpl(file, firstName, lastName, programID);
    }

}
