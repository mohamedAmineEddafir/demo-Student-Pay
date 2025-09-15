package com.learn.demostudentpay.mappers;

import com.learn.demostudentpay.dtos.PaymentDTO.PaymentRequestDTO;
import com.learn.demostudentpay.dtos.PaymentDTO.PaymentResponseDTO;
import com.learn.demostudentpay.entites.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = StudentMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mapping(source = "date", target = "dateOfPayment")
    @Mapping(source = "amount", target = "amountOfPayment")
    @Mapping(source = "type", target = "paymentType")
    @Mapping(source = "status", target = "paymentStatus")
    @Mapping(source = "file", target = "paymentFilePdf")
    @Mapping(source = "student", target = "studentId")
    PaymentRequestDTO toPaymentRequestDTO(Payment payment);

    Payment toEntity(PaymentRequestDTO paymentRequestDTO);

    List<PaymentRequestDTO> toPaymentRequestDTOList(List<Payment> payments);

    @Mapping(source = "student.fullName", target = "studentFullName")
    PaymentResponseDTO toResponseDto(Payment entity);

    List<PaymentResponseDTO> toResponseDtoList(List<Payment> payment);
}
