package com.devcard.devcard.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CreateRoomResponse(
    String id,
    List<String> participants,
    LocalDateTime createdAt
) {

}
