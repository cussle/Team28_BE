package com.devcard.devcard.mypage.dto;

import java.time.LocalDateTime;

public record NoticeResponse(String title, String content, LocalDateTime timestamp) {

}
