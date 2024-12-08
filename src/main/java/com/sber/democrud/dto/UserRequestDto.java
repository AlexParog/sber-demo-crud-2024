package com.sber.democrud.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank
    private String name;

    @Size(max = 50, message = "Максимальная длина логина превышена < 50.")
    @NotBlank
    private String login;

    @Size(min = 5, message = "Минимальная длина пароля не соблюдена > 5.")
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private String role;

    @Nullable
    private LocalDateTime archiveDate;

}
