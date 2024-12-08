package com.sber.democrud.repository;

import com.sber.democrud.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}

