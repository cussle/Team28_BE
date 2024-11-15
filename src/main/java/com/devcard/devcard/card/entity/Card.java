package com.devcard.devcard.card.entity;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.dto.CardUpdateDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToMany(mappedBy = "cards")
    private List<Group> groups = new ArrayList<>();

    private String email;
    private String nickname;
    private String profileImg;
    private String company;
    private String position;
    private String phone;
    private String bio;
    private String linkedin;
    private String notion;
    private String certification;
    private String extra;

    private boolean techStack;
    private boolean repository;
    private boolean contributions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 기본 생성자 (JPA 요구 사항)
    protected Card() {
    }

    // 빌더 패턴을 위한 생성자
    private Card(Builder builder) {
        this.member = builder.member;
        this.email = builder.email;
        this.nickname = builder.nickname;
        this.profileImg = builder.profileImg;
        this.company = builder.company;
        this.position = builder.position;
        this.phone = builder.phone;
        this.bio = builder.bio;
        this.linkedin = builder.linkedin;
        this.notion = builder.notion;
        this.certification = builder.certification;
        this.extra = builder.extra;
        this.techStack = builder.techStack;
        this.repository = builder.repository;
        this.contributions = builder.contributions;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 빌더 패턴을 위한 생성자
    public static class Builder {
        private final Member member;
        private String email;
        private String nickname;
        private String profileImg;
        private String company;
        private String position;
        private String phone;
        private String bio;
        private String linkedin;
        private String notion;
        private String certification;
        private String extra;
        private boolean techStack;
        private boolean repository;
        private boolean contributions;

        public Builder(Member member) {
            this.member = member;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder profileImg(String profileImg) {
            this.profileImg = profileImg;
            return this;
        }

        public Builder company(String company) {
            this.company = company;
            return this;
        }

        public Builder position(String position) {
            this.position = position;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder linkedin(String linkedin) {
            this.linkedin = linkedin;
            return this;
        }

        public Builder notion(String notion) {
            this.notion = notion;
            return this;
        }

        public Builder certification(String certification) {
            this.certification = certification;
            return this;
        }

        public Builder extra(String extra) {
            this.extra = extra;
            return this;
        }

        public Builder techStack(boolean techStack) {
            this.techStack = techStack;
            return this;
        }

        public Builder repository(boolean repository) {
            this.repository = repository;
            return this;
        }

        public Builder contributions(boolean contributions) {
            this.contributions = contributions;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }

    // Getter
    public Long getId() { return id; }
    public Member getMember() { return member; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public String getProfileImg() { return profileImg; }
    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getPhone() { return phone; }
    public String getBio() { return bio; }
    public String getLinkedin() { return linkedin; }
    public String getNotion() { return notion; }
    public String getCertification() { return certification; }
    public String getExtra() { return extra; }
    public boolean isTechStack() { return techStack; }
    public boolean isRepository() { return repository; }
    public boolean isContributions() { return contributions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<Group> getGroups() { return groups; }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    // DTO 기반 업데이트 메서드
    public void updateFromDto(CardUpdateDto dto) {
        this.company = dto.getCompany();
        this.position = dto.getPosition();
        this.phone = dto.getPhone();
        this.bio = dto.getBio();
        this.email = dto.getEmail();
        this.nickname = dto.getCardName();
        this.profileImg = dto.getProfileImg();
        this.linkedin = dto.getLinkedin();
        this.notion = dto.getNotion();
        this.certification = dto.getCertification();
        this.extra = dto.getExtra();
        this.techStack = dto.isTechStack();
        this.repository = dto.isRepository();
        this.contributions = dto.isContributions();
        this.updatedAt = LocalDateTime.now();
    }
}
