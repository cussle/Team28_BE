package com.devcard.devcard.mypage.controller.rest;

import com.devcard.devcard.mypage.dto.NoticeRequest;
import com.devcard.devcard.mypage.dto.NoticeResponse;
import com.devcard.devcard.mypage.dto.NoticeUpdateRequest;
import com.devcard.devcard.mypage.service.NoticeService;
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

@RestController()
@RequestMapping()
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<List<NoticeResponse>> getAllNotice(){
        return ResponseEntity.status(201).body(noticeService.getNoticeList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable("id") Long id){
        return ResponseEntity.ok(noticeService.getNotice(id));
    }

    @PostMapping("")
    public ResponseEntity<NoticeResponse> addNotice(@RequestBody NoticeRequest noticeRequest){
        return ResponseEntity.ok(noticeService.addNotice(noticeRequest));
    }

    @PutMapping("")
    public ResponseEntity<NoticeResponse> updateNotice(@RequestBody NoticeUpdateRequest noticeUpdateRequest){
        return ResponseEntity.ok(noticeService.updateNotice(noticeUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoticeResponse> deleteNotice(@PathVariable("id") Long id){
        return ResponseEntity.ok(noticeService.deleteNotice(id));
    }
}
