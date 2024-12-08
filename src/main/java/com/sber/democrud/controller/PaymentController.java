package com.sber.democrud.controller;

import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.dto.PaymentResponseDto;
import com.sber.democrud.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto paymentResponseDto = paymentService.createPayment(paymentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) {
        return new ResponseEntity<>(paymentService.getPaymentById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> updatePaymentById(
            @PathVariable Long id,
            @RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(paymentService.updatePaymentById(id, paymentRequestDto));
    }

    @DeleteMapping("/archive/{id}")
    public ResponseEntity<PaymentResponseDto> archivePaymentById(@PathVariable Long id) {
        return new ResponseEntity<>(paymentService.archivePaymentById(id), HttpStatus.OK);
    }
}
