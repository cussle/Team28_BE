package com.devcard.devcard.mypage.dto;

import java.time.LocalDateTime;

public record QnAResponse(Long id,
                          String name,
                          String questionTitle,
                          String questionContent,
                          String answer,
                          LocalDateTime questionTimestamp,
                          LocalDateTime answerTimestamp,
                          boolean answerCompleted) {
}
