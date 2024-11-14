package com.devcard.devcard.mypage.controller.rest;

import com.devcard.devcard.mypage.dto.NoticeRequest;
import com.devcard.devcard.mypage.dto.NoticeResponse;
import com.devcard.devcard.mypage.dto.NoticeUpdateRequest;
import com.devcard.devcard.mypage.service.QnAService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qna")
public class QnAController {

    private final QnAService qnAService;

    public QnAController(QnAService qnAService) {
        this.qnAService = qnAService;
    }

    @GetMapping
    public ResponseEntity<List<NoticeResponse>> getAllQnA(){
        return ResponseEntity.status(201).body(qnAService.getNoticeList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> getQnA(@PathVariable("id") Long id){
        return ResponseEntity.ok(qnAService.getNotice(id));
    }

    @PostMapping("")
    public ResponseEntity<NoticeResponse> addQnA(@RequestBody NoticeRequest noticeRequest){
        return ResponseEntity.ok(qnAService.addNotice(noticeRequest));
    }

    @PutMapping("")
    public ResponseEntity<NoticeResponse> updateQnA(@RequestBody NoticeUpdateRequest noticeUpdateRequest){
        return ResponseEntity.ok(qnAService.updateNotice(noticeUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoticeResponse> deleteQnA(@PathVariable("id") Long id){
        return ResponseEntity.ok(qnAService.deleteNotice(id));
    }
}
