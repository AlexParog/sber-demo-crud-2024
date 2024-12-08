package com.sber.democrud.repository;

import com.sber.democrud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для управления сущностью {@link User}.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Находит всех архивированных пользователей.
     *
     * @return список архивированных пользователей с установленной датой архивации.
     */
    List<User> findByArchiveDateIsNotNull();
}

