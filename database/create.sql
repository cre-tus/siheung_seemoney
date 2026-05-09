SET NAMES utf8mb4;

-- =========================
-- 사용자 테이블
-- =========================
CREATE TABLE users (

    -- 사용자 고유 ID
    -- 내부 DB 기본 키
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- 로그인 이메일
    -- 중복 불가
                       email VARCHAR(100) NOT NULL UNIQUE,
    -- 암호화된 비밀번호 저장
    -- bcrypt 해시 문자열 저장 예정
                       password_hash VARCHAR(255) NOT NULL,

    -- 사용자 주소
    -- 시흥시 지역 인증 등에 사용 가능
                       address VARCHAR(255) NOT NULL,

    -- 현재 보유 포인트
    -- 활동 시 증가
                       point INT NOT NULL DEFAULT 0,

    -- 사용자 등급
    -- BRONZE / SILVER / GOLD 등
                       user_grade VARCHAR(30) NOT NULL DEFAULT 'BRONZE',

    -- 총 투표 참여 횟수
                       vote_count INT NOT NULL DEFAULT 0,

    -- 총 제안글 작성 횟수
                       proposal_count INT NOT NULL DEFAULT 0,

    -- 랭킹 계산용 점수
    -- 좋아요, 활동량 등 기반
                       ranking_point INT NOT NULL DEFAULT 0,

    -- 사용자 권한
    -- USER / ADMIN 등
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',

    -- 회원가입 시간
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 사용자 정보 수정 시간
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP
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
-- 유저활동 테이블
-- =========================
CREATE TABLE activities (

    -- 활동 로그 기본 키
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- 활동한 사용자
                            user_id BIGINT NOT NULL,

    -- 활동 타입
    -- vote / post / like / comment
                            type VARCHAR(20) NOT NULL,

    -- 활동 제목
    -- ex) "학교 급식 개선안에 공감했습니다"
                            title VARCHAR(255) NOT NULL,

    -- 포인트 변화
                            points INT NOT NULL DEFAULT 0,

    -- 연결된 게시글
                            post_id BIGINT NULL,

    -- 게시글 public UUID
                            post_public_id CHAR(36) NULL,

    -- 활동 생성 시간
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- 유저 활동 조회 최적화
                            INDEX idx_activities_user_id (user_id),

    -- 최신 활동 조회 최적화
                            INDEX idx_activities_created_at (created_at)
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



