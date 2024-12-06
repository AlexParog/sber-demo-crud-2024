package com.sber.democrud.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.util.function.Supplier;

/**
 * Основной класс для обработки API исключений.
 * Все специфические исключения должны наследовать этот класс.
 */
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;

    /**
     * Конструктор для создания исключения с указанным сообщением и статусом.
     *
     * @param message Сообщение об ошибке.
     * @param status  HTTP статус ошибки.
     */
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * Конструктор для создания исключения с форматированным сообщением и статусом INTERNAL_SERVER_ERROR.
     *
     * @param message Сообщение об ошибке.
     * @param args    Аргументы для форматирования сообщения.
     */
    public ApiException(String message, Object... args) {
        super(MessageFormat.format(message, args));
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * Создание поставщика исключения с форматированным сообщением и статусом INTERNAL_SERVER_ERROR.
     *
     * @param message Сообщение об ошибке.
     * @param args    Аргументы для форматирования сообщения.
     * @return Поставщик исключения.
     */
    public static Supplier<? extends ApiException> apiException(String message, Object... args) {
        return () -> new ApiException(MessageFormat.format(message, args), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Создание поставщика исключения с заданным сообщением и статусом.
     *
     * @param message Сообщение об ошибке.
     * @param status  HTTP статус ошибки.
     * @return Поставщик исключения.
     */
    public static Supplier<? extends ApiException> apiException(String message, HttpStatus status) {
        return () -> new ApiException(message, status);
    }
}
