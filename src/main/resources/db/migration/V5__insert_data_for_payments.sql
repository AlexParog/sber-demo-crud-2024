-- заполнение таблицы Payments
INSERT INTO payments (id, user_id, total_purchase_amount, created_at)
VALUES (1, 'bc31c9b2-49d8-4d1c-9f7b-c40bfbcbce1d', 2999.98,
        CURRENT_TIMESTAMP),                                                -- Alex купил по одному экземпляру MacBook и iPhone
       (2, '4545ec5b-f3b8-40d3-91cd-8f8d8efc3922', 65.99, CURRENT_TIMESTAMP), -- Bob купил по одному экземпляру всех книг
       (3, '0b4e7b98-8b73-4d4b-af81-64d58499be78', 49.98, CURRENT_TIMESTAMP); -- Julia купила по одному экземпляру Polo и T-shirt


-- заполнение кросс-таблицы для связи many-to-many таблиц Payment, Good
INSERT INTO payment_goods (payment_id, good_id, quantity)
VALUES (1, 1, 1),
       (1, 2, 1),
       (2, 5, 1),
       (2, 6, 1),
       (2, 7, 1),
       (3, 3, 1),
       (3, 4, 1);