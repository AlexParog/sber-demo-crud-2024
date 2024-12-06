package com.sber.democrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * Исключение для случаев, когда запрашиваемый ресурс не найден.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends ApiException {

    /**
     * Конструктор для создания исключения NotFoundException с форматированным сообщением.
     *
     * @param message Сообщение об ошибке.
     * @param args    Аргументы для форматирования сообщения.
     */
    public NotFoundException(String message, Object... args) {
        super(MessageFormat.format(message, args), HttpStatus.NOT_FOUND);
    }
}
