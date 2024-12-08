package com.sber.democrud.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO для создания и обновления информации о товаре.
 */
@Getter
@Setter
public class GoodRequestDto {

    /**
     * Название товара.
     */
    @NotBlank
    private String name;

    /**
     * Тип товара.
     */
    @NotNull
    private String type;

    /**
     * Описание товара.
     */
    @NotBlank
    private String description;

    /**
     * Цена товара.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    @Digits(integer = 10, fraction = 2, message = "Цена должна быть действительным денежным значением")
    private BigDecimal price;

    /**
     * Количество товара на складе.
     */
    @NotNull
    @PositiveOrZero(message = "Количество на складе должно быть равно нулю или больше")
    private Long stockQuantity;
}
