package com.sber.democrud.service;

import com.sber.democrud.dto.GoodRequestDto;
import com.sber.democrud.dto.GoodResponseDto;

/**
 * Интерфейс для управления логикой работы с товарами.
 */
public interface GoodService {

    /**
     * Создаёт новый товар.
     *
     * @param goodRequestDto DTO с данными для создания товара.
     * @return DTO с информацией о созданном товаре.
     */
    GoodResponseDto createGood(GoodRequestDto goodRequestDto);

    /**
     * Получает товар по его идентификатору.
     *
     * @param id идентификатор товара.
     * @return DTO с информацией о найденном товаре.
     */
    GoodResponseDto getGoodById(Long id);

    /**
     * Обновляет товар по его идентификатору.
     *
     * @param id             идентификатор товара.
     * @param goodRequestDto DTO с обновлёнными данными товара.
     * @return DTO с информацией об обновлённом товаре.
     */
    GoodResponseDto updateGoodById(Long id, GoodRequestDto goodRequestDto);

    /**
     * Архивирует товар по его идентификатору.
     *
     * @param id идентификатор товара.
     * @return DTO с информацией об архивированном товаре.
     */
    GoodResponseDto archiveGoodById(Long id);
}

