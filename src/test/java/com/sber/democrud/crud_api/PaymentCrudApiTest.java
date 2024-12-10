package com.sber.democrud.crud_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.entity.*;
import com.sber.democrud.mapper.GoodMapper;
import com.sber.democrud.repository.GoodRepository;
import com.sber.democrud.repository.PaymentRepository;
import com.sber.democrud.repository.UserRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тестовый класс для проверки CRUD операций с сущностью Payment.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentCrudApiTest {

    /**
     * Базовый путь для API платежей.
     */
    private static final String PATH = "/api/payments";

    /**
     * Объект для тестирования REST API через HTTP-запросы.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Объект для сериализации и десериализации JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Репозиторий для работы с сущностью Payment.
     */
    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Репозиторий для работы с сущностью User.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Репозиторий для работы с сущностью Good.
     */
    @Autowired
    private GoodRepository goodRepository;

    /**
     * Маппер для преобразования Good в DTO и обратно.
     */
    @Autowired
    private GoodMapper goodMapper;

    /**
     * Тестовый пользователь для выполнения операций с платежами.
     */
    private User testUser;

    /**
     * Набор тестовых товаров для создания платежей.
     */
    private Set<Good> testGoods = new HashSet<>();

    /**
     * Тестовый платеж.
     */
    private Payment testPayment;

    /**
     * Инициализация тестовых данных перед выполнением всех тестов.
     */
    @BeforeAll
    void setup() {
        // Создание тестового пользователя
        testUser = new User();
        testUser.setName("Test User");
        testUser.setLogin("testuser");
        testUser.setPassword("testpassword123");
        testUser.setEmail("testuser@example.com");
        testUser.setRole(UserRolesEnum.USER);
        testUser.setArchiveDate(null);
        testUser = userRepository.save(testUser);

        // Создание тестовых товаров
        Good testGood1 = new Good();
        testGood1.setName("Test Good 1");
        testGood1.setType(GoodTypesEnum.OTHER);
        testGood1.setDescription("Test Description 1");
        testGood1.setPrice(BigDecimal.valueOf(1000));
        testGood1.setStockQuantity(1L);
        testGood1.setArchiveDate(null);

        Good testGood2 = new Good();
        testGood2.setName("Test Good 2");
        testGood2.setType(GoodTypesEnum.OTHER);
        testGood2.setDescription("Test Description 2");
        testGood2.setPrice(BigDecimal.valueOf(1000));
        testGood2.setStockQuantity(1L);
        testGood2.setArchiveDate(null);

        // Сохраняем все товары сразу
        testGoods = new HashSet<>(goodRepository.saveAll(List.of(testGood1, testGood2)));

        // Создание тестового платежа
        testPayment = new Payment();
        testPayment.setTotalPurchaseAmount(BigDecimal.valueOf(300));
        testPayment.setArchiveDate(null);
        testPayment.setUser(testUser);
        testPayment.setGoods(testGoods);
        testPayment = paymentRepository.save(testPayment);
    }

    /**
     * Удаление тестовых данных после выполнения всех тестов.
     */
    @AfterAll
    void cleanup() {
        // Удаляем все созданные тестовые данные
        if (testUser != null && userRepository.existsById(testUser.getId())) {
            userRepository.deleteById(testUser.getId());
        }
        if (testPayment != null && paymentRepository.existsById(testPayment.getId())) {
            paymentRepository.deleteById(testPayment.getId());
        }
        for (Good good : testGoods) {
            if (good != null && goodRepository.existsById(good.getId())) {
                goodRepository.deleteById(good.getId());
            }
        }
    }

    /**
     * Тест для создания нового платежа.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testCreatePayment() throws Exception {
        User testUserForNewPayment = new User();
        testUserForNewPayment.setName("Test User For New Payment");
        testUserForNewPayment.setLogin("testuserfornewpayment");
        testUserForNewPayment.setPassword("testpassword123");
        testUserForNewPayment.setEmail("testuserfornewpayment@example.com");
        testUserForNewPayment.setRole(UserRolesEnum.USER);
        testUserForNewPayment.setArchiveDate(null);
        testUserForNewPayment = userRepository.save(testUserForNewPayment);

        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        paymentRequest.setTotalPurchaseAmount(BigDecimal.valueOf(500));
        paymentRequest.setUserId(testUserForNewPayment.getId());
        paymentRequest.setGoods(goodMapper.toGoodResponseDtos(testGoods));

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated());

        // Проверяем создание платежа
        Payment createdPayment = paymentRepository.findPaymentByUserId(testUserForNewPayment.getId())
                .orElseThrow();

        assertThat(createdPayment).isNotNull();
        assertThat(createdPayment.getUser()).isEqualTo(testUserForNewPayment);
        assertThat(createdPayment.getGoods()).hasSize(testGoods.size());

        // Удаляем созданный платеж
        paymentRepository.deleteById(createdPayment.getId());
        // Удаляем созданного пользователя
        userRepository.deleteById(testUserForNewPayment.getId());
    }

    /**
     * Тест для получения платежа по его идентификатору.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testGetPaymentById() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", testPayment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPayment.getId()))
                .andExpect(jsonPath("$.totalPurchaseAmount").value(300))
                .andExpect(jsonPath("$.userId").value(testUser.getId().toString()))
                .andExpect(jsonPath("$.goods", hasSize(testGoods.size())));
    }

    /**
     * Тест для обновления платежа по идентификатору.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testUpdatePaymentById() throws Exception {
        // Создаем новый товар
        Good updateTestGood3 = new Good();
        updateTestGood3.setName("Test Good 3");
        updateTestGood3.setType(GoodTypesEnum.OTHER);
        updateTestGood3.setDescription("Test Description 3");
        updateTestGood3.setPrice(BigDecimal.valueOf(3));
        updateTestGood3.setStockQuantity(3L);
        updateTestGood3.setArchiveDate(null);

        Set<Good> updateSetGoods = new HashSet<>();
        updateSetGoods.add(updateTestGood3);
        updateSetGoods = new HashSet<>(goodRepository.saveAll(updateSetGoods));

        // Обновляем платеж
        PaymentRequestDto updateRequest = new PaymentRequestDto();
        updateRequest.setTotalPurchaseAmount(BigDecimal.valueOf(1100.99));
        updateRequest.setUserId(testUser.getId());
        updateRequest.setGoods(goodMapper.toGoodResponseDtos(updateSetGoods));

        mockMvc.perform(put(PATH + "/{id}", testPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Проверяем обновление
        Payment updatedPayment = paymentRepository.findById(testPayment.getId()).orElseThrow();
        assertThat(updatedPayment.getTotalPurchaseAmount()).isEqualTo(BigDecimal.valueOf(1100.99));
        assertThat(updatedPayment.getUser()).isEqualTo(testUser);
        assertThat(updatedPayment.getGoods()).hasSize(1);

        // Удаляем созданные данные
        goodRepository.deleteAll(updateSetGoods);
    }

    /**
     * Тест для архивирования платежа по идентификатору.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testArchivePaymentById() throws Exception {
        mockMvc.perform(delete(PATH + "/archive/{id}", testPayment.getId()))
                .andExpect(status().isOk());

        // Проверяем, что платеж архивирован
        Payment archivedPayment = paymentRepository.findById(testPayment.getId()).orElseThrow();
        assertThat(archivedPayment.getArchiveDate()).isNotNull();

        // Проверяем удаление связи с товарами
        assertThat(archivedPayment.getGoods()).isEmpty();
    }

    /**
     * Тест для проверки обработки ситуации, когда платеж с указанным идентификатором не найден.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testGetPaymentByIdNotFound() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", 9999))
                .andExpect(status().isNotFound());
    }
}