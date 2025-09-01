package com.learn.demostudentpay.repositorys;

import com.learn.demostudentpay.entites.Payment;
import com.learn.demostudentpay.entites.PaymentStatus;
import com.learn.demostudentpay.entites.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudentCode(String studentCode);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByType(PaymentType type);
}
