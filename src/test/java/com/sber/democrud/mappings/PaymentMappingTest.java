package com.sber.democrud.mappings;

import com.sber.democrud.dto.GoodResponseDto;
import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.dto.PaymentResponseDto;
import com.sber.democrud.entity.*;
import com.sber.democrud.mapper.PaymentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестовый класс для проверки маппинга между сущностью {@link Payment}, DTO объектами
 * {@link PaymentRequestDto} и {@link PaymentResponseDto}, а также обратного преобразования.
 * <p>
 * Проверяются следующие сценарии:
 * <ul>
 *     <li>Маппинг сущности {@link Payment} в DTO {@link PaymentResponseDto}</li>
 *     <li>Маппинг DTO {@link PaymentRequestDto} в сущность {@link Payment}</li>
 *     <li>Обновление сущности {@link Payment} данными из {@link PaymentRequestDto}</li>
 * </ul>
 */
@SpringBootTest
public class PaymentMappingTest {

    /**
     * Автоматически внедряемый бин маппера {@link PaymentMapper}.
     * Используется для проведения маппинга между сущностью {@link Payment} и DTO объектами.
     */
    @Autowired
    private PaymentMapper paymentMapper;

    /**
     * Тестирует маппинг сущности {@link Payment} в объект {@link PaymentResponseDto}.
     * <p>
     * Проверяет корректность переноса обязательных, вложенных полей, а также их соответствие
     * ожидаемым значениям. Дополнительно проверяется, что маппинг товаров в платеже выполняется правильно.
     */
    @Test
    void paymentToPaymentResponseDtoTest() {
        Payment payment = getPayment();

        PaymentResponseDto paymentResponseDto = paymentMapper.toPaymentResponseDto(payment);

        // Проверяем обязательные поля
        assertThat(paymentResponseDto).isNotNull();
        assertThat(paymentResponseDto.getId()).isEqualTo(payment.getId());
        assertThat(paymentResponseDto.getUserId()).isEqualTo(payment.getUser().getId());
        assertThat(paymentResponseDto.getTotalPurchaseAmount()).isEqualTo(payment.getTotalPurchaseAmount());
        assertThat(paymentResponseDto.getArchiveDate()).isEqualTo(payment.getArchiveDate());

        // Проверяем вложенное поле goods
        assertThat(paymentResponseDto.getGoods()).isNotEmpty();
        assertThat(paymentResponseDto.getGoods().size()).isEqualTo(payment.getGoods().size());

        // Создаем мапу для сопоставления товаров
        Map<Long, Good> goodMap = payment.getGoods().stream()
                .collect(Collectors.toMap(Good::getId, Function.identity()));

        // Проверяем каждый товар
        for (GoodResponseDto dto : paymentResponseDto.getGoods()) {
            assertThat(dto).isNotNull();
            Good good = goodMap.get(dto.getId());
            assertThat(good).isNotNull();

            // Тестируем корректность маппинга
            assertThat(dto.getId()).isEqualTo(good.getId());
            assertThat(dto.getName()).isEqualTo(good.getName());
            assertThat(dto.getType()).isEqualTo(good.getType().getValue());
            assertThat(dto.getDescription()).isEqualTo(good.getDescription());
            assertThat(dto.getPrice()).isEqualTo(good.getPrice());
            assertThat(dto.getStockQuantity()).isEqualTo(good.getStockQuantity());
            assertThat(dto.getArchiveDate()).isEqualTo(good.getArchiveDate());
        }
    }

    /**
     * Тестирует маппинг DTO {@link PaymentRequestDto} в сущность {@link Payment}.
     * <p>
     * Проверяет соответствие обязательных, вложенных и игнорируемых полей между DTO и сущностью.
     */
    @Test
    void paymentRequestDtoToPaymentTest() {
        PaymentRequestDto paymentRequestDto = getPaymentRequestDto();

        Payment payment = paymentMapper.toPayment(paymentRequestDto);

        // Проверяем обязательные поля
        assertThat(payment).isNotNull();
        assertThat(payment.getTotalPurchaseAmount()).isEqualTo(paymentRequestDto.getTotalPurchaseAmount());
        assertThat(payment.getArchiveDate()).isEqualTo(paymentRequestDto.getArchiveDate());

        // Проверяем, что игнорируемые поля не заполнены
        assertThat(payment.getId()).isNull();
        assertThat(payment.getUser()).isNull();
        assertThat(payment.getDateOfPurchase()).isNull();
        assertThat(payment.getCreatedAt()).isNull();
        assertThat(payment.getUpdatedAt()).isNull();

        // Проверяем вложенное поле goods
        assertThat(payment.getGoods()).isNotEmpty();
        assertThat(payment.getGoods().size()).isEqualTo(paymentRequestDto.getGoods().size());

        Map<Long, GoodResponseDto> dtosMap = paymentRequestDto.getGoods().stream()
                .collect(Collectors.toMap(GoodResponseDto::getId, Function.identity()));

        // Проверяем каждый сопоставленный товар
        for (Good good : payment.getGoods()) {
            assertThat(good).isNotNull();
            GoodResponseDto dto = dtosMap.get(good.getId());
            assertThat(dto).isNotNull();

            // Тестируем корректность маппинга
            assertThat(good.getId()).isEqualTo(dto.getId());
            assertThat(good.getName()).isEqualTo(dto.getName());
            assertThat(good.getType()).isEqualTo(GoodTypesEnum.valueOf(dto.getType()));
            assertThat(good.getDescription()).isEqualTo(dto.getDescription());
            assertThat(good.getPrice()).isEqualTo(dto.getPrice());
            assertThat(good.getStockQuantity()).isEqualTo(dto.getStockQuantity());
            assertThat(good.getArchiveDate()).isEqualTo(dto.getArchiveDate());
        }
    }

    /**
     * Тестирует обновление существующей сущности {@link Payment} данными из
     * DTO {@link PaymentRequestDto}.
     * <p>
     * Проверяет, что поля платежа корректно обновляются, а также что новый товар
     * добавляется в список товаров.
     */
    @Test
    void updatePaymentFromPaymentRequestDtoTest() {
        Payment payment = getPayment();
        PaymentRequestDto paymentRequestDto = getPaymentRequestDto();

        // Добавляем новый товар в платеж
        GoodResponseDto newGoodDto = new GoodResponseDto();
        newGoodDto.setId(3L);
        newGoodDto.setName("Other");
        newGoodDto.setType(GoodTypesEnum.OTHER.name());
        newGoodDto.setDescription("Other description");
        newGoodDto.setPrice(new BigDecimal("1.99"));
        newGoodDto.setStockQuantity(1L);
        newGoodDto.setArchiveDate(null);

        Set<GoodResponseDto> updatedGoodsDto = new HashSet<>(paymentRequestDto.getGoods());
        updatedGoodsDto.add(newGoodDto);
        paymentRequestDto.setGoods(updatedGoodsDto);

        // Выполняем обновление
        paymentMapper.updatePaymentFromDto(paymentRequestDto, payment);

        // Проверяем обновленные поля
        assertThat(payment).isNotNull();
        assertThat(payment.getTotalPurchaseAmount()).isEqualTo(paymentRequestDto.getTotalPurchaseAmount());
        assertThat(payment.getArchiveDate()).isEqualTo(paymentRequestDto.getArchiveDate());

        // Проверяем, что новые товары добавлены
        assertThat(payment.getGoods()).isNotNull();
        assertThat(payment.getGoods().size()).isEqualTo(paymentRequestDto.getGoods().size());

        // Проверка на наличие нового товара
        assertThat(payment.getGoods().stream().anyMatch(g -> g.getId().equals(newGoodDto.getId()))).isTrue();
    }

    /**
     * Создаёт тестовую сущность {@link Payment} с подготовленным набором данных.
     *
     * @return объект {@link Payment}, содержащий основные поля и связанные товары.
     */
    protected static Payment getPayment() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setUser(createUser());
        payment.setTotalPurchaseAmount(new BigDecimal("50.99"));
        payment.setArchiveDate(null);
        payment.setGoods(GoodsMappingTest.getSetGoods());

        return payment;
    }

    /**
     * Создаёт тестовый DTO {@link PaymentRequestDto} с подготовленным набором данных.
     *
     * @return объект {@link PaymentRequestDto}, содержащий основные поля и связанные товары.
     */
    protected static PaymentRequestDto getPaymentRequestDto() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setUserId(UserMappingTest.getUser().getId());
        paymentRequestDto.setTotalPurchaseAmount(new BigDecimal("10.00"));
        paymentRequestDto.setArchiveDate(LocalDateTime.now());
        paymentRequestDto.setGoods(GoodsMappingTest.getSetGoodResponseDtos());

        return paymentRequestDto;
    }

    /**
     * Создаёт тестового пользователя {@link User} для использования в платежах.
     *
     * @return объект {@link User} с заполненными основными полями.
     */
    private static User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Vlad");
        user.setLogin("vladevo");
        user.setPassword("evelon");
        user.setEmail("vladevo@gmail.com");
        user.setRole(UserRolesEnum.USER);
        user.setArchiveDate(null);

        return user;
    }
}
