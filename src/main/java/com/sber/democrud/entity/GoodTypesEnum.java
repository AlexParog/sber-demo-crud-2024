package com.sber.democrud.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

/**
 * Enum, представляющий возможные типы товара {@link Good}.
 */
@Getter
public enum GoodTypesEnum {
    /**
     * Тип товара: электроника.
     */
    ELECTRONICS("ELECTRONICS"),

    /**
     * Тип товара: одежда.
     */
    CLOTHING("CLOTHING"),

    /**
     * Тип товара: книги.
     */
    BOOKS("BOOKS"),

    /**
     * Тип товара: другой тип товара не подходящий ни под одну из вышеперечисленных категорий.
     */
    OTHER("OTHER");

    /**
     * Строковое значение типа товара.
     */
    @JsonValue
    private final String value;

    /**
     * Конструктор для создания типа товара с указанным строковым значением.
     *
     * @param value строковое представление типа товара
     */
    GoodTypesEnum(String value) {
        this.value = value;
    }

    /**
     * Преобразует строковое значение в соответствующий тип товара.
     *
     * @param value строковое представление типа товара
     * @return объект {@link GoodTypesEnum}, соответствующий строковому значению,
     * или {@code null}, если значение некорректно
     */
    @JsonCreator
    public GoodTypesEnum fromString(String value) {
        return Arrays.stream(values())
                .filter(status -> status.value.equals(value))
                .findFirst()
                .orElse(null);
    }

    /**
     * Возвращает строковое представление типа товара.
     *
     * @return строковое значение типа товара
     */
    @Override
    public String toString() {
        return value;
    }
}
