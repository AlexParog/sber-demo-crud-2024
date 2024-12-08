package com.sber.democrud.mapper;

import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.dto.PaymentResponseDto;
import com.sber.democrud.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {GoodMapper.class})
public interface PaymentMapper {
    /**
     * Преобразует сущность {@link com.sber.democrud.entity.Payment} в объект {@link com.sber.democrud.dto.PaymentResponseDto}.
     *
     * @param payment объект покупки.
     * @return DTO объекта покупки.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", expression = "java(payment.getUser().getId())")
    @Mapping(target = "totalPurchaseAmount", source = "totalPurchaseAmount")
    @Mapping(target = "archiveDate", source = "archiveDate")
    @Mapping(target = "goods", source = "goods")
    PaymentResponseDto toPaymentResponseDto(Payment payment);

    /**
     * Преобразует DTO объекта {@link com.sber.democrud.dto.PaymentRequestDto} в сущность {@link com.sber.democrud.entity.Payment}.
     *
     * @param paymentRequestDto DTO с данными для создания покупки.
     * @return объект сущности покупки.
     */
    @Mapping(target = "goods", source = "goods")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfPurchase", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    Payment toPayment(PaymentRequestDto paymentRequestDto);

    /**
     * Обновляет существующий объект {@link Payment} на основе данных из {@link PaymentRequestDto}.
     *
     * @param paymentRequestDto DTO с новыми данными покупки.
     * @param payment           объект покупки, который необходимо обновить.
     */

    @Mapping(target = "goods", source = "goods")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfPurchase", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updatePaymentFromDto(PaymentRequestDto paymentRequestDto, @MappingTarget Payment payment);
}
