package com.sber.democrud.crud_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.democrud.dto.GoodRequestDto;
import com.sber.democrud.entity.Good;
import com.sber.democrud.entity.GoodTypesEnum;
import com.sber.democrud.repository.GoodRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Тестовый класс для проверки CRUD API операций над сущностью {@code Good}.
 *
 * <p>Класс содержит тесты для проверки создания, чтения, обновления и архивирования товаров,
 * а также обработку сценария, когда товар не найден.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GoodCrudApiTest {
    private static final String PATH = "/api/goods";
    /**
     * Объект для тестирования REST API через HTTP-запросы.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Объект для сериализации и десериализации JSON-данных.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Репозиторий для работы с сущностью {@code Good}.
     */
    @Autowired
    private GoodRepository goodRepository;

    /**
     * Тестовый объект {@code Good}, используемый в сценариях тестирования.
     */
    private Good testGood;

    /**
     * Метод, выполняемый перед всеми тестами.
     * <p>Создает тестовую запись в базе данных.</p>
     */
    @BeforeAll
    void setup() {
        // тестовая запись в БД
        testGood = new Good();
        testGood.setName("iPad Air");
        testGood.setType(GoodTypesEnum.ELECTRONICS);
        testGood.setDescription("Apple iPad");
        testGood.setPrice(BigDecimal.valueOf(1000.99));
        testGood.setStockQuantity(10L);
        testGood.setArchiveDate(null);
        testGood = goodRepository.save(testGood);
    }

    /**
     * Метод, выполняемый после всех тестов.
     * <p>Удаляет тестовую запись из базы данных, если она существует.</p>
     */
    @AfterAll
    void cleanup() {
        // Удаляем только тестовую запись по ID
        if (testGood != null && goodRepository.existsById(testGood.getId())) {
            goodRepository.deleteById(testGood.getId());
        }
    }

    /**
     * Тест для проверки создания нового товара через API.
     * <p>Сценарий: отправка POST-запроса с JSON-данными, проверка ответа и записи в базе данных.</p>
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testCreateGood() throws Exception {
        GoodRequestDto goodRequest = new GoodRequestDto();
        goodRequest.setName("Samsung");
        goodRequest.setType("ELECTRONICS");
        goodRequest.setDescription("Samsung description");
        goodRequest.setPrice(BigDecimal.valueOf(1000.99));
        goodRequest.setStockQuantity(1L);
        goodRequest.setArchiveDate(null);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goodRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Samsung"))
                .andExpect(jsonPath("$.type").value("ELECTRONICS"))
                .andExpect(jsonPath("$.description").value("Samsung description"))
                .andExpect(jsonPath("$.price").value(1000.99))
                .andExpect(jsonPath("$.stockQuantity").value(1))
                .andExpect(jsonPath("$.archiveDate").doesNotExist());

        // Проверяем, что запись появилась в базе
        Good createdGood = goodRepository.findByName("Samsung").orElseThrow();
        assertThat(createdGood)
                .isNotNull()
                .extracting(Good::getName, Good::getType, Good::getDescription, Good::getPrice, Good::getStockQuantity)
                .containsExactly(
                        goodRequest.getName(),
                        GoodTypesEnum.valueOf(goodRequest.getType()),
                        goodRequest.getDescription(),
                        goodRequest.getPrice(),
                        goodRequest.getStockQuantity());

        // Удаляем созданную запись
        goodRepository.delete(createdGood);
    }

    /**
     * Тест для проверки получения товара по его идентификатору.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testGetGoodById() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", testGood.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGood.getId()))
                .andExpect(jsonPath("$.name").value(testGood.getName()))
                .andExpect(jsonPath("$.type").value(testGood.getType().getValue()))
                .andExpect(jsonPath("$.description").value(testGood.getDescription()))
                .andExpect(jsonPath("$.price").value(testGood.getPrice()))
                .andExpect(jsonPath("$.stockQuantity").value(testGood.getStockQuantity()));
    }

    /**
     * Тест для проверки обновления товара по его идентификатору.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testUpdateGoodById() throws Exception {
        GoodRequestDto updatedGoodRequest = new GoodRequestDto();
        updatedGoodRequest.setName("MacBook Air");
        updatedGoodRequest.setType("ELECTRONICS");
        updatedGoodRequest.setDescription("MacBook Air description");
        updatedGoodRequest.setPrice(new BigDecimal("1500.00"));
        updatedGoodRequest.setStockQuantity(5L);


        mockMvc.perform(put(PATH + "/{id}", testGood.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGoodRequest)))
                .andExpect(status().isOk());

        // Проверяем обновленную запись в базе
        Good updatedGood = goodRepository.findById(testGood.getId()).orElseThrow();
        assertThat(updatedGood)
                .isNotNull()
                .extracting(Good::getName, Good::getType, Good::getDescription, Good::getPrice, Good::getStockQuantity)
                .containsExactly(
                        updatedGoodRequest.getName(),
                        GoodTypesEnum.valueOf(updatedGoodRequest.getType()),
                        updatedGoodRequest.getDescription(),
                        updatedGoodRequest.getPrice(),
                        updatedGoodRequest.getStockQuantity());
    }

    /**
     * Тест для проверки архивирования товара по его идентификатору.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testArchiveGoodById() throws Exception {
        mockMvc.perform(delete(PATH + "/archive/{id}", testGood.getId()))
                .andExpect(status().isOk());

        // Проверяем, что поле archiveDate заполнено
        Good archivedGood = goodRepository.findById(testGood.getId()).orElseThrow();
        assertThat(archivedGood.getArchiveDate()).isNotNull();
    }

    /**
     * Тест для проверки обработки ситуации, когда товар с указанным идентификатором не найден.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testGetGoodByIdNotFound() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", 9999))
                .andExpect(status().isNotFound());
    }
}