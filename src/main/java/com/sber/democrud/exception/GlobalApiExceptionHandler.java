package com.sber.democrud.exception;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * Глобальный обработчик исключений API.
 * Обрабатывает различные типы исключений и возвращает подходящие ответы.
 */
@Slf4j
@RestControllerAdvice
public class GlobalApiExceptionHandler {

    /**
     * Обработчик исключения NotFoundException.
     * Возвращает статус 404 и сообщение об ошибке.
     *
     * @param notFoundException Исключение NotFoundException.
     * @return Ответ с сообщением об ошибке и статусом 404.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException notFoundException) {
        log.error(notFoundException.getMessage(), notFoundException);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(notFoundException.getMessage()));
    }

    /**
     * Обработчик исключения NoSuchElementException.
     * Возвращает статус 204 и сообщение об ошибке.
     *
     * @param noSuchElementException Исключение NoSuchElementException.
     * @return Ответ с сообщением об ошибке и статусом 204.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> noSuchElementException(NoSuchElementException noSuchElementException) {
        log.error(noSuchElementException.getMessage(), noSuchElementException);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ErrorMessage(noSuchElementException.getMessage()));
    }

    /**
     * Обработчик всех прочих исключений ApiException.
     * Возвращает статус 500 и сообщение об ошибке.
     *
     * @param apiException Исключение ApiException.
     * @return Ответ с сообщением об ошибке и статусом 500.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorMessage> generalApiException(ApiException apiException) {
        log.error(apiException.getMessage(), apiException);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(apiException.getMessage()));
    }
}
