package com.sber.democrud.service;

import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.dto.PaymentResponseDto;

/**
 * Интерфейс сервисного слоя для управления платежами.
 */
public interface PaymentService {

    /**
     * Создаёт новый платёж.
     *
     * @param paymentRequestDto DTO с данными для создания платежа.
     * @return созданный {@link PaymentResponseDto}.
     */
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto);

    /**
     * Получает платеж по его идентификатору.
     *
     * @param id идентификатор платежа.
     * @return {@link PaymentResponseDto}, соответствующий найденному платежу.
     */
    PaymentResponseDto getPaymentById(Long id);

    /**
     * Обновляет платёж по его идентификатору.
     *
     * @param id                идентификатор платежа.
     * @param paymentRequestDto DTO с новыми данными для платежа.
     * @return обновлённый {@link PaymentResponseDto}.
     */
    PaymentResponseDto updatePaymentById(Long id, PaymentRequestDto paymentRequestDto);

    /**
     * Архивирует платёж по его идентификатору.
     *
     * @param id идентификатор платежа.
     * @return архивированный {@link PaymentResponseDto}.
     */
    PaymentResponseDto archivePaymentById(Long id);
}

