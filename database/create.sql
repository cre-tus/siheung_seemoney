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