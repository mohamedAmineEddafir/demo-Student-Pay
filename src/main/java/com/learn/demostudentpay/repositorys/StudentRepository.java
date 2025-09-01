package com.learn.demostudentpay.repositorys;

import com.learn.demostudentpay.entites.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByCode(String code);
    List<Student> findByProgramID(String programID);
}
