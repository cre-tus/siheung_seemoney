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


-- =========================
-- 게시글 테이블
-- =========================
CREATE TABLE posts (

    -- 내부 DB용 기본 키
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- 외부 공개용 UUID
    -- URL에 사용됨
    -- ex) /api/posts/550e8400-e29b-41d4-a716-446655440000
                       public_id CHAR(36) NOT NULL UNIQUE,

    -- 작성자 ID
    -- users 테이블과 연결 가능
                       user_id BIGINT NOT NULL,

    -- 작성자 닉네임
                       nickname VARCHAR(50) NOT NULL,

    -- 게시글 제목
                       title VARCHAR(200) NOT NULL,

    -- 게시글 본문 내용
                       content TEXT NOT NULL,

    -- 좋아요 수
                       like_count INT NOT NULL DEFAULT 0,

    -- 댓글 수
                       comment_count INT NOT NULL DEFAULT 0,

    -- 조회수
                       view_count INT NOT NULL DEFAULT 0,

    -- 생성 시간
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 수정 시간
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

    -- public_id 검색 최적화
                       INDEX idx_posts_public_id (public_id),

    -- 최신순 조회 최적화
                       INDEX idx_posts_created_at (created_at),

    -- 특정 유저 게시글 조회 최적화
                       INDEX idx_posts_user_id (user_id)
);



-- =========================
-- 댓글 테이블
-- =========================
CREATE TABLE comments (

    -- 댓글 기본 키
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- 어떤 게시글의 댓글인지
                          post_id BIGINT NOT NULL,

    -- 댓글 작성자 ID
                          user_id BIGINT NOT NULL,

    -- 댓글 작성자 닉네임
                          nickname VARCHAR(50) NOT NULL,

    -- 댓글 내용
                          content TEXT NOT NULL,

    -- 댓글 작성 시간
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 댓글 수정 시간
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP,

    -- 게시글 삭제 시 댓글도 자동 삭제
                          CONSTRAINT fk_comments_post
                              FOREIGN KEY (post_id) REFERENCES posts(id)
                                  ON DELETE CASCADE,

    -- 특정 게시글 댓글 조회 최적화
                          INDEX idx_comments_post_id (post_id)
);



-- =========================
-- 게시글 좋아요 테이블
-- =========================
CREATE TABLE post_likes (

    -- 좋아요 기본 키
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- 좋아요 누른 게시글
                            post_id BIGINT NOT NULL,

    -- 좋아요 누른 유저
                            user_id BIGINT NOT NULL,

    -- 좋아요 누른 시간
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 게시글 삭제 시 좋아요도 자동 삭제
                            CONSTRAINT fk_post_likes_post
                                FOREIGN KEY (post_id) REFERENCES posts(id)
                                    ON DELETE CASCADE,

    -- 한 유저가 같은 글에 중복 좋아요 못 누르게 제한
                            CONSTRAINT uq_post_likes_post_user
                                UNIQUE (post_id, user_id)
);



