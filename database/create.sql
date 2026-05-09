SET NAMES utf8mb4;

CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,

                       address VARCHAR(255) NOT NULL,

                       point INT NOT NULL DEFAULT 0,
                       user_grade VARCHAR(30) NOT NULL DEFAULT 'BRONZE',

                       vote_count INT NOT NULL DEFAULT 0,
                       proposal_count INT NOT NULL DEFAULT 0,
                       ranking_point INT NOT NULL DEFAULT 0,

                       role VARCHAR(20) NOT NULL DEFAULT 'USER',

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================
-- Budget Domain.
-- =========================

CREATE TABLE IF NOT EXISTS budget_category (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    display_order INT NOT NULL
    ) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS budget_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year INT NOT NULL,
    category_id BIGINT NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    department_name VARCHAR(100) NOT NULL,
    detail_name TEXT,
    amount BIGINT NOT NULL,
    note TEXT,
    FOREIGN KEY (category_id) REFERENCES budget_category(id)
    ) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;