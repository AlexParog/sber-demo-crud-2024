package com.sber.democrud.mappings;

import com.sber.democrud.dto.UserRequestDto;
import com.sber.democrud.dto.UserResponseDto;
import com.sber.democrud.entity.*;
import com.sber.democrud.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserMappingTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void userToUserResponseDtoTest() {
        User user = getUser();

        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);

        assertThat(userResponseDto).isNotNull();
        // обязательные поля при маппинге
        assertThat(userResponseDto.getId()).isEqualTo(user.getId());
        assertThat(userResponseDto.getName()).isEqualTo(user.getName());
        assertThat(userResponseDto.getLogin()).isEqualTo(user.getLogin());
        assertThat(userResponseDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponseDto.getRole()).isEqualTo(user.getRole().getValue());
        assertThat(userResponseDto.getArchiveDate()).isEqualTo(user.getArchiveDate());
        // игнорируемые поля
        assertThat(userResponseDto.getPassword()).isNotEqualTo(user.getPassword());
        // вложенное поле payments
        assertThat(userResponseDto.getPayments()).isNotEmpty();
        assertThat(userResponseDto.getPayments().size()).isEqualTo(user.getPayments().size());
    }

    @Test
    void userRequestDtoToUserTest() {
        UserRequestDto userRequestDto = getUserRequestDto();

        User user = userMapper.toUser(userRequestDto);

        assertThat(user).isNotNull();
        // обязательные поля при маппинге
        assertThat(user.getName()).isEqualTo(userRequestDto.getName());
        assertThat(user.getLogin()).isEqualTo(userRequestDto.getLogin());
        assertThat(user.getPassword()).isEqualTo(userRequestDto.getPassword());
        assertThat(user.getEmail()).isEqualTo(userRequestDto.getEmail());
        assertThat(user.getRole()).isEqualTo(UserRolesEnum.valueOf(userRequestDto.getRole()));
        assertThat(user.getArchiveDate()).isEqualTo(userRequestDto.getArchiveDate());
        // игнорируемые поля
        assertThat(user.getId()).isNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
        assertThat(user.getPayments()).isEmpty();
    }

    @Test
    void updateUserFromUserRequestDtoTest() {
        User user = getUser();
        int preMappingSizePayments = user.getPayments().size();
        UserRequestDto userRequestDto = getUserRequestDto();

        userMapper.updateUserFromDto(userRequestDto, user);

        assertThat(user).isNotNull();
        // обязательные поля
        assertThat(user.getName()).isEqualTo(userRequestDto.getName());
        assertThat(user.getLogin()).isEqualTo(userRequestDto.getLogin());
        assertThat(user.getPassword()).isEqualTo(userRequestDto.getPassword());
        assertThat(user.getEmail()).isEqualTo(userRequestDto.getEmail());
        assertThat(user.getRole()).isEqualTo(UserRolesEnum.valueOf(userRequestDto.getRole()));
        assertThat(user.getArchiveDate()).isEqualTo(userRequestDto.getArchiveDate());
        // игнорируемые поля
        assertThat(user.getId()).isNotNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
        // вложенное поле payments
        assertThat(user.getPayments()).isNotEmpty();
        assertThat(user.getPayments().size()).isEqualTo(preMappingSizePayments);
    }

    protected static User getUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Egor");
        user.setLogin("egorgudini");
        user.setPassword("egorthebest");
        user.setEmail("egortop100@gmail.com");
        user.setRole(UserRolesEnum.USER);
        user.setArchiveDate(null);

        Set<Payment> payments = new HashSet<>();

        // Первый платеж с двумя товарами
        Payment payment1 = new Payment();
        payment1.setId(1L);
        payment1.setUser(user);
        payment1.setTotalPurchaseAmount(new BigDecimal("50.99"));
        payment1.setArchiveDate(null);

        // Создаем два товара
        Set<Good> goodsForPayment1 = new HashSet<>();
        Good good1 = new Good();
        good1.setId(1L);
        good1.setName("Good1");
        good1.setType(GoodTypesEnum.ELECTRONICS);
        good1.setDescription("Description for Good1");
        good1.setPrice(new BigDecimal("20.99"));
        good1.setStockQuantity(10L);
        good1.setArchiveDate(null);

        Good good2 = new Good();
        good2.setId(2L);
        good2.setName("Good2");
        good2.setType(GoodTypesEnum.OTHER);
        good2.setDescription("Description for Good2");
        good2.setPrice(new BigDecimal("30.00"));
        good2.setStockQuantity(5L);
        good2.setArchiveDate(null);

        goodsForPayment1.add(good1);
        goodsForPayment1.add(good2);
        payment1.setGoods(goodsForPayment1);

        // Второй платеж с одним товаром
        Payment payment2 = new Payment();
        payment2.setId(2L);
        payment2.setUser(user);
        payment2.setTotalPurchaseAmount(new BigDecimal("15.00"));
        payment2.setArchiveDate(null);

        // Создаем один товар
        Set<Good> goodsForPayment2 = new HashSet<>();
        Good good3 = new Good();
        good3.setId(3L);
        good3.setName("Good3");
        good3.setType(GoodTypesEnum.CLOTHING);
        good3.setDescription("Description for Good3");
        good3.setPrice(new BigDecimal("15.00"));
        good3.setStockQuantity(20L);
        good3.setArchiveDate(null);

        goodsForPayment2.add(good3);
        payment2.setGoods(goodsForPayment2);

        // Добавляем платежи в множество
        payments.add(payment1);
        payments.add(payment2);
        user.setPayments(payments);

        return user;
    }

    protected static UserRequestDto getUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Alex");
        userRequestDto.setLogin("alexparog");
        userRequestDto.setPassword("password123");
        userRequestDto.setEmail("alex@gmail.com");
        userRequestDto.setRole("ADMIN");
        userRequestDto.setArchiveDate(null);

        return userRequestDto;
    }
}
