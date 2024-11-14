package com.devcard.devcard.mypage.service;

import com.devcard.devcard.mypage.dto.QnAAnswerDTO;
import com.devcard.devcard.mypage.dto.QnAListResponse;
import com.devcard.devcard.mypage.dto.QnARequest;
import com.devcard.devcard.mypage.dto.QnAResponse;
import com.devcard.devcard.mypage.dto.QnAUpdateRequest;
import com.devcard.devcard.mypage.entity.QnA;
import com.devcard.devcard.mypage.repository.QnARepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class QnAService {
    private final QnARepository qnARepository;

    public QnAService(QnARepository qnARepository) {
        this.qnARepository = qnARepository;
    }
    public List<QnAListResponse> getQnAList() {
        List<QnA> qnaList = qnARepository.findAllByOrderByQuestionTimestampDesc();
        return qnaList.stream().map(qna -> new QnAListResponse(
            qna.getId(),
            qna.getName(),
            qna.getQuestionTitle(),
            qna.isAnswerCompleted(),
            qna.getQuestionTimestamp()
        )).toList();
    }

    public QnAResponse getQnA(Long id) {
        QnA qna = qnARepository.findQnAById(id);
        return new QnAResponse(qna.getId(), qna.getName(), qna.getQuestionTitle(), qna.getQuestionContent(), qna.getAnswer(), qna.getQuestionTimestamp(), qna.getAnswerTimestamp(), qna.isAnswerCompleted());
    }

    public QnAResponse addQnA(QnARequest qnARequest) {
        QnA qna = qnARepository.save(new QnA(qnARequest.name(), qnARequest.questionTitle(), qnARequest.questionContent(),
            LocalDateTime.now()));
        return new QnAResponse(qna.getId(), qna.getName(), qna.getQuestionTitle(), qna.getQuestionContent(), qna.getAnswer(), qna.getQuestionTimestamp(), qna.getAnswerTimestamp(), qna.isAnswerCompleted());
    }

    public QnAResponse updateQnA(QnAUpdateRequest qnAUpdateRequest) {
        QnA qna = qnARepository.findQnAById(qnAUpdateRequest.id());
        qna.updateByRequest(qnAUpdateRequest.questionTitle(), qnAUpdateRequest.questionContent());
        qnARepository.save(qna);
        return new QnAResponse(qna.getId(), qna.getName(), qna.getQuestionTitle(), qna.getQuestionContent(), qna.getAnswer(), qna.getQuestionTimestamp(), qna.getAnswerTimestamp(), qna.isAnswerCompleted());
    }

    public QnAResponse deleteQnA(Long id) {
        QnA qna = qnARepository.findQnAById(id);
        qnARepository.delete(qna);
        return new QnAResponse(qna.getId(), qna.getName(), qna.getQuestionTitle(), qna.getQuestionContent(), qna.getAnswer(), qna.getQuestionTimestamp(), qna.getAnswerTimestamp(), qna.isAnswerCompleted());
    }

    public QnAResponse updateAnswer(QnAAnswerDTO qnAAnswerDTO) {
        QnA qna = qnARepository.findQnAById(qnAAnswerDTO.id());
        qna.updateAnswer(qnAAnswerDTO.answer());
        return new QnAResponse(qna.getId(), qna.getName(), qna.getQuestionTitle(), qna.getQuestionContent(), qna.getAnswer(), qna.getQuestionTimestamp(), qna.getAnswerTimestamp(), qna.isAnswerCompleted());
    }

}
