package com.devcard.devcard.chat.dto;

import java.util.List;

public class CreateRoomRequest {

    private List<Long> participantsId;

    // 기본 생성자
    public CreateRoomRequest() {
    }

    // 모든 필드 생성자
    public CreateRoomRequest(List<Long> participantsId) {
        this.participantsId = participantsId;
    }

    public List<Long> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(List<Long> participantsId) {
        this.participantsId = participantsId;
    }
}
