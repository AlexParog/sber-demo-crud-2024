package com.sber.democrud.service.impl;

import com.sber.democrud.dto.PaymentRequestDto;
import com.sber.democrud.dto.PaymentResponseDto;
import com.sber.democrud.entity.Payment;
import com.sber.democrud.entity.User;
import com.sber.democrud.exception.NotFoundException;
import com.sber.democrud.mapper.PaymentMapper;
import com.sber.democrud.repository.PaymentRepository;
import com.sber.democrud.repository.UserRepository;
import com.sber.democrud.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Реализация сервисного слоя для управления платежами.
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    /**
     * Логгер.
     */
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    /**
     * Репозиторий платежей.
     */
    private final PaymentRepository paymentRepository;
    /**
     * Репозиторий пользователей.
     */
    private final UserRepository userRepository;
    /**
     * Маппер для сущности {@link Payment}.
     */
    private final PaymentMapper paymentMapper;

    /**
     * Конструктор сервиса {@link PaymentServiceImpl}.
     *
     * @param paymentRepository репозиторий для работы с платежами.
     * @param userRepository    репозиторий для работы с пользователями.
     * @param paymentMapper     маппер для преобразования между сущностями и DTO.
     */
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              UserRepository userRepository,
                              PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Создаёт новый платеж, связывая его с пользователем.
     *
     * @param paymentRequestDto DTO с данными для создания платежа.
     * @return созданный {@link PaymentResponseDto}.
     * @throws NotFoundException, если пользователь с указанным ID не найден.
     */
    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
        log.info("Создание платежа: {}", paymentRequestDto);

        Payment payment = paymentMapper.toPayment(paymentRequestDto);

        // Находим пользователя, связанного с платежом
        User userPayment = userRepository.findById(paymentRequestDto.getUserId())
                .orElseThrow(() -> {
                    log.error("Пользователь с ID: {} не найден", paymentRequestDto.getUserId());
                    return new NotFoundException("Пользователь с id={0} не найден", paymentRequestDto.getUserId());
                });
        log.debug("Пользователь найден: {}", userPayment);
        payment.setUser(userPayment);

//        // Устанавливаем связь между пользователем и платежом, приводят к ConcurrentModificationException
//        userPayment.addPayment(payment);
//
//        // Связываем товары с платежом
//        Set<Good> goodsInPayment = payment.getGoods();
//        goodsInPayment.forEach(payment::addGood);

        // Сохраняем платёж
        paymentRepository.save(payment);

        PaymentResponseDto responseDto = paymentMapper.toPaymentResponseDto(payment);
        log.info("Платеж успешно создан с ID: {}", responseDto.getId());
        return responseDto;
    }

    /**
     * Получает платёж по его идентификатору.
     *
     * @param id идентификатор платежа.
     * @return {@link PaymentResponseDto}, соответствующий найденному платежу.
     * @throws NotFoundException, если платёж не найден.
     */
    @Override
    public PaymentResponseDto getPaymentById(Long id) {
        log.info("Получение платежа с ID: {}", id);

        Payment payment = findPaymentOrNotFound(id);
        log.debug("Платеж найден: {}", payment);

        return paymentMapper.toPaymentResponseDto(payment);
    }

    /**
     * Обновляет существующий платёж по идентификатору.
     * Кроме пользователя, так как в совершенной покупке нельзя поменять данные того, кто произвел оплату.
     *
     * @param id                идентификатор платежа.
     * @param paymentRequestDto DTO с новыми данными для платежа.
     * @return обновлённый {@link PaymentResponseDto}.
     * @throws NotFoundException, если платёж не найден.
     */
    @Override
    public PaymentResponseDto updatePaymentById(Long id, PaymentRequestDto paymentRequestDto) {
        log.info("Обновление платежа с ID: {} данными: {}", id, paymentRequestDto);

        Payment currentPayment = findPaymentOrNotFound(id);
        paymentMapper.updatePaymentFromDto(paymentRequestDto, currentPayment);

        paymentRepository.save(currentPayment);
        log.info("Платеж с ID: {} успешно обновлен", id);

        return paymentMapper.toPaymentResponseDto(currentPayment);
    }

    /**
     * Архивирует платеж, устанавливая дату архивации.
     *
     * @param id идентификатор платежа.
     * @return обновлённый {@link PaymentResponseDto}.
     * @throws NotFoundException, если платёж не найден.
     */
    @Override
    public PaymentResponseDto archivePaymentById(Long id) {
        log.info("Архивирование платежа с ID: {}", id);

        Payment payment = findPaymentOrNotFound(id);

        // Разрываем связь между платежом и товарами, приводят к ConcurrentModificationException
//        Set<Good> goodsInPayment = payment.getGoods();
//        goodsInPayment.forEach(payment::removeGood);

        payment.setArchiveDate(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info("Платеж с ID: {} успешно архивирован на дату: {}", id, payment.getArchiveDate());
        return paymentMapper.toPaymentResponseDto(payment);
    }

    /**
     * Поиск платежа по ID.
     *
     * @param id идентификатор платежа.
     * @return найденный {@link Payment}.
     * @throws NotFoundException, если платёж не найден.
     */
    private Payment findPaymentOrNotFound(Long id) {
        log.debug("Поиск платежа с ID: {}", id);

        return paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Платеж с ID: {} не найден", id);
                    return new NotFoundException("Платеж c id={0} не найден", id);
                });
    }
}