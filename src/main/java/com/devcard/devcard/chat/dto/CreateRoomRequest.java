package com.devcard.devcard.chat.dto;

import java.util.List;

public class CreateRoomRequest {

    private List<Long> participantsId;

    public <T> CreateRoomRequest(List<T> list) {
    }

    public List<Long> getParticipantsId() {
        return participantsId;
    }
}
