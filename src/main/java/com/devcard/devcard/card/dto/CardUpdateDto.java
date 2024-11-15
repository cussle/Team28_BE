package com.devcard.devcard.card.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CardUpdateDto {

    @NotBlank(message = "회사는 필수 입력 항목입니다.")
    private final String company;

    private final String position;

    @Pattern(regexp = "^[+]?[0-9\\-\\s]+$", message = "유효한 전화번호 형식을 입력하세요.")
    private final String phone;
    private final String bio;
    private final String email;
    private final String cardName;
    private final String profileImg;

    public CardUpdateDto(String company, String position, String phone, String bio, String email, String cardName, String profileImg) {
        this.company = company;
        this.position = position;
        this.phone = phone;
        this.bio = bio;
        this.email = email;
        this.cardName = cardName;
        this.profileImg = profileImg;
    }

    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getPhone() { return phone; }
    public String getBio() { return bio; }
    public String getEmail() { return email; }
    public String getCardName() { return cardName; }
    public String getProfileImg() { return profileImg; }

}
