package com.devcard.devcard.card.dto;

public class CardUpdateDto {

    private final String company;
    private final String position;
    private final String phone;
    private final String bio;
    private final String email;
    private final String cardName;
    private final String profileImg;
    private final String linkedin;
    private final String notion;
    private final String certification;
    private final String extra;
    private final boolean techStack;
    private final boolean repository;
    private final boolean contributions;

    public CardUpdateDto(String company, String position, String phone, String bio, String email, String cardName, String profileImg,
                         String linkedin, String notion, String certification, String extra,
                         boolean techStack, boolean repository, boolean contributions) {
        this.company = company;
        this.position = position;
        this.phone = phone;
        this.bio = bio;
        this.email = email;
        this.cardName = cardName;
        this.profileImg = profileImg;
        this.linkedin = linkedin;
        this.notion = notion;
        this.certification = certification;
        this.extra = extra;
        this.techStack = techStack;
        this.repository = repository;
        this.contributions = contributions;
    }

    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getPhone() { return phone; }
    public String getBio() { return bio; }
    public String getEmail() { return email; }
    public String getCardName() { return cardName; }
    public String getProfileImg() { return profileImg; }
    public String getLinkedin() { return linkedin; }
    public String getNotion() { return notion; }
    public String getCertification() { return certification; }
    public String getExtra() { return extra; }
    public boolean isTechStack() { return techStack; }
    public boolean isRepository() { return repository; }
    public boolean isContributions() { return contributions; }
}
