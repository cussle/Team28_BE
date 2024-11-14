package com.devcard.devcard.card.controller.page;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.model.OauthMemberDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OauthMemberDetails oauthMemberDetails, Model model) {
        if (oauthMemberDetails != null) {
            Member member = oauthMemberDetails.getMember();
            model.addAttribute("member", member);
        }
        return "home";
    }
}
