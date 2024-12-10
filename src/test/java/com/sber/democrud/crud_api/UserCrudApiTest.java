package com.sber.democrud.crud_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.democrud.repository.PaymentRepository;
import com.sber.democrud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserCrudApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Тест для проверки обработки ситуации, когда пользователь с указанным идентификатором не найден.
     *
     * @throws Exception если происходит ошибка при выполнении запроса
     */
    @Test
    void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 9999))
                .andExpect(status().isNotFound());
    }
}
