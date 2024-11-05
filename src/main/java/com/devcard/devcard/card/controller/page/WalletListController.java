package com.devcard.devcard.card.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WalletListController {

    @GetMapping("wallet-List")
    public String walletList() {
        return "wallet-list";
    }
}
