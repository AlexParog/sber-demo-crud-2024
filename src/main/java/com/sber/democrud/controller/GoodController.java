package com.sber.democrud.controller;

import com.sber.democrud.dto.GoodRequestDto;
import com.sber.democrud.dto.GoodResponseDto;
import com.sber.democrud.service.GoodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для управления товарами.
 */
@RestController
@RequestMapping("/api/goods")
public class GoodController {

    /**
     * Сервис для работы с товарами.
     */
    private final GoodService goodService;

    /**
     * Конструктор для внедрения зависимости сервиса {@link GoodService}.
     *
     * @param goodService сервис для работы с товарами.
     */
    public GoodController(GoodService goodService) {
        this.goodService = goodService;
    }

    /**
     * Создаёт новый товар.
     *
     * @param goodRequestDto DTO с данными для создания товара.
     * @return ответ с созданным товаром и статусом HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<GoodResponseDto> createGood(@RequestBody @Valid GoodRequestDto goodRequestDto) {
        GoodResponseDto response = goodService.createGood(goodRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Получает товар по его идентификатору.
     *
     * @param id идентификатор товара.
     * @return ответ с найденным товаром и статусом HTTP 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GoodResponseDto> getGoodById(@PathVariable Long id) {
        return new ResponseEntity<>(goodService.getGoodById(id), HttpStatus.OK);
    }

    /**
     * Обновляет данные товара по его идентификатору.
     *
     * @param id             идентификатор товара.
     * @param goodRequestDto DTO с обновлёнными данными товара.
     * @return обновлённый товар и статус HTTP 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GoodResponseDto> updateGoodById(
            @PathVariable Long id,
            @RequestBody @Valid GoodRequestDto goodRequestDto) {
        return ResponseEntity.ok(goodService.updateGoodById(id, goodRequestDto));
    }

    /**
     * Архивирует товар по его идентификатору.
     * Устанавливает дату архивации для товара.
     *
     * @param id идентификатор товара.
     * @return архивированный товар и статус HTTP 200 OK.
     */
    @DeleteMapping("/archive/{id}")
    public ResponseEntity<GoodResponseDto> archiveGoodById(@PathVariable Long id) {
        return new ResponseEntity<>(goodService.archiveGoodById(id), HttpStatus.OK);
    }
}

