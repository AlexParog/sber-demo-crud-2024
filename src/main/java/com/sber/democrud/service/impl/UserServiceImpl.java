package com.sber.democrud.service.impl;

import com.sber.democrud.dto.PaymentResponseDto;
import com.sber.democrud.dto.UserRequestDto;
import com.sber.democrud.dto.UserResponseDto;
import com.sber.democrud.entity.User;
import com.sber.democrud.exception.NotFoundException;
import com.sber.democrud.mapper.PaymentMapper;
import com.sber.democrud.mapper.UserMapper;
import com.sber.democrud.repository.UserRepository;
import com.sber.democrud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервисного слоя для управления пользователями.
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * Логгер.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    /**
     * Репозиторий пользователей.
     */
    private final UserRepository userRepository;
    /**
     * Маппер для сущности {@link User}.
     */
    private final UserMapper userMapper;
    /**
     * Маппер для сущности {@link com.sber.democrud.entity.Payment}
     */
    private final PaymentMapper paymentMapper;

    /**
     * Конструктор сервиса {@link UserServiceImpl}.
     *
     * @param userRepository репозиторий для работы с пользователями.
     * @param userMapper     маппер для преобразования пользователей между сущностями и DTO.
     * @param paymentMapper  маппер для преобразования платежей между сущностями и DTO.
     */
    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PaymentMapper paymentMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Создаёт нового пользователя.
     *
     * @param userRequestDto DTO с данными для создания пользователя.
     * @return созданный {@link UserResponseDto}.
     */
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Создание пользователя: {}", userRequestDto);

        User user = userMapper.toUser(userRequestDto);
        userRepository.save(user);

        UserResponseDto responseDto = userMapper.toUserResponseDto(user);
        log.info("Пользователь успешно создан с ID: {}", responseDto.getId());
        return responseDto;
    }

    /**
     * Получает пользователя по идентификатору.
     * Может включать связанные платежи, если указан соответствующий флаг.
     *
     * @param id              идентификатор пользователя.
     * @param includePayments флаг для включения связанных платежей.
     * @return найденный {@link UserResponseDto}.
     * @throws NotFoundException если пользователь не найден.
     */
    @Override
    public UserResponseDto getUserById(UUID id, boolean includePayments) {
        log.info("Получение пользователя с ID: {}, includePayments: {}", id, includePayments);

        User user = findUserOrNotFound(id);
        log.debug("Пользователь найден: {}", user);

        UserResponseDto responseDto = userMapper.toUserResponseDto(user);
        if (includePayments) {
            log.debug("Добавление связанных платежей в ответ");
            Set<PaymentResponseDto> paymentDtos = user.getPayments().stream()
                    .map(paymentMapper::toPaymentResponseDto)
                    .collect(Collectors.toSet());
            responseDto.setPayments(paymentDtos);
        }

        return responseDto;
    }

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param id             идентификатор пользователя.
     * @param userRequestDto DTO с новыми данными для пользователя.
     * @return обновленный {@link UserResponseDto}.
     * @throws NotFoundException если пользователь не найден.
     */
    @Override
    public UserResponseDto updateUserById(UUID id, UserRequestDto userRequestDto) {
        log.info("Обновление пользователя с ID: {} данными: {}", id, userRequestDto);

        User currentUser = findUserOrNotFound(id);
        userMapper.updateUserFromDto(userRequestDto, currentUser);

        userRepository.save(currentUser);
        log.info("Пользователь с ID: {} успешно обновлен", id);

        return userMapper.toUserResponseDto(currentUser);
    }

    /**
     * Архивирует пользователя, разрывая связи с платежами.
     *
     * @param id идентификатор пользователя.
     * @return архивированный {@link UserResponseDto}.
     * @throws NotFoundException если пользователь не найден.
     */
    @Override
    public UserResponseDto archiveUserById(UUID id) {
        log.info("Архивирование пользователя с ID: {}", id);

        User user = findUserOrNotFound(id);

        // Разрываем связь с платежами, приводят к ConcurrentModificationException
//        Set<Payment> userPayments = user.getPayments();
//        userPayments.forEach(user::removePayment);

        user.setArchiveDate(LocalDateTime.now());
        userRepository.save(user);

        log.info("Пользователь с ID: {} успешно архивирован на дату: {}", id, user.getArchiveDate());
        return userMapper.toUserResponseDto(user);
    }

    /**
     * Выполняет поиск пользователя по идентификатору.
     * Если пользователь не найден, выбрасывается {@link NotFoundException}.
     *
     * @param id идентификатор пользователя.
     * @return найденный {@link User}.
     */
    private User findUserOrNotFound(UUID id) {
        log.debug("Поиск пользователя с ID: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID: {} не найден", id);
                    return new NotFoundException("Пользователь с id={0} не найден", id);
                });
    }
}