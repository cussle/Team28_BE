package com.devcard.devcard.chat.dto;

import java.util.List;

public record ChatRoomResponse(
    String id,
    List<String> participants,
    List<ChatMessageResponse> messages
) {

}
