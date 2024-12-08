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

/**
 * Тестовый класс для проверки маппинга между сущностью {@link Good},
 * объектами DTO ({@link GoodRequestDto} и {@link GoodResponseDto}), а также коллекциями этих объектов.
 * <p>
 * Тестируются следующие сценарии:
 * <ul>
 *     <li>Маппинг сущности {@link Good} в {@link GoodResponseDto}</li>
 *     <li>Маппинг {@link GoodRequestDto} в сущность {@link Good}</li>
 *     <li>Обновление существующей сущности {@link Good} из DTO {@link GoodRequestDto}</li>
 *     <li>Маппинг коллекций сущностей в коллекции DTO и наоборот</li>
 * </ul>
 */
@SpringBootTest
public class GoodsMappingTest {

    /**
     * Автоматически внедряемый бин маппера {@link GoodMapper}.
     * Используется для проведения маппинга между сущностями {@link Good} и DTO.
     */
    @Autowired
    private GoodMapper goodMapper;

    /**
     * Тестирует маппинг сущности {@link Good} в DTO {@link GoodResponseDto}.
     * <p>
     * Проверяет корректность заполнения обязательных полей, а также их соответствие
     * данным исходной сущности.
     */
    @Test
    void goodToGoodResponseDtoTest() {
        Good good = getGood();

        GoodResponseDto goodResponseDto = goodMapper.toGoodResponseDto(good);

        assertThat(goodResponseDto).isNotNull();

        // Проверка обязательных полей при маппинге
        assertThat(goodResponseDto.getId()).isEqualTo(good.getId());
        assertThat(goodResponseDto.getName()).isEqualTo(good.getName());
        assertThat(goodResponseDto.getType()).isEqualTo(good.getType().getValue());
        assertThat(goodResponseDto.getDescription()).isEqualTo(good.getDescription());
        assertThat(goodResponseDto.getPrice()).isEqualTo(good.getPrice());
        assertThat(goodResponseDto.getStockQuantity()).isEqualTo(good.getStockQuantity());
        assertThat(goodResponseDto.getArchiveDate()).isEqualTo(good.getArchiveDate());
    }

    /**
     * Тестирует маппинг DTO {@link GoodRequestDto} в сущность {@link Good}.
     * <p>
     * Проверяет корректность переноса обязательных полей, а также игнорирование тех
     * полей, которые не должны быть заполнены на основе DTO.
     */
    @Test
    void goodRequestDtoToGoodTest() {
        GoodRequestDto goodRequestDto = getGoodRequestDto();

        Good good = goodMapper.toGood(goodRequestDto);

        assertThat(good).isNotNull();

        // Проверяем обязательные поля
        assertThat(good.getName()).isEqualTo(goodRequestDto.getName());
        assertThat(good.getType()).isEqualTo(GoodTypesEnum.valueOf(goodRequestDto.getType()));
        assertThat(good.getDescription()).isEqualTo(goodRequestDto.getDescription());
        assertThat(good.getPrice()).isEqualTo(goodRequestDto.getPrice());
        assertThat(good.getStockQuantity()).isEqualTo(goodRequestDto.getStockQuantity());
        assertThat(good.getArchiveDate()).isEqualTo(goodRequestDto.getArchiveDate());

        // Проверка игнорируемых полей
        assertThat(good.getId()).isNull();
        assertThat(good.getCreatedAt()).isNull();
        assertThat(good.getUpdatedAt()).isNull();
        assertThat(good.getGoodsInPayments()).isEmpty();
    }

    /**
     * Тестирует обновление существующей сущности {@link Good} данными из DTO {@link GoodRequestDto}.
     * <p>
     * Проверяет, что поля обновляются корректно, при этом поля, которые
     * не должны изменяться, остаются нетронутыми.
     */
    @Test
    void updateGoodFromGoodRequestDtoTest() {
        Good good = getGood();
        GoodRequestDto goodRequestDto = getGoodRequestDto();

        goodMapper.updateGoodFromDto(goodRequestDto, good);

        assertThat(good).isNotNull();

        // Проверка обязательных полей, которые обновляются
        assertThat(good.getName()).isEqualTo(goodRequestDto.getName());
        assertThat(good.getType()).isEqualTo(GoodTypesEnum.valueOf(goodRequestDto.getType()));
        assertThat(good.getDescription()).isEqualTo(goodRequestDto.getDescription());
        assertThat(good.getPrice()).isEqualTo(goodRequestDto.getPrice());
        assertThat(good.getStockQuantity()).isEqualTo(goodRequestDto.getStockQuantity());
        assertThat(good.getArchiveDate()).isEqualTo(goodRequestDto.getArchiveDate());

        // Проверка неизменных полей
        assertThat(good.getId()).isNotNull();

        // Проверка игнорируемых полей
        assertThat(good.getCreatedAt()).isNull();
        assertThat(good.getUpdatedAt()).isNull();
        assertThat(good.getGoodsInPayments()).isEmpty();
    }

    /**
     * Тестирует маппинг коллекции сущностей {@link Good} в коллекцию DTO {@link GoodResponseDto}.
     * <p>
     * Проверяет, что все сущности корректно преобразуются и соответствуют исходным данным.
     */
    @Test
    void setGoodsToGoodResponseDtosTest() {
        Set<Good> goods = getSetGoods();

        Set<GoodResponseDto> goodResponseDtos = goodMapper.toGoodResponseDtos(goods);

        Map<Long, Good> goodMap = goods.stream()
                .collect(Collectors.toMap(Good::getId, Function.identity()));

        for (GoodResponseDto dto : goodResponseDtos) {
            assertThat(dto).isNotNull();

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

    /**
     * Тестирует маппинг коллекции DTO {@link GoodResponseDto} в коллекцию сущностей {@link Good}.
     * <p>
     * Проверяет, что все DTO корректно преобразуются и данные соответствуют исходным.
     */
    @Test
    void setGoodResponseDtosToGoodsTest() {
        Set<GoodResponseDto> goodResponseDtos = getSetGoodResponseDtos();

        Set<Good> goods = goodMapper.toGoods(goodResponseDtos);

        Map<Long, GoodResponseDto> dtosMap = goodResponseDtos.stream()
                .collect(Collectors.toMap(GoodResponseDto::getId, Function.identity()));

        for (Good good : goods) {
            assertThat(good).isNotNull();

            GoodResponseDto dto = dtosMap.get(good.getId());
            assertThat(dto).isNotNull();

            // Проверяем корректность маппинга
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
     * Создаёт тестовую коллекцию сущностей {@link Good}.
     *
     * @return коллекция объектов {@link Good}.
     */
    protected static Set<Good> getSetGoods() {
        Set<Good> goods = new HashSet<>();

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

    /**
     * Создаёт тестовую коллекцию DTO {@link GoodResponseDto}.
     *
     * @return коллекция объектов {@link GoodResponseDto}.
     */
    protected static Set<GoodResponseDto> getSetGoodResponseDtos() {
        Set<GoodResponseDto> goodResponseDtos = new HashSet<>();

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

    /**
     * Создаёт тестовую сущность {@link Good}.
     *
     * @return объект {@link Good} с заполненными тестовыми данными.
     */
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

    /**
     * Создаёт тестовый объект DTO {@link GoodRequestDto}.
     *
     * @return объект {@link GoodRequestDto} с заполненными тестовыми данными.
     */
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