package com.learn.demostudentpay.services.serviceInterface;

import com.learn.demostudentpay.dtos.StudentDTO.StudentResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface StudentServiceInterface {

    List<StudentResponseDTO> getAllStudentsImpl();

    StudentResponseDTO getStudentByCodeImpl(String code);

    List<StudentResponseDTO> getStudentByProgramIdImpl(String programId);

    ResponseEntity<Resource> getImageByCodeImpl(String code) throws IOException;

    StudentResponseDTO updateProgramByCodeImpl(String programId, String code);

    StudentResponseDTO createStudentImpl(StudentResponseDTO studentResponseDTO, MultipartFile file) throws IOException;
}
