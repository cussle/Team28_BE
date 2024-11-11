-- 채팅 관련 schema.sql (line 1~39)

-- 채팅방(CHAT ROOM) 스키마
CREATE TABLE IF NOT EXISTS chat_room (
    id BIGINT AUTO_INCREMENT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_message VARCHAR(255),
    last_message_time TIMESTAMP,
    PRIMARY KEY (id)
    );

-- 채팅 메시지(CHAT MESSAGE) 스키마
CREATE TABLE IF NOT EXISTS chat_message (
    id BIGINT AUTO_INCREMENT,
    content VARCHAR(2000),
    sender VARCHAR(255),
    `timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    chat_room_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_chat_room FOREIGN KEY (chat_room_id) REFERENCES chat_room(id)
    );

-- 채팅방 참가자(CHAT ROOM PARTICIPANTS) 스키마
CREATE TABLE IF NOT EXISTS chat_room_participants (
    chat_room_id BIGINT,
    participants_id BIGINT,
    PRIMARY KEY (chat_room_id, participants_id),
    CONSTRAINT fk_participants_user FOREIGN KEY (participants_id) REFERENCES member(id),
    CONSTRAINT fk_participants_chat_room FOREIGN KEY (chat_room_id) REFERENCES chat_room(id)
    );

-- 명함(Card) 테이블 스키마
CREATE TABLE IF NOT EXISTS card (
    id BIGINT AUTO_INCREMENT,
    company VARCHAR(255),
    position VARCHAR(255),
    phone VARCHAR(255),
    bio VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
    );

-- 회원(Member) 테이블 스키마
CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(255),
    github_id VARCHAR(255),
    nickname VARCHAR(255),
    profile_img VARCHAR(255),
    role VARCHAR(50),
    username VARCHAR(255),
    PRIMARY KEY (id)
    );

-- 그룹(Groups) 테이블 스키마
CREATE TABLE IF NOT EXISTS `groups` (
    id BIGINT AUTO_INCREMENT,
    `name` VARCHAR(255),
    member_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_group_member FOREIGN KEY (member_id) REFERENCES member(member_id)
    );

-- 그룹 카드(Group_Card) 테이블 스키마
CREATE TABLE IF NOT EXISTS group_card (
    group_id BIGINT,
    card_id BIGINT,
    PRIMARY KEY (group_id, card_id),
    CONSTRAINT fk_group_card_group FOREIGN KEY (group_id) REFERENCES `groups`(id),
    CONSTRAINT fk_group_card_card FOREIGN KEY (card_id) REFERENCES card(id)
    );