package com.devcard.devcard.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomListResponse {

    private String id;
    private List<Long> participantsId;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public ChatRoomListResponse(
        long id,
        List<Long> participantsId,
        String lastMessage,
        LocalDateTime lastMessageTime
    ) {
        this.id = "room_" + id;
        this.participantsId = participantsId;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public String getId() {
        return id;
    }

    public void setId(long id) {
        this.id = "msg_" + id;
    }

    public List<Long> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(List<Long> participantsId) {
        this.participantsId = participantsId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
