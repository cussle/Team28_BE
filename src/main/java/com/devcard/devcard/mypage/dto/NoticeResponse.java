package com.devcard.devcard.mypage.dto;

import java.time.LocalDateTime;

public record NoticeResponse(Long id, String title, String content, LocalDateTime timestamp) {

}
