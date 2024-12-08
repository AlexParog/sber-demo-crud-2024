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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Реализация сервисного слоя для управления платежами.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

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
        Payment payment = paymentMapper.toPayment(paymentRequestDto);

        // Находим пользователя, связанного с платежом
        User userPayment = userRepository.findById(paymentRequestDto.getUserId())
                .orElseThrow(() ->
                        new NotFoundException("Пользователь c id={0} не найден", paymentRequestDto.getUserId()));
        payment.setUser(userPayment);

        // Устанавливаем связь между пользователем и платежом
        userPayment.addPayment(payment);

        // Связываем товары с платежом
        payment.getGoods().forEach(payment::addGood);

        // Сохраняем платёж
        paymentRepository.save(payment);

        return paymentMapper.toPaymentResponseDto(payment);
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
        Payment payment = findPaymentOrNotFound(id);
        return paymentMapper.toPaymentResponseDto(payment);
    }

    /**
     * Обновляет существующий платёж по идентификатору.
     *
     * @param id                идентификатор платежа.
     * @param paymentRequestDto DTO с новыми данными для платежа.
     * @return обновлённый {@link PaymentResponseDto}.
     * @throws NotFoundException, если платёж не найден.
     */
    @Override
    public PaymentResponseDto updatePaymentById(Long id, PaymentRequestDto paymentRequestDto) {
        Payment currentPayment = findPaymentOrNotFound(id);
        paymentMapper.updatePaymentFromDto(paymentRequestDto, currentPayment);
        paymentRepository.save(currentPayment);

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
        Payment payment = findPaymentOrNotFound(id);

        // Разрываем связь между платежом и товарами
        payment.getGoods().forEach(payment::removeGood);

        payment.setArchiveDate(LocalDateTime.now());
        paymentRepository.save(payment);

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
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Платёж c id={0} не найден", id));
    }
}