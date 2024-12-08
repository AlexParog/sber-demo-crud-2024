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

@Getter
@Setter
public class UserResponseDto {
    private UUID id;

    @NotBlank
    private String name;

    @Size(max = 50, message = "Максимальная длина логина превышена < 50.")
    @NotBlank
    private String login;

    @JsonIgnore
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private String role;

    @Nullable
    private LocalDateTime archiveDate;

    @Nullable
    private Set<PaymentResponseDto> payments = new HashSet<>();
}
