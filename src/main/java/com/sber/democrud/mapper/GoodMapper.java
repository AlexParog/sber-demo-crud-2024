package com.sber.democrud.mapper;

import com.sber.democrud.dto.GoodRequestDto;
import com.sber.democrud.dto.GoodResponseDto;
import com.sber.democrud.entity.Good;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Маппер для преобразования между сущностью {@link Good} и DTO объектами.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GoodMapper {

    /**
     * Преобразует сущность {@link Good} в объект {@link GoodResponseDto}.
     *
     * @param good объект товара.
     * @return DTO объекта товара.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    GoodResponseDto toGoodResponseDto(Good good);

    /**
     * Преобразует DTO объекта {@link GoodRequestDto} в сущность {@link Good}.
     *
     * @param goodRequestDto DTO с данными для создания товара.
     * @return объект сущности товара.
     */
    Good toGood(GoodRequestDto goodRequestDto);

    /**
     * Обновляет существующий объект {@link Good} на основе данных из {@link GoodRequestDto}.
     *
     * @param goodRequestDto DTO с новыми данными товара.
     * @param good           объект товара, который необходимо обновить.
     */
    void updateGoodFromDto(GoodRequestDto goodRequestDto, @MappingTarget Good good);
}

