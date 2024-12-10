package com.sber.democrud.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
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
 * Класс представляет сущность "Платёж" в системе.
 */
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    /**
     * Уникальный идентификатор платежа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата совершения покупки.
     */
    @Column(name = "date_of_purchase", insertable = false, updatable = false)
    private LocalDateTime dateOfPurchase;

    /**
     * Общая сумма платежа.
     */
    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Column(name = "total_purchase_amount", nullable = false)
    private BigDecimal totalPurchaseAmount;

    /**
     * Дата архивации платежа.
     */
    @Nullable
    @Column(name = "archive_date")
    private LocalDateTime archiveDate;

    /**
     * Дата создания записи о платеже.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата последнего обновления записи о платеже.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Пользователь, связанный с этим платежом.
     * Устанавливается через связь "многие к одному" с сущностью {@link User}.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Товары, связанные с этим платежом.
     * Устанавливается через связь "многие ко многим" с сущностью {@link Good}.
     * Для хранения связи используется промежуточная таблица `payment_goods`.
     */
    @ToString.Exclude
    @NotNull
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "payment_goods",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "good_id"))
    private Set<Good> goods = new HashSet<>();

    /**
     * Добавляет товар в платёж и устанавливает двунаправленную связь.
     *
     * @param good объект товара, который добавляется к платежу.
     */
    public void addGood(Good good) {
        goods.add(good);
        good.goodsInPayments.add(this);
    }

    /**
     * Удаляет товар из платежа и разрывает двунаправленную связь.
     *
     * @param good объект товара, который удаляется из платежа.
     */
    public void removeGood(Good good) {
        goods.remove(good);
        good.goodsInPayments.remove(this);
    }

    /**
     * Проверяет, является ли покупка "удалённой".
     * Покупка считается удалённой, если поле {@code archiveDate} не равно {@code null}.
     *
     * @return {@code true}, если покупка удалена, иначе {@code false}
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
        Payment payment = (Payment) o;
        return getId() != null && Objects.equals(getId(), payment.getId());
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
