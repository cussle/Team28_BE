-- 회원 관련 data.sql (line 1~6)
-- 예시 회원 데이터
INSERT INTO member (member_id, create_date, email, github_id, nickname, profile_img, role, username) VALUES
    (1, CURRENT_TIMESTAMP, 'member1@example.com', 'github1', 'Member1', 'profile1.jpg', 'USER', 'member1'),
    (2, CURRENT_TIMESTAMP, 'member2@example.com', 'github2', 'Member2', 'profile2.jpg', 'USER', 'member2'),
    (3, CURRENT_TIMESTAMP, 'member3@example.com', 'github3', 'Member3', 'profile3.jpg', 'USER', 'member3');

-- 채팅 관련 data.sql (line 8~25)
-- 예시 채팅방 데이터
INSERT INTO chat_room (created_at, last_message, last_message_time) VALUES
    (CURRENT_TIMESTAMP, 'Hello, world!', CURRENT_TIMESTAMP),
    (CURRENT_TIMESTAMP, '새로운 채팅방 메시지~', CURRENT_TIMESTAMP);

-- 예시 채팅 메시지 데이터
INSERT INTO chat_message (content, sender, timestamp, chat_room_id) VALUES
    ('안녕하세요', 'Member1', CURRENT_TIMESTAMP, 1),
    ('새로운 채팅방 메시지~', 'Member1', CURRENT_TIMESTAMP, 2),
    ('Hello, world!', 'Member2', CURRENT_TIMESTAMP + 0.1, 1);

-- 예시 채팅방 참가자 데이터
INSERT INTO chat_room_participants (chat_room_id, participants_member_id) VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3);

-- 명함(Card) 테이블에 예시 데이터 추가
-- INSERT INTO card (github_id, name, company, position, email, phone, profile_picture, bio) VALUES
--     ('hong-gildong', '어피치', 'DevCompany', '백엔드 개발자', 'hong@example.com', '010-1234-5678', 'profile.jpg', '열정적인 개발자입니다.'),
--     ('hong-gildong', '춘식이', 'kakaoCompany', '백엔드 주니어 개발자', 'chun@kakaoTech.com', '010-7894-456', 'profile.jpg', '소통하는 개발자입니다.');
