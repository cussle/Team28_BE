package com.devcard.devcard.mypage.dto;

import java.time.LocalDateTime;

public record NoticeUpdateRequest (Long id, String title, String content, LocalDateTime timestamp) {

}
