package com.devcard.devcard.card.controller.page;

import com.devcard.devcard.auth.model.OauthMemberDetails;
import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.dto.GroupResponseDto;
import com.devcard.devcard.card.service.CardService;
import com.devcard.devcard.card.service.GroupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CardPageController {

    private final CardService cardService;
    private final GroupService groupService;

    @Value("${kakao.javascript.key}")
    private String kakaoJavascriptKey;

    public CardPageController(CardService cardService, GroupService groupService) {
        this.cardService = cardService;
        this.groupService = groupService;
    }

    @GetMapping("/cards/{id}/view")
    public String viewCard(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        CardResponseDto card = cardService.getCard(id);
        boolean isMyCard = card.getGithubId().equals(oauthMemberDetails.getMember().getGithubId());
        model.addAttribute("card", card);
        model.addAttribute("isMyCard", isMyCard);
        model.addAttribute("kakaoJavascriptKey", kakaoJavascriptKey);

        if (!isMyCard) {
            List<GroupResponseDto> groups = groupService.getGroupsByMember(oauthMemberDetails.getMember());
            model.addAttribute("groups", groups);
        }

        return "card-detail";
    }

    @GetMapping("/cards/{id}/update")
    public String updateCard(@PathVariable("id") Long id, Model model) {
        CardResponseDto card = cardService.getCard(id);
        model.addAttribute("card", card);
        return "card-update";
    }

    @GetMapping("/groups/{id}/cards")
    public String viewCardList(@PathVariable("id") Long groupId, Model model, @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        List<CardResponseDto> cards = cardService.getCardsByGroup(groupId, oauthMemberDetails.getMember());
        model.addAttribute("cards", cards);
        return "card-list";
    }

    @GetMapping("/cards/manage")
    public String cardManage() {
        return "card-manage";
    }

    @GetMapping("/cards/create-view")
    public String createCardView() {
        return "card-create";
    }

    @GetMapping("/cards/{cardId}/edit")
    public String editCardView(@PathVariable("cardId") Long cardId, Model model) {
        model.addAttribute("cardId", cardId);
        return "card-edit";
    }

    @GetMapping("/shared/cards/{cardId}")
    public String sharedCardView(@PathVariable("cardId") Long cardId, Model model) {
        CardResponseDto card = cardService.getCard(cardId);
        model.addAttribute("card", card);
        return "card-shared-view";
    }

}
