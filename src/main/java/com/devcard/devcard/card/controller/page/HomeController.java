package com.devcard.devcard.card.controller.page;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.model.OauthMemberDetails;
import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.service.CardService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    private final CardService cardService;

    public HomeController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OauthMemberDetails oauthMemberDetails, Model model) {
        if (oauthMemberDetails != null) {
            Member member = oauthMemberDetails.getMember();
            model.addAttribute("member", member);

            // CardResponseDto 리스트 가져오기
            List<CardResponseDto> cards = cardService.getMyCards(member.getId());

            // 첫 번째 카드 ID 가져오기 (카드가 존재하는 경우)
            if (!cards.isEmpty()) {
                Long firstCardId = cards.getFirst().getId();
                model.addAttribute("cardId", firstCardId);
            } else {
                // 기본 카드 ID 또는 처리 방법 정의 (카드가 없을 경우)
                model.addAttribute("cardId", 1); // 기본값으로 1 설정
            }
        }
        return "home";
    }
}
