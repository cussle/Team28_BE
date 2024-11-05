package com.devcard.devcard.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomListResponse {

    private String id;
    private List<String> participants;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public ChatRoomListResponse(
        long id, List<String> participants, String lastMessage,
        LocalDateTime lastMessageTime
    ) {
        this.id = "room_" + id;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public String getId() {
        return id;
    }

    public void setId(long id) {
        this.id = "msg_" + id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
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
