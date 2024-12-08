package com.sber.democrud.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO-запроса нового пользователя или обновления существующего {@link com.sber.democrud.entity.User}.
 */
@Getter
@Setter
public class UserRequestDto {

    /**
     * Имя пользователя.
     */
    @NotBlank
    private String name;

    /**
     * Логин пользователя.
     */
    @Size(max = 50, message = "Максимальная длина логина превышена < 50.")
    @NotBlank
    private String login;

    /**
     * Пароль пользователя.
     */
    @Size(min = 5, message = "Минимальная длина пароля не соблюдена > 5.")
    private String password;

    /**
     * Email пользователя.
     */
    @NotBlank
    @Email
    private String email;

    /**
     * Роль пользователя.
     */
    @NotNull
    private String role;

    /**
     * Дата архивации пользователя.
     */
    @Nullable
    private LocalDateTime archiveDate;
}