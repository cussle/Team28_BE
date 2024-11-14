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

}
