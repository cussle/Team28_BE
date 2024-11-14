package com.devcard.devcard.mypage.dto;

import java.time.LocalDateTime;

public record QnAListResponse(Long id, String name, String questionTitle, boolean answerCompleted, LocalDateTime questionTimestamp) {

}
