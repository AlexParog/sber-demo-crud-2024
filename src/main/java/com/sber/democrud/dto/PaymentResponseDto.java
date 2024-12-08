package com.sber.democrud.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class PaymentResponseDto {
    private Long id;

    @NotNull
    private UUID userId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Итоговая цена должна быть больше 0")
    @Digits(integer = 10, fraction = 2, message = "Итоговая цена покупки должна быть действительным денежным значением")
    private BigDecimal totalPurchaseAmount;

    @Nullable
    private LocalDateTime archiveDate;

    @NotNull
    private Set<GoodResponseDto> goods = new HashSet<>();
}
