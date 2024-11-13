package com.devcard.devcard.mypage.controller.page;

import com.devcard.devcard.mypage.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MyPageController {
    private final NoticeService noticeService;

    public MyPageController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/mypage/notice")
    public String getNoticeList(Model model) {
        model.addAttribute("noticeList", noticeService.getNoticeList());
        return "notice-list";
    }

    @GetMapping("/mypage/notice/{id}")
    public String getNotice(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("notice", noticeService.getNotice(id));
        return "notice-detail";
    }
}
