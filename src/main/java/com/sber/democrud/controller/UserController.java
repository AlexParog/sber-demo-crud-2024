package com.sber.democrud.controller;

import com.sber.democrud.dto.UserRequestDto;
import com.sber.democrud.dto.UserResponseDto;
import com.sber.democrud.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Контроллер для взаимодействия с API сущности пользователя.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Сервис пользователей.
     */
    private final UserService userService;

    /**
     * Конструктор контроллера {@link UserController}.
     *
     * @param userService сервисный слой для управления пользователями.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Создаёт нового пользователя.
     *
     * @param userRequestDto DTO для создания пользователя.
     * @return {@link ResponseEntity}, содержащий {@link UserResponseDto} и статус 201 (Created).
     */
    @Operation(summary = "Создание пользователя", description = "Создает нового пользователя и сохраняет в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    /**
     * Получает пользователя по его идентификатору.
     * Опционально, может включать связанные платежи.
     *
     * @param id              идентификатор пользователя.
     * @param includePayments флаг, указывающий, нужно ли включать платежи в ответ (по умолчанию false).
     * @return {@link ResponseEntity}, содержащий {@link UserResponseDto} и статус 200 (OK).
     */
    @Operation(summary = "Получение пользователя по ID", description = "Возвращает информацию о пользователе по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "false") boolean includePayments) {
        UserResponseDto userResponseDto = userService.getUserById(id, includePayments);
        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * Обновляет существующего пользователя по идентификатору.
     *
     * @param id             идентификатор пользователя.
     * @param userRequestDto DTO с новыми данными для пользователя.
     * @return {@link ResponseEntity}, содержащий обновлённый {@link UserResponseDto} и статус 200 (OK).
     */
    @Operation(summary = "Обновление информации о пользователе ID",
            description = "Возвращает пользователя по ID с обновленными полями")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновленный пользователь",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Ошибка при обновлении пользователя",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable UUID id,
            @RequestBody @Valid UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.updateUserById(id, userRequestDto));
    }

    /**
     * Архивирует пользователя, устанавливая дату архивации.
     *
     * @param id идентификатор пользователя.
     * @return {@link ResponseEntity}, содержащий архивированный {@link UserResponseDto} и статус 200 (OK).
     */
    @Operation(summary = "Архивирует пользователя по ID",
            description = "Возвращает пользователя по ID с датой архивации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Архивированный пользователь",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Ошибка при архивации пользователя",
                    content = @Content)
    })
    @DeleteMapping("/archive/{id}")
    public ResponseEntity<UserResponseDto> archiveUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.archiveUserById(id), HttpStatus.OK);
    }
}
