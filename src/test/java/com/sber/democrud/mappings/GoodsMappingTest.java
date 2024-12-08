package com.sber.democrud.mappings;

import com.sber.democrud.dto.GoodRequestDto;
import com.sber.democrud.dto.GoodResponseDto;
import com.sber.democrud.entity.Good;
import com.sber.democrud.entity.GoodTypesEnum;
import com.sber.democrud.mapper.GoodMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GoodsMappingTest {
    @Autowired
    private GoodMapper goodMapper;

    @Test
    void goodToGoodResponseDtoTest() {
        Good good = getGood();

        GoodResponseDto goodResponseDto = goodMapper.toGoodResponseDto(good);

        assertThat(goodResponseDto).isNotNull();
        // обязательные поля при маппинге
        assertThat(goodResponseDto.getId()).isEqualTo(good.getId());
        assertThat(goodResponseDto.getName()).isEqualTo(good.getName());
        assertThat(goodResponseDto.getType()).isEqualTo(good.getType().getValue());
        assertThat(goodResponseDto.getDescription()).isEqualTo(good.getDescription());
        assertThat(goodResponseDto.getPrice()).isEqualTo(good.getPrice());
        assertThat(goodResponseDto.getStockQuantity()).isEqualTo(good.getStockQuantity());
        assertThat(goodResponseDto.getArchiveDate()).isEqualTo(good.getArchiveDate());
    }

    @Test
    void goodRequestDtoToGoodTest() {
        GoodRequestDto goodRequestDto = getGoodRequestDto();

        Good good = goodMapper.toGood(goodRequestDto);

        assertThat(good).isNotNull();
        // обязательные поля
        assertThat(good.getName()).isEqualTo(goodRequestDto.getName());
        assertThat(good.getType()).isEqualTo(GoodTypesEnum.valueOf(goodRequestDto.getType()));
        assertThat(good.getDescription()).isEqualTo(goodRequestDto.getDescription());
        assertThat(good.getPrice()).isEqualTo(goodRequestDto.getPrice());
        assertThat(good.getStockQuantity()).isEqualTo(goodRequestDto.getStockQuantity());
        assertThat(good.getArchiveDate()).isEqualTo(goodRequestDto.getArchiveDate());
        // игнорируемые поля
        assertThat(good.getId()).isNull();
        assertThat(good.getCreatedAt()).isNull();
        assertThat(good.getUpdatedAt()).isNull();
        assertThat(good.getGoodsInPayments()).isEmpty();
    }

    @Test
    void updateGoodFromGoodRequestDtoTest() {
        Good good = getGood();
        GoodRequestDto goodRequestDto = getGoodRequestDto();

        goodMapper.updateGoodFromDto(goodRequestDto, good);

        assertThat(good).isNotNull();
        // обязательные поля, которые мы меняем
        assertThat(good.getName()).isEqualTo(goodRequestDto.getName());
        assertThat(good.getType()).isEqualTo(GoodTypesEnum.valueOf(goodRequestDto.getType()));
        assertThat(good.getDescription()).isEqualTo(goodRequestDto.getDescription());
        assertThat(good.getPrice()).isEqualTo(goodRequestDto.getPrice());
        assertThat(good.getStockQuantity()).isEqualTo(goodRequestDto.getStockQuantity());
        assertThat(good.getArchiveDate()).isEqualTo(goodRequestDto.getArchiveDate());
        // поля, которые были заполнены до маппинга
        assertThat(good.getId()).isNotNull();
        // игнорируемые поля
        assertThat(good.getCreatedAt()).isNull();
        assertThat(good.getUpdatedAt()).isNull();
        assertThat(good.getGoodsInPayments()).isEmpty();
    }

    @Test
    void setGoodsToGoodResponseDtosTest() {
        Set<Good> goods = getSetGoods();

        Set<GoodResponseDto> goodResponseDtos = goodMapper.toGoodResponseDtos(goods);

        // мапа для быстрого поиска Good по id
        Map<Long, Good> goodMap = goods.stream()
                .collect(Collectors.toMap(Good::getId, Function.identity()));

        for (GoodResponseDto dto : goodResponseDtos) {
            assertThat(dto).isNotNull();

            // Находим соответствующий Good по id
            Good good = goodMap.get(dto.getId());
            assertThat(good).isNotNull();

            // Проверяем корректность маппинга
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
    void setGoodResponseDtosToGoodsTest() {
        Set<GoodResponseDto> goodResponseDtos = getSetGoodResponseDtos();

        Set<Good> goods = goodMapper.toGoods(goodResponseDtos);

        Map<Long, GoodResponseDto> dtosMap = goodResponseDtos.stream()
                .collect(Collectors.toMap(GoodResponseDto::getId, Function.identity()));

        for (Good good : goods) {
            assertThat(good).isNotNull();

            // Находим соответствующую DTO по id
            GoodResponseDto dto = dtosMap.get(good.getId());
            assertThat(dto).isNotNull();

            // Проверяем обязательные поля после маппинга
            assertThat(good.getId()).isEqualTo(dto.getId());
            assertThat(good.getName()).isEqualTo(dto.getName());
            assertThat(good.getType()).isEqualTo(GoodTypesEnum.valueOf(dto.getType()));
            assertThat(good.getDescription()).isEqualTo(dto.getDescription());
            assertThat(good.getPrice()).isEqualTo(dto.getPrice());
            assertThat(good.getStockQuantity()).isEqualTo(dto.getStockQuantity());
            assertThat(good.getArchiveDate()).isEqualTo(dto.getArchiveDate());
        }

    }

    protected static Set<Good> getSetGoods() {
        Set<Good> goods = new HashSet<>();

        // Пример заполнения множества
        Good good1 = getGood();
        Good good2 = new Good();
        good2.setId(2L);
        good2.setName("Laptop");
        good2.setType(GoodTypesEnum.ELECTRONICS);
        good2.setDescription("High-performance laptop");
        good2.setPrice(new BigDecimal("1200.50"));
        good2.setStockQuantity(5L);
        good2.setArchiveDate(null);

        goods.add(good1);
        goods.add(good2);

        return goods;
    }

    protected static Set<GoodResponseDto> getSetGoodResponseDtos() {
        Set<GoodResponseDto> goodResponseDtos = new HashSet<>();

        // Пример заполнения множества
        GoodResponseDto dto1 = new GoodResponseDto();
        dto1.setId(1L);
        dto1.setName("Product");
        dto1.setType(GoodTypesEnum.OTHER.getValue());
        dto1.setDescription("Some description");
        dto1.setPrice(new BigDecimal("20.99"));
        dto1.setStockQuantity(10L);
        dto1.setArchiveDate(null);

        GoodResponseDto dto2 = new GoodResponseDto();
        dto2.setId(2L);
        dto2.setName("Laptop");
        dto2.setType(GoodTypesEnum.ELECTRONICS.getValue());
        dto2.setDescription("High-performance laptop");
        dto2.setPrice(new BigDecimal("1200.50"));
        dto2.setStockQuantity(5L);
        dto2.setArchiveDate(null);

        goodResponseDtos.add(dto1);
        goodResponseDtos.add(dto2);

        return goodResponseDtos;
    }


    protected static Good getGood() {
        Good good = new Good();
        good.setId(1L);
        good.setName("Product");
        good.setType(GoodTypesEnum.OTHER);
        good.setDescription("Some description");
        good.setPrice(new BigDecimal("20.99"));
        good.setStockQuantity(10L);
        good.setArchiveDate(null);
        return good;
    }

    protected static GoodRequestDto getGoodRequestDto() {
        GoodRequestDto goodRequestDto = new GoodRequestDto();
        goodRequestDto.setName("Book");
        goodRequestDto.setType(GoodTypesEnum.BOOKS.getValue());
        goodRequestDto.setDescription("Java Effective");
        goodRequestDto.setPrice(new BigDecimal("20.99"));
        goodRequestDto.setStockQuantity(5L);
        goodRequestDto.setArchiveDate(null);
        return goodRequestDto;
    }
}
