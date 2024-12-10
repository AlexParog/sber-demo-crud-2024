package com.sber.democrud.controller;

import com.sber.democrud.dto.GoodRequestDto;
import com.sber.democrud.dto.GoodResponseDto;
import com.sber.democrud.service.GoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Создание товара", description = "Создает новый товар и сохраняет в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Товар успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoodResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
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
    @Operation(summary = "Получение товара по ID", description = "Возвращает информацию о товаре по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoodResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Товар не найден",
                    content = @Content)
    })
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
    @Operation(summary = "Обновление всей информации о товаре ID", description = "Возвращает товар по ID с обновленными полями")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновленный товар",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoodResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Ошибка при обновлении товара",
                    content = @Content)
    })
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
    @Operation(summary = "Архивирует товар по ID", description = "Возвращает товар по ID с датой архивации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Архивированный товар",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoodResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Ошибка при архивации товара",
                    content = @Content)
    })
    @DeleteMapping("/archive/{id}")
    public ResponseEntity<GoodResponseDto> archiveGoodById(@PathVariable Long id) {
        return new ResponseEntity<>(goodService.archiveGoodById(id), HttpStatus.OK);
    }
}

