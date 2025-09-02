package com.learn.demostudentpay.services.serviceInterface;

import com.learn.demostudentpay.entites.Student;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface StudentServiceInterface {

    List<Student> getAllStudentsImpl();

    Student getStudentByCodeImpl(String code);

    List<Student> getStudentByProgramIdImpl(String programId);

    ResponseEntity<Resource> getImageByCodeImpl(String code) throws IOException;

    Student updateProgramByCodeImpl(String programId, String code);

    Student createStudentImpl(MultipartFile file,
                              String firstName,
                              String lastName,
                              String programID)
            throws IOException;
}
