package com.sber.democrud.service.impl;

import com.sber.democrud.dto.GoodRequestDto;
import com.sber.democrud.dto.GoodResponseDto;
import com.sber.democrud.entity.Good;
import com.sber.democrud.exception.NotFoundException;
import com.sber.democrud.mapper.GoodMapper;
import com.sber.democrud.repository.GoodRepository;
import com.sber.democrud.service.GoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Сервисный слой для управления объектами {@link Good}.
 * Реализует интерфейс {@link GoodService}.
 * Содержит бизнес-логику обработки данных товаров.
 */
@Service
public class GoodServiceImpl implements GoodService {
    /**
     * Логгер.
     */
    private static final Logger log = LoggerFactory.getLogger(GoodServiceImpl.class);

    /**
     * Репозиторий для работы с базой данных, соответствующий сущности {@link Good}.
     */
    private final GoodRepository goodRepository;

    /**
     * Маппер для преобразования между объектами {@link Good}, {@link GoodRequestDto} и {@link GoodResponseDto}.
     */
    private final GoodMapper goodMapper;

    /**
     * Конструктор для создания объекта {@link GoodServiceImpl}.
     *
     * @param goodRepository репозиторий для управления товарами.
     * @param goodMapper     маппер для преобразования DTO и сущностей.
     */
    public GoodServiceImpl(GoodRepository goodRepository, GoodMapper goodMapper) {
        this.goodRepository = goodRepository;
        this.goodMapper = goodMapper;
    }

    /**
     * Создаёт новый товар.
     * Преобразует входной DTO {@link GoodRequestDto} в сущность {@link Good}, сохраняет его в базе данных
     * и возвращает обратно DTO {@link GoodResponseDto}.
     *
     * @param goodRequestDto DTO с данными для создания товара.
     * @return DTO с информацией о созданном товаре.
     */
    @Override
    public GoodResponseDto createGood(GoodRequestDto goodRequestDto) {
        log.info("Создание товара: {}", goodRequestDto);

        Good good = goodMapper.toGood(goodRequestDto);
        goodRepository.save(good);

        GoodResponseDto goodResponseDto = goodMapper.toGoodResponseDto(good);
        log.info("Товар успешно создан с ID: {}", goodResponseDto.getId());
        return goodResponseDto;
    }

    /**
     * Получает товар по его идентификатору.
     * Выполняет поиск по ID в репозитории.
     * Если товар не найден, выбрасывает исключение {@link NotFoundException}.
     *
     * @param id идентификатор товара.
     * @return DTO с информацией о найденном товаре.
     */
    @Override
    public GoodResponseDto getGoodById(Long id) {
        log.info("Получение товара с ID: {}", id);

        Good good = findGoodOrNotFound(id);
        log.debug("Товар найден: {}", good);

        return goodMapper.toGoodResponseDto(good);
    }

    /**
     * Обновляет существующий товар по его идентификатору.
     * Получает товар из базы данных, обновляет его полями из DTO
     * и сохраняет его в базе данных.
     *
     * @param id             идентификатор обновляемого товара.
     * @param goodRequestDto DTO с новыми данными для товара.
     * @return DTO с обновленной информацией о товаре.
     */
    @Override
    public GoodResponseDto updateGoodById(Long id, GoodRequestDto goodRequestDto) {
        log.info("Обновление товара с ID: {} данными: {}", id, goodRequestDto);

        Good existingGood = findGoodOrNotFound(id);
        goodMapper.updateGoodFromDto(goodRequestDto, existingGood);
        goodRepository.save(existingGood);
        log.info("Товар с ID: {} успешно обновлен", id);

        return goodMapper.toGoodResponseDto(existingGood);
    }

    /**
     * Архивирует существующий товар по его идентификатору.
     * Устанавливает текущую дату как дату архивации и сохраняет изменения в базе данных.
     *
     * @param id идентификатор архивируемого товара.
     * @return DTO с информацией об архивированном товаре.
     */
    @Override
    public GoodResponseDto archiveGoodById(Long id) {
        log.info("Архивирование товара с ID: {}", id);

        Good good = findGoodOrNotFound(id);
        good.setArchiveDate(LocalDateTime.now());
        goodRepository.save(good);

        log.info("Товар с ID: {} успешно архивирован на дату: {}", id, good.getArchiveDate());
        return goodMapper.toGoodResponseDto(good);
    }

    /**
     * Выполняет поиск товара по идентификатору.
     * Если товар не найден, выбрасывает исключение {@link NotFoundException}.
     *
     * @param id идентификатор товара.
     * @return объект {@link Good}, найденный в базе данных.
     */
    private Good findGoodOrNotFound(Long id) {
        log.debug("Поиск товара с ID: {}", id);

        return goodRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Товар с ID: {} не найден", id);
                    return new NotFoundException("Товар c id={0} не найден", id);
                });
    }
}

