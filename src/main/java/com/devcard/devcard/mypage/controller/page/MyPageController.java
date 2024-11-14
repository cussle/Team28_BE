package com.devcard.devcard.mypage.controller.page;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.repository.MemberRepository;
import com.devcard.devcard.mypage.service.NoticeService;
import com.devcard.devcard.mypage.service.QnAService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MyPageController {
    private final NoticeService noticeService;
    private final QnAService qnAService;
    private final MemberRepository memberRepository;

    public MyPageController(NoticeService noticeService, QnAService qnAService,
        MemberRepository memberRepository) {
        this.noticeService = noticeService;
        this.qnAService = qnAService;
        this.memberRepository = memberRepository;
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

    @GetMapping("/mypage/qna")
    public String getQnAList(Model model) {
        model.addAttribute("qnaList", qnAService.getQnAList());
        return "qna-list";
    }

    @GetMapping("/mypage/qna/{id}")
    public String getQnA(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("qna", qnAService.getQnA(id));
        return "qna-detail";
    }

    @GetMapping("/mypage/qna/create")
    public String getQnACreate(Model model, Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String githubId = String.valueOf(oAuth2User.getAttributes().get("id"));

        // GitHub ID로 사용자를 찾아서 등록 여부 확인
        Member member = memberRepository.findByGithubId(githubId);

        model.addAttribute("member", member);
        return "qna-create-new";
    }

    @GetMapping("/mypage/qna/create/{id}")
    public String getQnAUpdate(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("qna", qnAService.getQnA(id));
        return "qna-create-update";
    }
}
