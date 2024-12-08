package com.sber.democrud.mappings;

import com.sber.democrud.dto.GoodResponseDto;
import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.dto.PaymentResponseDto;
import com.sber.democrud.entity.Good;
import com.sber.democrud.entity.GoodTypesEnum;
import com.sber.democrud.entity.Payment;
import com.sber.democrud.mapper.PaymentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaymentMappingTest {
    @Autowired
    private PaymentMapper paymentMapper;

    @Test
    void paymentToPaymentResponseDtoTest() {
        Payment payment = getPayment();

        PaymentResponseDto paymentResponseDto = paymentMapper.toPaymentResponseDto(payment);

        // обязательные поля
        assertThat(paymentResponseDto).isNotNull();
        assertThat(paymentResponseDto.getId()).isEqualTo(payment.getId());
        assertThat(paymentResponseDto.getUserId()).isEqualTo(payment.getUser().getId());
        assertThat(paymentResponseDto.getTotalPurchaseAmount()).isEqualTo(payment.getTotalPurchaseAmount());
        assertThat(paymentResponseDto.getArchiveDate()).isEqualTo(payment.getArchiveDate());

        // вложенное поле goods
        assertThat(paymentResponseDto.getGoods()).isNotEmpty();
        assertThat(paymentResponseDto.getGoods().size()).isEqualTo(payment.getGoods().size());

        // Создаем мапу для проверки товаров
        Map<Long, Good> goodMap = payment.getGoods().stream()
                .collect(Collectors.toMap(Good::getId, Function.identity()));

        for (GoodResponseDto dto : paymentResponseDto.getGoods()) {
            assertThat(dto).isNotNull();
            Good good = goodMap.get(dto.getId());
            assertThat(good).isNotNull();

            // Проверяем корректность маппинга каждого товара
            assertThat(dto.getId()).isEqualTo(good.getId());
            assertThat(dto.getName()).isEqualTo(good.getName());
            assertThat(dto.getType()).isEqualTo(good.getType().getValue());
            assertThat(dto.getDescription()).isEqualTo(good.getDescription());
            assertThat(dto.getPrice()).isEqualTo(good.getPrice());
            assertThat(dto.getStockQuantity()).isEqualTo(good.getStockQuantity());
            assertThat(dto.getArchiveDate()).isEqualTo(good.getArchiveDate());
        }
    }

    @Test
    void paymentRequestDtoToPaymentTest() {
        PaymentRequestDto paymentRequestDto = getPaymentRequestDto();

        Payment payment = paymentMapper.toPayment(paymentRequestDto);

        // обязательные поля
        assertThat(payment).isNotNull();
        assertThat(payment.getTotalPurchaseAmount()).isEqualTo(paymentRequestDto.getTotalPurchaseAmount());
        assertThat(payment.getArchiveDate()).isEqualTo(paymentRequestDto.getArchiveDate());

        // игнорируемые поля
        assertThat(payment.getId()).isNull();
        assertThat(payment.getUser()).isNull();
        assertThat(payment.getDateOfPurchase()).isNull();
        assertThat(payment.getCreatedAt()).isNull();
        assertThat(payment.getUpdatedAt()).isNull();

        // вложенное поле goods
        assertThat(payment.getGoods()).isNotEmpty();
        assertThat(payment.getGoods().size()).isEqualTo(paymentRequestDto.getGoods().size());

        Map<Long, GoodResponseDto> dtosMap = paymentRequestDto.getGoods().stream()
                .collect(Collectors.toMap(GoodResponseDto::getId, Function.identity()));

        for (Good good : payment.getGoods()) {
            assertThat(good).isNotNull();
            GoodResponseDto dto = dtosMap.get(good.getId());
            assertThat(dto).isNotNull();

            // Проверяем корректность маппинга каждого товара
            assertThat(good.getId()).isEqualTo(dto.getId());
            assertThat(good.getName()).isEqualTo(dto.getName());
            assertThat(good.getType()).isEqualTo(GoodTypesEnum.valueOf(dto.getType()));
            assertThat(good.getDescription()).isEqualTo(dto.getDescription());
            assertThat(good.getPrice()).isEqualTo(dto.getPrice());
            assertThat(good.getStockQuantity()).isEqualTo(dto.getStockQuantity());
            assertThat(good.getArchiveDate()).isEqualTo(dto.getArchiveDate());
        }
    }

    @Test
    void updatePaymentFromPaymentRequestDtoTest() {
        Payment payment = getPayment();
        PaymentRequestDto paymentRequestDto = getPaymentRequestDto();

        // Добавляем новый товар в DTO
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


        paymentMapper.updatePaymentFromDto(paymentRequestDto, payment);

        // Проверяем основные поля
        assertThat(payment).isNotNull();
        assertThat(payment.getTotalPurchaseAmount()).isEqualTo(paymentRequestDto.getTotalPurchaseAmount());
        assertThat(payment.getArchiveDate()).isEqualTo(paymentRequestDto.getArchiveDate());

        // игнорируемые поля
        assertThat(payment.getId()).isNotNull();
        assertThat(payment.getUser()).isNotNull();
        assertThat(payment.getDateOfPurchase()).isNull();
        assertThat(payment.getCreatedAt()).isNull();
        assertThat(payment.getUpdatedAt()).isNull();

        // Вложенное поле goods
        assertThat(payment.getGoods()).isNotNull();
        assertThat(payment.getGoods().size()).isEqualTo(paymentRequestDto.getGoods().size());

        Map<Long, GoodResponseDto> dtoMap = paymentRequestDto.getGoods().stream()
                .collect(Collectors.toMap(GoodResponseDto::getId, Function.identity()));

        for (Good good : payment.getGoods()) {
            assertThat(good).isNotNull();
            GoodResponseDto dto = dtoMap.get(good.getId());
            assertThat(dto).isNotNull();

            // Проверяем корректность маппинга каждого товара
            assertThat(good.getId()).isEqualTo(dto.getId());
            assertThat(good.getName()).isEqualTo(dto.getName());
            assertThat(good.getType()).isEqualTo(GoodTypesEnum.valueOf(dto.getType()));
            assertThat(good.getDescription()).isEqualTo(dto.getDescription());
            assertThat(good.getPrice()).isEqualTo(dto.getPrice());
            assertThat(good.getStockQuantity()).isEqualTo(dto.getStockQuantity());
            assertThat(good.getArchiveDate()).isEqualTo(dto.getArchiveDate());
        }

        // Проверяем, что новый товар добавлен
        assertThat(payment.getGoods().stream().anyMatch(g -> g.getId().equals(newGoodDto.getId()))).isTrue();
    }


    protected static Payment getPayment() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setUser(UserMappingTest.getUser());
        payment.setTotalPurchaseAmount(new BigDecimal("50.99"));
        payment.setArchiveDate(null);
        payment.setGoods(GoodsMappingTest.getSetGoods());

        return payment;
    }

    protected static PaymentRequestDto getPaymentRequestDto() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setUserId(UserMappingTest.getUser().getId());
        paymentRequestDto.setTotalPurchaseAmount(new BigDecimal("10.00"));
        paymentRequestDto.setArchiveDate(LocalDateTime.now());
        paymentRequestDto.setGoods(GoodsMappingTest.getSetGoodResponseDtos());

        return paymentRequestDto;
    }
}
