package com.devcard.devcard.card.service;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.dto.CardRequestDto;
import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.entity.Card;
import com.devcard.devcard.card.exception.CardNotFoundException;
import com.devcard.devcard.card.repository.CardRepository;
import com.devcard.devcard.card.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private GroupRepository groupRepository;

    private Member member;
    private Card card;
    private CardRequestDto cardRequestDto;

    @BeforeEach
    void setUp() throws Exception {
        // Member 객체 초기화 및 ID 설정
        member = new Member(
                "githubId",
                "test@example.com",
                "profileImgUrl",
                "username",
                "nickname",
                "USER",
                Timestamp.from(Instant.now())
        );
        setId(member, 1L); // ID 설정

        // CardRequestDto 초기화 (추가된 필드 포함)
        cardRequestDto = new CardRequestDto(
                "Test Company",
                "Test Position",
                "123-456-7890",
                "Test Bio",
                "test@example.com",
                "Test Card Name",
                "profileImgUrl"
        );

        // Card 객체 초기화 및 ID 설정
        card = new Card.Builder(member)
                .company(cardRequestDto.getCompany())
                .position(cardRequestDto.getPosition())
                .phone(cardRequestDto.getPhone())
                .bio(cardRequestDto.getBio())
                .email(cardRequestDto.getEmail())
                .profileImg(cardRequestDto.getProfileImg())
                .build();
        setId(card, 1L); // ID 설정
    }

    // Reflection을 사용하여 ID 설정하는 메서드
    void setId(Object target, Long id) throws Exception {
        Field idField = target.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(target, id);
    }

    @Test
    void createCard_Success() throws Exception {
        // Mock 설정: 저장 시 ID를 설정하여 반환
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card savedCard = invocation.getArgument(0);
            setId(savedCard, 1L); // ID 설정
            return savedCard;
        });

        CardResponseDto response = cardService.createCard(cardRequestDto, member);

        assertThat(response.getCompany()).isEqualTo(card.getCompany());
        assertThat(response.getPosition()).isEqualTo(card.getPosition());
        assertThat(response.getEmail()).isEqualTo(card.getEmail());
        assertThat(response.getProfileImg()).isEqualTo(card.getProfileImg());
        assertThat(response.getId()).isEqualTo(1L);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void getCard_ValidId_Success() throws Exception {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        CardResponseDto response = cardService.getCard(1L);

        assertThat(response.getCompany()).isEqualTo(card.getCompany());
        assertThat(response.getEmail()).isEqualTo(card.getEmail());
        assertThat(response.getProfileImg()).isEqualTo(card.getProfileImg());
        assertThat(response.getId()).isEqualTo(1L);
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    void getCard_InvalidId_ThrowsException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getCard(1L));
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    void getMyCards_ReturnsCardList() throws Exception {
        when(cardRepository.findByMemberId(member.getId())).thenReturn(List.of(card));

        List<CardResponseDto> cards = cardService.getMyCards(member.getId());

        assertThat(cards).hasSize(1);
        assertThat(cards.get(0).getCompany()).isEqualTo(card.getCompany());
        verify(cardRepository, times(1)).findByMemberId(member.getId());
    }

    @Test
    void getCards_ReturnsAllCards() throws Exception {
        when(cardRepository.findAll()).thenReturn(List.of(card));

        List<CardResponseDto> cards = cardService.getCards();

        assertThat(cards).hasSize(1);
        assertThat(cards.get(0).getCompany()).isEqualTo(card.getCompany());
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void deleteCard_ValidId_Success() throws Exception {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        cardService.deleteCard(1L, member);

        verify(cardRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).delete(card);
    }

    @Test
    void deleteCard_InvalidId_ThrowsException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.deleteCard(1L, member));
        verify(cardRepository, times(1)).findById(1L);
        verify(cardRepository, times(0)).delete(any(Card.class));
    }
}
