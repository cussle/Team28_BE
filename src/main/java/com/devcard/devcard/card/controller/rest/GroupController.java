package com.devcard.devcard.card.controller.rest;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.model.OauthMemberDetails;
import com.devcard.devcard.card.dto.GroupResponseDto;
import com.devcard.devcard.card.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        GroupResponseDto group = groupService.createGroup("새로운 그룹", oauthMemberDetails.getMember());
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/cards/{cardId}")
    public ResponseEntity<Void> addCardToGroup(@PathVariable Long groupId, @PathVariable Long cardId, @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        groupService.addCardToGroup(groupId, cardId, oauthMemberDetails.getMember());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{groupId}/update")
    public ResponseEntity<String> updateGroupName(@PathVariable Long groupId,
                                                  @RequestBody Map<String, String> request,
                                                  @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        String newName = request.get("name");
        Member member = oauthMemberDetails.getMember();  // OauthMemberDetails에서 Member 객체 가져오기
        groupService.updateGroupName(groupId, newName, member);
        return ResponseEntity.ok("그룹 이름이 수정되었습니다.");
    }

    @DeleteMapping("/{groupId}/cards/{cardId}/delete")
    public ResponseEntity<Void> removeCardFromGroup(@PathVariable Long groupId, @PathVariable Long cardId, @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        groupService.deleteCardFromGroup(groupId, cardId, oauthMemberDetails.getMember());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/delete")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId, @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        groupService.deleteGroup(groupId, oauthMemberDetails.getMember());
        return ResponseEntity.ok().build();
    }



}
