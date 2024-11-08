package com.devcard.devcard.card.service;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.dto.GroupResponseDto;
import com.devcard.devcard.card.entity.Group;
import com.devcard.devcard.card.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<GroupResponseDto> getGroupsByMember(Member member) {
        List<Group> groups = groupRepository.findByMember(member);

        return groups.stream()
                .map(group -> new GroupResponseDto(group.getId(), group.getName(), group.getCount()))
                .collect(Collectors.toList());
    }
}
