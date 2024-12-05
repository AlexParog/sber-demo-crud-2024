CREATE TABLE users
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         VARCHAR(225)       NOT NULL,
    login        varchar(50) UNIQUE NOT NULL,
    password     VARCHAR(255)       NOT NULL,
    email        VARCHAR(50) UNIQUE NOT NULL,
    role         VARCHAR(20)        NOT NULL,
    archive_date TIMESTAMP,
    created_at   TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    update_at    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE goods
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255)   NOT NULL,
    type           varchar(20)    NOT NULL,
    description    TEXT           NOT NULL,
    price          NUMERIC(10, 2) NOT NULL,
    stock_quantity BIGINT         NOT NULL,
    archive_date   TIMESTAMP,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments
(
    id                    BIGSERIAL PRIMARY KEY,
    user_id               UUID           NOT NULL,
    date_of_purchase      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_purchase_amount NUMERIC(10, 2) NOT NULL,
    archive_date          TIMESTAMP,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment_goods
(
    id         BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL,
    good_id    BIGINT NOT NULL,
    quantity   BIGINT NOT NULL
);

-- one-to-many связь для user и payments
ALTER TABLE payments
    ADD CONSTRAINT fk_user_to_payments
        FOREIGN KEY (user_id) REFERENCES users (id);

-- many-to-many связь для payments и goods
ALTER TABLE ONLY payment_goods
    ADD CONSTRAINT good_id_to_payment_id_fk
        FOREIGN KEY (payment_id) REFERENCES payments (id);
ALTER TABLE ONLY payment_goods
    ADD CONSTRAINT payment_id_to_good_id_fk
        FOREIGN KEY (good_id) REFERENCES goods (id);