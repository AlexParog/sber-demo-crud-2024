package com.sber.democrud.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Класс представляет сущность "Пользователь" в системе.
 */
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    /**
     * Имя пользователя.
     */
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Логин пользователя.
     */
    @Size(max = 50, message = "Максимальная длина логина превышена < 50.")
    @NotBlank
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /**
     * Пароль пользователя.
     */
    @ToString.Exclude
    @NotBlank
    @Size(min = 5, message = "Минимальная длина пароля не соблюдена > 5.")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Электронная почта пользователя.
     */
    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Роль пользователя в системе.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRolesEnum role;

    /**
     * Дата архивирования пользователя.
     */
    @Nullable
    @Column(name = "archive_date")
    private LocalDateTime archiveDate;

    /**
     * Дата создания записи о пользователе.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата последнего обновления записи о пользователе.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Список платежей, связанных с пользователем, связь "один-ко-многим".
     */
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    protected Set<Payment> payments = new HashSet<>();

    /**
     * Добавляет платёж к пользователю и устанавливает двунаправленную связь.
     *
     * @param payment объект платежа, который нужно добавить
     */
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setUser(this);
    }

    /**
     * Удаляет платёж, связанный с пользователем, и удаляет двунаправленную связь.
     *
     * @param payment объект платежа, который нужно удалить
     */
    public void removePayment(Payment payment) {
        payment.setUser(null);
        this.payments.remove(payment);
    }

    /**
     * Проверяет, является ли пользователь "удалённым".
     * Пользователь считается удалённым, если поле {@code archiveDate} не равно {@code null}.
     *
     * @return {@code true}, если пользователь удалён, иначе {@code false}
     */
    public boolean isDeleted() {
        return archiveDate != null;
    }

    /**
     * Сравнивает текущий объект с другим объектом на равенство.
     * Сравнение выполняется на основе идентификатора {@code id}.
     *
     * @param o объект для сравнения
     * @return {@code true}, если объекты равны, иначе {@code false}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer()
                .getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    /**
     * Вычисляет хэш-код объекта на основе его класса и прокси.
     *
     * @return хэш-код объекта
     */
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode()
                : getClass().hashCode();
    }
}
