package com.devcard.devcard.card.controller.page;

import com.devcard.devcard.card.entity.Group;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WalletListController {

    @GetMapping("wallet-list")
    public String walletList(Model model) {

        List<Group> groups = List.of(
                new Group("카테캠", 3),
                new Group("대학교", 2),
                new Group("친구", 4),
                new Group("동아리", 10),
                new Group("2024 하계 인턴", 5)
        );

        model.addAttribute("groups", groups);
        return "wallet-list";
    }
}
