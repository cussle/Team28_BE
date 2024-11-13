package com.devcard.devcard.mypage.dto;

import java.time.LocalDateTime;

public record NoticeRequest(String title, String content, LocalDateTime timestamp) {

}
