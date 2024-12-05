INSERT INTO users (name, login, password, email, role, created_at)
VALUES ('Alex', 'pro100alex', 'password123', 'proalex@mail.com', 'USER', CURRENT_TIMESTAMP),
       ('Bob', 'prr100admin', 'cooladmin123', 'bob@example.com', 'ADMIN', CURRENT_TIMESTAMP),
       ('Julia', 'qtjulia', 'cqgirl', 'julia495@example.com', 'USER', CURRENT_TIMESTAMP);

INSERT INTO goods (name, type, description, price, stock_quantity, created_at)
VALUES ('MacBook', 'ELECTRONICS', 'Apple laptop', 1999.99, 10, CURRENT_TIMESTAMP),
       ('iPhone 17', 'ELECTRONICS', 'Apple smartphone', 999.99, 20, CURRENT_TIMESTAMP),
       ('T-shirt', 'CLOTHING', 'Off White T-Shirt', 19.99, 100, CURRENT_TIMESTAMP),
       ('Polo', 'CLOTHING', 'Ralph Lauren Polo', 29.99, 50, CURRENT_TIMESTAMP),
       ('Effective Java', 'BOOKS', 'Joshua J. Bloch is an American software engineer and a technology author', 20.50,
        100, CURRENT_TIMESTAMP),
       ('Java Concurrency in Practice', 'BOOKS',
        'Brian Goetz is a software consultant with twenty years industry experience, with over 75 articles on Java development',
        25.50, 5, CURRENT_TIMESTAMP),
       ('Грокаем алгоритмы', 'BOOKS',
        'Иллюстрированное пособие для программистов и любопытствующих от Бхаргава А. Эта книга рекомендована Яндекс Практикум при подготовке к алгоритмическому собеседованию',
        19.99, 100, CURRENT_TIMESTAMP),
       ('iCloud Subscription', 'OTHER', 'iCloud cloud subscription', 5.99, 1000, CURRENT_TIMESTAMP),
       ('Apple Music Subscription', 'OTHER', 'Streaming service subscription', 3.99, 1000, CURRENT_TIMESTAMP);

