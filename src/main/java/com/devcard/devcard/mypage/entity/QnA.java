package com.devcard.devcard.mypage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "qna")
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String questionTitle;

    private String questionContent;

    private String answer;

    private LocalDateTime questionTimestamp;

    private LocalDateTime answerTimestamp;

    private boolean answerCompleted;

    public QnA(){

    }

    public QnA(String name, String questionTitle, String questionContent, LocalDateTime questionTimestamp) {
        this.name = name;
        this.questionTitle = questionTitle;
        this.questionContent = questionContent;
        this.questionTimestamp = questionTimestamp;
        this.answerCompleted = false;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public String getAnswer() {
        return answer;
    }

    public LocalDateTime getQuestionTimestamp() {
        return questionTimestamp;
    }

    public LocalDateTime getAnswerTimestamp() {
        return answerTimestamp;
    }

    public boolean isAnswerCompleted() {
        return answerCompleted;
    }

    public void updateByRequest(String updateQuestionTitle, String updateQuestionContent){
        this.questionTitle = updateQuestionTitle;
        this.questionContent = updateQuestionContent;
    }

    public void updateAnswer(String updateAnswer){
        this.answer = updateAnswer;
        this.answerTimestamp = LocalDateTime.now();
        this.answerCompleted = true;
    }
}
