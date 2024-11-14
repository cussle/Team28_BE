package com.devcard.devcard.card.service;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.dto.GroupResponseDto;
import com.devcard.devcard.card.entity.Card;
import com.devcard.devcard.card.entity.Group;
import com.devcard.devcard.card.repository.CardRepository;
import com.devcard.devcard.card.repository.GroupRepository;
import com.devcard.devcard.chat.dto.CreateRoomRequest;
import com.devcard.devcard.chat.service.ChatRoomService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final CardRepository cardRepository;
    private final ChatRoomService chatRoomService;

    public GroupService(
        GroupRepository groupRepository,
        CardRepository cardRepository,
        ChatRoomService chatRoomService
    ) {
        this.groupRepository = groupRepository;
        this.cardRepository = cardRepository;
        this.chatRoomService = chatRoomService;
    }

    @Transactional
    public GroupResponseDto createGroup(String name, Member member) {
        Group group = new Group(name, member);
        group = groupRepository.save(group);

        return new GroupResponseDto(group.getId(), group.getName(), group.getCount());
    }

    public List<GroupResponseDto> getGroupsByMember(Member member) {
        List<Group> groups = groupRepository.findByMember(member);

        return groups.stream()
            .map(group -> new GroupResponseDto(group.getId(), group.getName(), group.getCount()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void addCardToGroup(Long groupId, Long cardId, Member member) {
        Group group = groupRepository.findByIdAndMember(groupId, member)
            .orElseThrow(() -> new IllegalArgumentException("해당 그룹이 존재하지 않거나 접근 권한이 없습니다."));

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 명함이 존재하지 않습니다."));

        if (group.getCards().contains(card)) {
            throw new IllegalArgumentException("이미 해당 그룹에 추가되어 있는 명함입니다.");
        }

        // 채팅방 생성
        CreateRoomRequest createRoomRequest = new CreateRoomRequest(Arrays.asList(
            member.getId(),
            card.getMember().getId()
        ));
        chatRoomService.createChatRoom(createRoomRequest);

        group.addCard(card);
        groupRepository.save(group);
    }

    @Transactional
    public void deleteCardFromGroup(Long groupId, Long cardId, Member member) {
        Group group = groupRepository.findByIdAndMember(groupId, member)
            .orElseThrow(() -> new IllegalArgumentException("해당 그룹이 존재하지 않거나 접근 권한이 없습니다."));

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 명함이 존재하지 않습니다."));

        // 채팅방 제거
        chatRoomService.deleteChatRoomByParticipants(Arrays.asList(
            member.getId(),
            card.getMember().getId()
        ));

        group.removeCard(card); // 그룹에서 명함을 제거
        groupRepository.save(group); // 변경사항 저장
    }

    @Transactional
    public void updateGroupName(Long groupId, String newName, Member member) {
        Group group = groupRepository.findByIdAndMember(groupId, member)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "해당 그룹이 존재하지 않거나 접근 권한이 없습니다."
            ));

        group.setName(newName);
        groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId, Member member) {
        Group group = groupRepository.findByIdAndMember(groupId, member)
            .orElseThrow(() -> new IllegalArgumentException("해당 그룹이 존재하지 않거나 접근 권한이 없습니다."));

        groupRepository.delete(group); // 그룹 삭제
    }

}
