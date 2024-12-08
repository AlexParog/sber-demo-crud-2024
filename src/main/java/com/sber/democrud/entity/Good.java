package com.sber.democrud.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Класс представляет сущность "Товар" в системе.
 */
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "goods")
public class Good {
    /**
     * Уникальный идентификатор товара.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название товара.
     */
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Тип товара.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GoodTypesEnum type;

    /**
     * Описание товара.
     */
    @NotBlank
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Цена товара.
     */
    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * Количество товара на складе.
     */
    @NotNull
    @Digits(integer = 5, fraction = 0)
    @Column(name = "stock_quantity", nullable = false)
    private Long stockQuantity;

    /**
     * Дата архивирования товара.
     */
    @Nullable
    @Column(name = "archive_date")
    private LocalDateTime archiveDate;

    /**
     * Дата создания товара.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата последнего обновления записи о товаре.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Список платежей, связанных с этим товаром.
     * Это связь "многие ко многим" с сущностью {@link Payment}.
     */
    @ManyToMany(mappedBy = "goods")
    protected Set<Payment> goodsInPayments = new HashSet<>();

    /**
     * Проверяет, является ли товар "удалённым".
     * Товар считается удалённым, если поле {@code archiveDate} не равно {@code null}.
     *
     * @return {@code true}, если товар удалён, иначе {@code false}
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
        Good good = (Good) o;
        return getId() != null && Objects.equals(getId(), good.getId());
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
