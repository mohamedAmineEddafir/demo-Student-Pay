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

    // Entity -> RequestDTO
    @Mapping(source = "date", target = "dateOfPayment")
    @Mapping(source = "amount", target = "amountOfPayment")
    @Mapping(source = "type", target = "paymentType")
    @Mapping(source = "status", target = "paymentStatus")
    @Mapping(source = "file", target = "paymentFilePdf")
    @Mapping(source = "student.id", target = "studentId")
    PaymentRequestDTO toPaymentRequestDTO(Payment payment);

    // RequestDTO -> Entity
    @Mapping(source = "dateOfPayment", target = "date")
    @Mapping(source = "amountOfPayment", target = "amount")
    @Mapping(source = "paymentType", target = "type")
    @Mapping(source = "paymentStatus", target = "status")
    @Mapping(source = "paymentFilePdf", target = "file")
    @Mapping(target = "student", ignore = true) // On ignore student pour le moment
    @Mapping(target = "id", ignore = true) // On ignore l'ID, car il est généré
    Payment toEntity(PaymentRequestDTO paymentRequestDTO);

    // Entity -> ResponseDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "file", target = "file")
    @Mapping(expression = "java(entity.getStudent() != null ? entity.getStudent().getFullName() : null)", target = "studentFullName")
    PaymentResponseDTO toResponseDto(Payment entity);

    List<PaymentResponseDTO> toResponseDtoList(List<Payment> payments);
}
