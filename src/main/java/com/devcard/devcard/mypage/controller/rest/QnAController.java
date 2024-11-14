package com.devcard.devcard.mypage.controller.rest;

import com.devcard.devcard.mypage.dto.NoticeRequest;
import com.devcard.devcard.mypage.dto.NoticeResponse;
import com.devcard.devcard.mypage.dto.NoticeUpdateRequest;
import com.devcard.devcard.mypage.dto.QnAListResponse;
import com.devcard.devcard.mypage.dto.QnARequest;
import com.devcard.devcard.mypage.dto.QnAResponse;
import com.devcard.devcard.mypage.dto.QnAUpdateRequest;
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
    public ResponseEntity<List<QnAListResponse>> getAllQnA(){
        return ResponseEntity.status(201).body(qnAService.getQnAList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QnAResponse> getQnA(@PathVariable("id") Long id){
        return ResponseEntity.ok(qnAService.getQnA(id));
    }

    @PostMapping("")
    public ResponseEntity<QnAResponse> addQnA(@RequestBody QnARequest qnARequest){
        return ResponseEntity.ok(qnAService.addQnA(qnARequest));
    }

    @PutMapping("")
    public ResponseEntity<QnAResponse> updateQnA(@RequestBody QnAUpdateRequest qnAUpdateRequest){
        return ResponseEntity.ok(qnAService.updateQnA(qnAUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QnAResponse> deleteQnA(@PathVariable("id") Long id){
        return ResponseEntity.ok(qnAService.deleteQnA(id));
    }
}
