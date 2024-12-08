package com.sber.democrud.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DTO-ответа информации о пользователе {@link com.sber.democrud.entity.User}.
 */
@Getter
@Setter
public class UserResponseDto {

    /**
     * Уникальный идентификатор пользователя.
     */
    private UUID id;

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
    @JsonIgnore
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

    /**
     * Список платежей, связанных с пользователем.
     */
    @Nullable
    private Set<PaymentResponseDto> payments = new HashSet<>();
}
