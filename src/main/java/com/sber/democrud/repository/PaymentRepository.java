package com.sber.democrud.repository;

import com.sber.democrud.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для управления сущностью {@link Payment}.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Находит все архивированные платежи.
     *
     * @return список архивированных платежей (с установленной датой архивации).
     */
    List<Payment> findByArchiveDateIsNotNull();

    /**
     * Находит платеж по идентификатору пользователя.
     *
     * @param userId id пользователя
     * @return платеж пользователя.
     */
    Optional<Payment> findPaymentByUserId(UUID userId);
}

