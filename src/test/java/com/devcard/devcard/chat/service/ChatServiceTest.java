package com.devcard.devcard.chat.service;

import static com.devcard.devcard.chat.util.Constants.CHAT_ROOM_NOT_FOUND;
import static com.devcard.devcard.chat.util.Constants.EMPTY_MESSAGE;
import static com.devcard.devcard.chat.util.Constants.USER_NOT_IN_CHAT_ROOM;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.repository.MemberRepository;
import com.devcard.devcard.chat.exception.room.ChatRoomNotFoundException;
import com.devcard.devcard.chat.model.ChatMessage;
import com.devcard.devcard.chat.model.ChatRoom;
import com.devcard.devcard.chat.repository.ChatRepository;
import com.devcard.devcard.chat.repository.ChatRoomRepository;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WebSocketSession session;

    @InjectMocks
    private ChatService chatService;

    private ChatRoom chatRoom;
    private Member member;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // chatRoomSessions 필드를 리플렉션을 사용하여 초기화
        Field sessionsField = ChatService.class.getDeclaredField("chatRoomSessions");
        sessionsField.setAccessible(true);
        ((ConcurrentMap<Long, List<WebSocketSession>>) sessionsField.get(null)).clear();

        // Member 객체를 모킹하여 ID 설정
        member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        when(member.getNickname()).thenReturn("nickname");

        // ChatRoom 객체를 모킹하여 ID 설정 및 참여자 설정
        chatRoom = mock(ChatRoom.class);
        when(chatRoom.getId()).thenReturn(1L);
        when(chatRoom.getParticipants()).thenReturn(List.of(member));

        // chatRoomRepository가 특정 chatId를 반환하도록 설정
        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));

        // WebSocketSession을 chatRoom에 추가
        chatService.addSessionToChatRoom(chatRoom.getId(), session);
    }

    @Test
    @DisplayName("메시지 처리 성공")
    void testHandleIncomingMessage_Success() {
        ChatMessage chatMessage = mock(ChatMessage.class);

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        when(chatRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        assertDoesNotThrow(() -> chatService.handleIncomingMessage(1L, member.getId(), "Hello"));
    }

    @Test
    @DisplayName("존재하지 않는 채팅방")
    void testHandleIncomingMessage_ChatRoomNotFound() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
            ChatRoomNotFoundException.class,
            () -> chatService.handleIncomingMessage(1L, member.getId(), "Hello")
        );
        assertEquals(CHAT_ROOM_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("메시지 저장 오류")
    void testHandleIncomingMessage_MessageSaveError() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        doThrow(new RuntimeException("DB 저장 오류")).when(chatRepository).save(any(ChatMessage.class));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> chatService.handleIncomingMessage(1L, member.getId(), "Hello")
        );
        assertEquals("DB 저장 오류", exception.getMessage());
    }

    @Test
    @DisplayName("유효하지 않은 userId")
    void testHandleIncomingMessage_InvalidUserId_Null() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> chatService.handleIncomingMessage(1L, null, "Hello")
        );
        assertEquals(USER_NOT_IN_CHAT_ROOM, exception.getMessage());
    }

    @Test
    @DisplayName("빈 메시지")
    void testHandleIncomingMessage_EmptyMessage_Null() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> chatService.handleIncomingMessage(1L, member.getId(), null)
        );
        assertEquals(EMPTY_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("첫 번째 시도에 메시지 전송 성공")
    void testHandleIncomingMessage_SuccessOnFirstTry() throws IOException {
        // session이 열려 있음을 설정
        when(session.isOpen()).thenReturn(true);

        // 메시지 전송이 성공적으로 이루어지도록 설정
        doNothing().when(session).sendMessage(any(TextMessage.class));

        // 메시지 처리 메서드 호출
        chatService.handleIncomingMessage(chatRoom.getId(), member.getId(), "Test Message");

        // session.sendMessage가 한 번 호출되었는지 확인
        verify(session, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    @DisplayName("두 번째 시도에 메시지 전송 성공")
    void testHandleIncomingMessage_RetryAfterFailure() throws IOException {
        // session이 열려 있음을 설정
        when(session.isOpen()).thenReturn(true);

        // 첫 번째 시도에서 IOException 발생, 두 번째 시도에서 성공
        doThrow(new IOException("첫 번째 시도 실패"))
            .doNothing()
            .when(session).sendMessage(any(TextMessage.class));

        // 메시지 처리 메서드 호출
        chatService.handleIncomingMessage(chatRoom.getId(), member.getId(), "Test Message");

        // session.sendMessage가 총 두 번 호출되었는지 확인
        verify(session, times(2)).sendMessage(any(TextMessage.class));
    }

    @Test
    @DisplayName("모든 전송 시도 실패")
    void testHandleIncomingMessage_AllRetriesFail() throws IOException {
        // session이 열려 있음을 설정
        when(session.isOpen()).thenReturn(true);

        // 모든 시도에서 IOException 발생
        doThrow(new IOException("메시지 전송 실패"))
            .when(session).sendMessage(any(TextMessage.class));

        // 메시지 처리 메서드 호출
        chatService.handleIncomingMessage(chatRoom.getId(), member.getId(), "Test Message");

        // session.sendMessage가 재시도 횟수만큼 호출되었는지 확인 (3회)
        verify(session, times(3)).sendMessage(any(TextMessage.class));
    }

    @Test
    @DisplayName("새로운 채팅방에 세션 추가")
    void testAddSessionToNewChatRoom() {
        Long newChatId = 2L;
        WebSocketSession newSession = mock(WebSocketSession.class);

        // 새로운 채팅방에 세션 추가
        chatService.addSessionToChatRoom(newChatId, newSession);

        // chatRoomSessions에 새로운 chatId가 존재하고 해당 세션이 추가되었는지 확인
        assertTrue(chatService.getChatRoomSessions().containsKey(newChatId));
        assertTrue(chatService.getChatRoomSessions(newChatId).contains(newSession));
    }

    @Test
    @DisplayName("기존 채팅방에 세션 추가")
    void testAddSessionToExistingChatRoom() {
        WebSocketSession newSession = mock(WebSocketSession.class);

        // 기존 chatId에 새로운 세션 추가
        chatService.addSessionToChatRoom(chatRoom.getId(), newSession);

        // 기존 chatId 리스트에 새로운 세션이 추가되었는지 확인
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(chatRoom.getId());
        assertTrue(sessions.contains(newSession));
    }

    @Test
    @DisplayName("존재하는 세션 제거")
    void testRemoveSessionFromChatRoom_ExistingSession() {
        // 기존 chatId에 session이 존재하는 경우 제거 시도
        chatService.removeSessionFromChatRoom(chatRoom.getId(), session);

        // 세션이 제거되었는지 확인
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(chatRoom.getId());
        assertTrue(sessions == null || !sessions.contains(session));
    }

    @Test
    @DisplayName("존재하지 않는 세션 제거")
    void testRemoveSessionFromChatRoom_NonExistentSession() {
        WebSocketSession nonExistentSession = mock(WebSocketSession.class);

        // 존재하지 않는 session을 제거 시도
        chatService.removeSessionFromChatRoom(chatRoom.getId(), nonExistentSession);

        // 아무런 변경이 없는지 확인
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(chatRoom.getId());
        assertTrue(sessions != null && !sessions.contains(nonExistentSession));
    }

    @Test
    @DisplayName("존재하지 않는 채팅방에서 세션 제거")
    void testRemoveSessionFromChatRoom_NonExistentChatRoom() {
        Long nonExistentChatId = 999L;

        // 존재하지 않는 chatId에 session을 제거 시도
        chatService.removeSessionFromChatRoom(nonExistentChatId, session);

        // chatRoomSessions에 해당 chatId가 존재하지 않는지 확인
        assertFalse(chatService.getChatRoomSessions().containsKey(nonExistentChatId));
    }

    @Test
    @DisplayName("빈 세션 리스트에서 세션 제거")
    void testRemoveSessionFromChatRoom_EmptySessionList() {
        Long emptyChatId = 2L;
        WebSocketSession testSession = mock(WebSocketSession.class);

        // 빈 세션 리스트에서 존재하지 않는 세션 제거 시도
        chatService.removeSessionFromChatRoom(emptyChatId, testSession);

        // 세션 리스트가 비어 있거나 chatRoomSessions에 emptyChatId가 존재하지 않는지 확인
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(emptyChatId);
        assertTrue(sessions == null || sessions.isEmpty());
    }

    @Test
    @DisplayName("session이 null인 경우")
    void testRemoveSessionFromChatRoom_NullSession() {
        // session이 null인 경우 제거 시도
        chatService.removeSessionFromChatRoom(chatRoom.getId(), null);

        // chatRoomSessions에 아무런 변경이 없는지 확인
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(chatRoom.getId());
        assertTrue(sessions != null && sessions.contains(session));
    }

    @Test
    @DisplayName("기존 채팅방의 세션 리스트 반환")
    void testGetChatRoomSessions_ExistingChatRoom() {
        Long chatId = 1L;
        WebSocketSession session1 = mock(WebSocketSession.class);
        WebSocketSession session2 = mock(WebSocketSession.class);

        // 기존 채팅방에 세션 추가
        chatService.addSessionToChatRoom(chatId, session1);
        chatService.addSessionToChatRoom(chatId, session2);

        // 세션 리스트를 가져오고, 세션이 포함되어 있는지 확인
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(chatId);
        assertNotNull(sessions);
        assertTrue(sessions.contains(session1));
        assertTrue(sessions.contains(session2));
    }

    @Test
    @DisplayName("없는 채팅방의 세션 리스트 요청")
    void testGetChatRoomSessions_NonExistentChatRoom() {
        Long nonExistentChatId = 999L;

        // 존재하지 않는 채팅방의 세션 리스트를 요청하여 null 반환 확인
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(nonExistentChatId);
        assertNull(sessions);
    }

    @Test
    @DisplayName("빈 세션 리스트 반환")
    void testGetChatRoomSessions_EmptySessionList() {
        Long emptyChatId = 2L;
        WebSocketSession testSession = mock(WebSocketSession.class);
        when(testSession.getId()).thenReturn("test-session");

        // 세션 추가 후 제거하여 빈 리스트 상태로 만듦
        chatService.addSessionToChatRoom(emptyChatId, testSession);
        chatService.removeSessionFromChatRoom(emptyChatId, testSession);

        // 세션 리스트가 비어있거나, chatRoomSessions에 emptyChatId가 존재하지 않는지 확인
        List<WebSocketSession> sessions = chatService.getChatRoomSessions(emptyChatId);
        assertTrue(sessions == null || sessions.isEmpty());
    }

    @Test
    @DisplayName("유효한 JSON payload")
    void testExtractMessage_ValidPayload() {
        String payload = "{\"content\": \"Hello\"}";
        String result = chatService.extractMessage(payload);
        assertEquals("Hello", result);
    }

    @Test
    @DisplayName("content 필드가 없는 JSON payload")
    void testExtractMessage_NoContentField() {
        String payload = "{\"message\": \"Hi\"}";
        String result = chatService.extractMessage(payload);
        assertNull(result);
    }

    @Test
    @DisplayName("잘못된 JSON 형식의 payload")
    void testExtractMessage_InvalidJsonFormat() {
        String payload = "Invalid JSON";
        String result = chatService.extractMessage(payload);
        assertNull(result);
    }

    @Test
    @DisplayName("빈 JSON 객체")
    void testExtractMessage_EmptyJsonObject() {
        String payload = "{}";
        String result = chatService.extractMessage(payload);
        assertNull(result);
    }
}
