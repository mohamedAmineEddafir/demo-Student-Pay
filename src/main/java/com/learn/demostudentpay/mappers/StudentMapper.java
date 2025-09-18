package com.learn.demostudentpay.mappers;

import com.learn.demostudentpay.dtos.StudentDTO.StudentResponseDTO;
import com.learn.demostudentpay.entites.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    // Entity -> ResponseDTO
    @Mapping(source = "id", target = "studentId")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "photo", target = "photo")
    @Mapping(expression = "java(entity.getFullName())", target = "fullName")
    StudentResponseDTO toResponseDTO(Student entity);

    // ResponseDTO → Entity (si nécessaire)
    @Mapping(source = "studentId", target = "Id")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "photo", target = "Photo")
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    Student toResponseEntity(StudentResponseDTO studentResponseDTO);

    List<StudentResponseDTO> toResponseDTOList(List<Student> students);
}