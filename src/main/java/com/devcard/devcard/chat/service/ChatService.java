package com.devcard.devcard.chat.service;

import static com.devcard.devcard.chat.util.Constants.CHAT_ROOM_NOT_FOUND;

import com.devcard.devcard.chat.exception.room.ChatRoomNotFoundException;
import com.devcard.devcard.chat.model.ChatMessage;
import com.devcard.devcard.chat.model.ChatRoom;
import com.devcard.devcard.chat.repository.ChatRepository;
import com.devcard.devcard.chat.repository.ChatRoomRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 실시간 채팅 관련 로직을 처리
 * 사용자가 채팅방에 참여하는 로직
 * 메시지를 주고받는 로직
 */
@Service
@Transactional
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    // id를 통해 채팅방을 관리 및 id 와 List<WebSocketSession>을 통해 해당 채팅방의 각 세션 즉 사용자 관리 아래 세션리스트 serivice계층으로 이동
    private static final ConcurrentMap<Long, List<WebSocketSession>> chatRoomSessions = new ConcurrentHashMap<>();

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatService(ChatRepository chatRepository, ChatRoomRepository chatRoomRepository) {
        this.chatRepository = chatRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    /**
     * DB에 채팅을 저장하고 해당 채팅방에 연결된 모든 WebSocket 세션에 메시지 전송
     * @param chatId  채팅방 ID
     * @param userId  사용자 ID
     * @param message 전송하려는 메시지
     */
    public void handleIncomingMessage(Long chatId, Long userId, String message) {
        // ChatRoom 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
            .orElseThrow(() -> new ChatRoomNotFoundException(CHAT_ROOM_NOT_FOUND + chatId));

        // 메시지 저장
        ChatMessage chatMessage = new ChatMessage(chatRoom, "user_" + userId, message, LocalDateTime.now());
        chatRepository.save(chatMessage);

        // 채팅방에 연결된 모든 WebSocket 세션에 메시지 전송
        List<WebSocketSession> sessions = getChatRoomSessions(chatId);
        if (sessions != null) {
            for (WebSocketSession webSocketSession : sessions) {
                if (webSocketSession.isOpen()) {
                    try {
                        webSocketSession.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        logger.error("메시지 전송 실패: {}", webSocketSession.getId(), e);
                    }
                }
            }
        }
    }

    /**
     * 특정 채팅방에 WebSocketSession 추가
     * @param chatId  채팅방 ID
     * @param session 추가할 WebSocketSession
     */
    public void addSessionToChatRoom(Long chatId, WebSocketSession session) {
        // computIfAbsent메소드: 키 값이 없으면 해당되는 키값으로 생성 해 리턴, 있다면 해당 값 리턴
        chatRoomSessions.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>()).add(session);
    }

    /**
     * 특정 채팅방에 WebSocketSession 제거
     * @param chatId  채팅방 ID
     * @param session 추가할 WebSocketSession
     */
    public void removeSessionFromChatRoom(Long chatId, WebSocketSession session) {
        List<WebSocketSession> sessions = chatRoomSessions.get(chatId);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    /**
     * 특정 채팅방에 연결된 모든 WebSocketSession 리스트 반환
     * @param chatId 채팅방 ID
     * @return 해당 채팅방에 연결된 세션 목록
     */
    public List<WebSocketSession> getChatRoomSessions(Long chatId) {
        return chatRoomSessions.get(chatId);
    }

    /**
     * 메시지 payload에서 chatId 추출
     * @param payload 메시지의 payload
     * @return 추출된 chatId 값 (존재하지 않으면 null 반환)
     */
    public Long extractChatId(String payload) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(payload);
            String chatIdStr = jsonObject.getAsString("chatId");
            if (chatIdStr != null) {
                return Long.parseLong(chatIdStr);
            }
            return null;
        } catch (ParseException | NumberFormatException e) {
            logger.error("payload에서 chatId 추출 실패: {}", payload, e);
            return null; // 예외 발생 시 null 반환
        }
    }

    /**
     * 메시지 payload에서 message 값 추출
     * @param payload 메시지의 payload
     * @return 추출된 메시지 내용 (존재하지 않으면 null 반환)
     */
    public String extractMessage(String payload) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(payload);
            return jsonObject.getAsString("message");
        } catch (ParseException e) {
            logger.error("payload에서 message 추출 실패: {}", payload, e);
            return null; // 예외 발생 시 null 반환
        }
    }

    /**
     * WebSocketSession에서 chatId를 추출
     * @param session WebSocketSession 객체
     * @return 추출된 chatId 값
     */
    public Long extractChatIdFromSession(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        return extractParamFromUri(uri, "chatId");
    }

    /**
     * WebSocketSession에서 userId를 추출
     * @param session WebSocketSession 객체
     * @return 추출된 userId 값
     */
    public Long extractUserIdFromSession(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        return extractParamFromUri(uri, "userId");
    }

    /**
     * URI에서 특정 파라미터 값을 추출
     * @param uri       WebSocket URI 문자열
     * @param paramName 추출할 파라미터 이름
     * @return 해당 파라미터 값 (존재하지 않으면 null 반환)
     */
    private Long extractParamFromUri(String uri, String paramName) {
        // e.g. `ws://localhost:8080/ws?chatId=1&userId=1` 입력의 경우,
        try {
            // "?"로 나누어 쿼리 파라미터 부분만 가져옴 (e.g. `chatId=1&userId=1`)
            String[] parts = uri.split("\\?");
            if (parts.length < 2) {
                logger.warn("쿼리 파라미터 없음: {}", uri);
                return null;
            }
            // 쿼리 부분을 "&"로 나누어 매개변수 배열로 변환
            return Stream.of(parts[1].split("&"))  // 쿼리 문자열에서 &로 분리 (e.g. `["chatId=1", "userId=1"]`)
                .map(param -> param.split("="))  // =으로 key-value 분리 (e.g. `[["chatId", "1"], ["userId", "1"]])`
                .filter(values -> values.length == 2
                    && paramName.equals(values[0]))  // paramName(userId 또는 chatId) 필터링 (e.g. `["chatId", "1"]`)
                .map(pair -> Long.parseLong(pair[1]))  // 값 추출 후 Long으로 변환 (e.g. `[1]`)
                .findFirst()  // 값 가져오기 (e.g. `1`)
                .orElse(null);  // 없으면 null 반환
        } catch (NumberFormatException e) {
            logger.error("URI에서 {} 추출 실패: {}", paramName, uri, e);
            return null;
        }
    }
}
