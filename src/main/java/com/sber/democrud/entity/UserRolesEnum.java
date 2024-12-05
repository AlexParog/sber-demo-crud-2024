package com.sber.democrud.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

/**
 * Enum, представляющий роли пользователя {@link User}.
 */
@Getter
public enum UserRolesEnum {
    /**
     * Роль обычного пользователя.
     */
    USER("USER"),
    /**
     * Роль пользователя с расширенными правами.
     */
    ADMIN("ADMIN");

    /**
     * Строковое значение роли.
     */
    @JsonValue
    private final String value;

    /**
     * Конструктор для создания роли с указанным строковым значением.
     *
     * @param value строковое представление роли
     */
    UserRolesEnum(String value) {
        this.value = value;
    }

    /**
     * Преобразует строковое значение в соответствующую роль.
     *
     * @param value строковое представление роли
     * @return объект {@link UserRolesEnum}, соответствующий строковому значению,
     * или {@code null}, если значение некорректно
     */
    @JsonCreator
    public UserRolesEnum fromString(String value) {
        return Arrays.stream(values())
                .filter(status -> status.value.equals(value))
                .findFirst()
                .orElse(null);
    }

    /**
     * Возвращает строковое представление роли.
     *
     * @return строковое значение роли
     */
    @Override
    public String toString() {
        return value;
    }
}
