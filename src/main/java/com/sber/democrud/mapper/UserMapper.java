package com.sber.democrud.mapper;

import com.sber.democrud.dto.UserRequestDto;
import com.sber.democrud.dto.UserResponseDto;
import com.sber.democrud.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PaymentMapper.class})
public interface UserMapper {
    /**
     * Преобразует сущность {@link com.sber.democrud.entity.User} в объект {@link com.sber.democrud.dto.UserResponseDto}.
     *
     * @param user объект пользователя.
     * @return DTO объекта пользователя.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "archiveDate", source = "archiveDate")
    @Mapping(target = "payments", source = "payments")
    UserResponseDto toUserResponseDto(User user);

    /**
     * Преобразует DTO объекта {@link com.sber.democrud.dto.UserRequestDto} в сущность {@link User}.
     *
     * @param userRequestDto DTO с данными для создания пользователя.
     * @return объект сущности пользователя.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "payments", ignore = true)
    User toUser(UserRequestDto userRequestDto);

    /**
     * Обновляет существующий объект {@link User} на основе данных из {@link UserRequestDto}.
     *
     * @param userRequestDto DTO с новыми данными пользователя.
     * @param user           объект пользователя, который необходимо обновить.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "payments", ignore = true)
    void updateUserFromDto(UserRequestDto userRequestDto, @MappingTarget User user);
}
