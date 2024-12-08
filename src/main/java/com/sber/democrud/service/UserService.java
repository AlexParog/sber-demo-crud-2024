package com.sber.democrud.service;

import com.sber.democrud.dto.UserRequestDto;
import com.sber.democrud.dto.UserResponseDto;

import java.util.UUID;

/**
 * Интерфейс сервисного слоя для управления пользователями.
 */
public interface UserService {

    /**
     * Создаёт нового пользователя.
     *
     * @param userRequestDto DTO с данными для создания пользователя.
     * @return созданный {@link UserResponseDto}.
     */
    UserResponseDto createUser(UserRequestDto userRequestDto);

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id              идентификатор пользователя.
     * @param includePayments флаг, указывающий, нужно ли включать связанные платежи.
     * @return найденный {@link UserResponseDto}.
     */
    UserResponseDto getUserById(UUID id, boolean includePayments);

    /**
     * Обновляет данные пользователя.
     *
     * @param id             идентификатор пользователя.
     * @param userRequestDto DTO с новыми данными для пользователя.
     * @return обновленный {@link UserResponseDto}.
     */
    UserResponseDto updateUserById(UUID id, UserRequestDto userRequestDto);

    /**
     * Архивирует пользователя, устанавливая дату архивации.
     *
     * @param id идентификатор пользователя.
     * @return архивированный {@link UserResponseDto}.
     */
    UserResponseDto archiveUserById(UUID id);
}

