package com.sber.democrud.repository;

import com.sber.democrud.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Репозиторий для работы с сущностью {@link Good}.
 */
@Repository
public interface GoodRepository extends JpaRepository<Good, Long> {

    /**
     * Находит список товаров, которые были архивированы.
     * Возвращает только те товары, у которых поле {@code archiveDate} не равно {@code null}.
     *
     * @return список архивированных товаров.
     */
    List<Good> findByArchiveDateIsNotNull();

    Optional<Good> findByName(String name);
}

