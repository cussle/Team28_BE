package com.devcard.devcard.card.service;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.dto.CardRequestDto;
import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.exception.CardNotFoundException;
import com.devcard.devcard.card.repository.CardRepository;
import com.devcard.devcard.card.entity.Card;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public CardResponseDto createCard(CardRequestDto cardRequestDto, Member member) {
        Card card = new Card.Builder(member)
                .company(cardRequestDto.getCompany())
                .position(cardRequestDto.getPosition())
                .phone(cardRequestDto.getPhone())
                .bio(cardRequestDto.getBio())
                .build();

        Card savedCard = cardRepository.save(card);
        return CardResponseDto.fromEntity(savedCard);
    }

    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));
        return CardResponseDto.fromEntity(card);
    }

    @Transactional(readOnly = true)
    public List<CardResponseDto> getCards() {
        List<Card> cards = cardRepository.findAll();
        return cards.stream()
                .map(CardResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public CardResponseDto updateCard(Long id, CardRequestDto cardRequestDto, Member member) {
        Card existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));

        // 명함 소유자 확인
        if (!existingCard.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 기존 엔티티의 필드 업데이트
        existingCard.updateFromDto(cardRequestDto);

        return CardResponseDto.fromEntity(existingCard);
    }

    @Transactional
    public void deleteCard(Long id, Member member) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));

        // 명함 소유자 확인
        if (!card.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("권한이 없습니다.");
        }

        cardRepository.delete(card);
    }


}
