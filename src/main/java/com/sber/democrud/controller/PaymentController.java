package com.sber.democrud.controller;

import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.dto.PaymentResponseDto;
import com.sber.democrud.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Создание платежа", description = "Создает новый платеж и сохраняет в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Платеж успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
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
    @Operation(summary = "Получение платежа по ID", description = "Возвращает информацию о платеже по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Платеж найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Платеж не найден",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        return new ResponseEntity<>(paymentService.getPaymentById(id), HttpStatus.OK);
    }

    /**
     * Обновляет существующий платёж (кроме информации о пользователе платежа) по его идентификатору.
     *
     * @param id                идентификатор платежа.
     * @param paymentRequestDto DTO с новыми данными для платежа.
     * @return {@link ResponseEntity}, содержащий обновлённый {@link PaymentResponseDto} и статус 200 (OK).
     */
    @Operation(summary = "Обновление информации (кроме информации о пользователе платежа) о платеже ID",
            description = "Возвращает платеж по ID с обновленными полями")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновленный платеж",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Ошибка при обновлении платежа",
                    content = @Content)
    })
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
    @Operation(summary = "Архивирует платеж по ID", description = "Возвращает платеж по ID с датой архивации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Архивированный платеж",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Ошибка при архивации платежа",
                    content = @Content)
    })
    @DeleteMapping("/archive/{id}")
    public ResponseEntity<PaymentResponseDto> archivePaymentById(@PathVariable Long id) {
        return new ResponseEntity<>(paymentService.archivePaymentById(id), HttpStatus.OK);
    }
}