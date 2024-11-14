import static com.devcard.devcard.chat.util.Constants.CHAT_ROOM_NOT_FOUND;
import static com.devcard.devcard.chat.util.Constants.EMPTY_MESSAGE;
import static com.devcard.devcard.chat.util.Constants.USER_NOT_IN_CHAT_ROOM;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.repository.MemberRepository;
import com.devcard.devcard.chat.exception.room.ChatRoomNotFoundException;
import com.devcard.devcard.chat.model.ChatMessage;
import com.devcard.devcard.chat.model.ChatRoom;
import com.devcard.devcard.chat.repository.ChatRepository;
import com.devcard.devcard.chat.repository.ChatRoomRepository;
import com.devcard.devcard.chat.service.ChatService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ChatService chatService;

    private ChatRoom chatRoom;
    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Member 객체를 모킹하여 ID 설정
        member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        when(member.getNickname()).thenReturn("nickname");

        // ChatRoom 생성
        chatRoom = new ChatRoom(List.of(member), LocalDateTime.now());
    }

    @Test
    @DisplayName("성공적인 메시지 처리 - 유효한 chatId, userId, message가 입력된 경우")
    void testHandleIncomingMessage_Success() {
        ChatMessage chatMessage = mock(ChatMessage.class);

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        when(chatRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        assertDoesNotThrow(() -> chatService.handleIncomingMessage(1L, member.getId(), "Hello"));
    }

    @Test
    @DisplayName("존재하지 않는 채팅방 - 존재하지 않는 chatId가 입력된 경우")
    void testHandleIncomingMessage_ChatRoomNotFound() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
            ChatRoomNotFoundException.class,
            () -> chatService.handleIncomingMessage(1L, member.getId(), "Hello")
        );
        assertEquals(CHAT_ROOM_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("메시지 저장 오류 - 메시지 저장 중 chatRepository에서 예외가 발생한 경우")
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
    @DisplayName("유효하지 않은 userId - userId가 null인 경우")
    void testHandleIncomingMessage_InvalidUserId_Null() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> chatService.handleIncomingMessage(1L, null, "Hello")
        );
        assertEquals(USER_NOT_IN_CHAT_ROOM, exception.getMessage());
    }

    @Test
    @DisplayName("빈 메시지 - message가 null로 입력된 경우")
    void testHandleIncomingMessage_EmptyMessage_Null() {
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> chatService.handleIncomingMessage(1L, member.getId(), null)
        );
        assertEquals(EMPTY_MESSAGE, exception.getMessage());
    }
}
