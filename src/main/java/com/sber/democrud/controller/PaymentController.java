package com.sber.democrud.controller;

import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.dto.PaymentResponseDto;
import com.sber.democrud.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер REST API для управления платежами.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    /**
     * Сервис платежей.
     */
    private final PaymentService paymentService;

    /**
     * Конструктор контроллера {@link PaymentController}.
     *
     * @param paymentService сервисный слой для управления платежами.
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Создаёт новый платёж.
     *
     * @param paymentRequestDto DTO с данными для создания платежа.
     * @return {@link ResponseEntity}, содержащий {@link PaymentResponseDto} и статус 201 (Created).
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto paymentResponseDto = paymentService.createPayment(paymentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponseDto);
    }

    /**
     * Получает информацию о платеже по его идентификатору.
     *
     * @param id идентификатор платежа.
     * @return {@link ResponseEntity}, содержащий {@link PaymentResponseDto} и статус 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        return new ResponseEntity<>(paymentService.getPaymentById(id), HttpStatus.OK);
    }

    /**
     * Обновляет существующий платёж по его идентификатору.
     *
     * @param id                идентификатор платежа.
     * @param paymentRequestDto DTO с новыми данными для платежа.
     * @return {@link ResponseEntity}, содержащий обновлённый {@link PaymentResponseDto} и статус 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> updatePaymentById(
            @PathVariable Long id,
            @RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(paymentService.updatePaymentById(id, paymentRequestDto));
    }

    /**
     * Архивирует платёж по его идентификатору.
     *
     * @param id идентификатор платежа.
     * @return {@link ResponseEntity}, содержащий архивированный {@link PaymentResponseDto} и статус 200 (OK).
     */
    @DeleteMapping("/archive/{id}")
    public ResponseEntity<PaymentResponseDto> archivePaymentById(@PathVariable Long id) {
        return new ResponseEntity<>(paymentService.archivePaymentById(id), HttpStatus.OK);
    }
}