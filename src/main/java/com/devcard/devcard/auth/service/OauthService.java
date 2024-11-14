package com.devcard.devcard.auth.service;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.model.OauthMemberDetails;
import com.devcard.devcard.auth.repository.MemberRepository;
import com.devcard.devcard.card.service.CardService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class OauthService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final CardService cardService;

    public OauthService(MemberRepository memberRepository, CardService cardService) {
        this.memberRepository = memberRepository;
        this.cardService = cardService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Integer githubIdInt = (Integer) attributes.get("id");
        String githubId = String.valueOf(githubIdInt);

        String email = fetchEmailFromGitHub(userRequest);

        // 데이터베이스에 이미 등록된 사용자인지 확인
        Member member = memberRepository.findByGithubId(githubId);
        if (member == null) {
            // 사용자가 없으면 새로 저장
            member = new Member(
                    githubId,
                    email,
                    (String) attributes.get("avatar_url"),
                    (String) attributes.get("name"),
                    (String) attributes.get("login"),
                    "ROLE_USER",  // 기본 역할 설정
                    new Timestamp(System.currentTimeMillis())
            );
            memberRepository.save(member);

            // 회원가입시 멤버 정보로 명함 자동 생성
            cardService.createCardWithDefaultInfo(member);
        } else {
            // 기존 회원 정보 업데이트
            member.updateFromAttributes(attributes);
            memberRepository.save(member);
        }

        // OauthMemberDetails 반환
        return new OauthMemberDetails(member, attributes);
    }

    // 이메일 정보를 가져오는 메서드 추가
    private String fetchEmailFromGitHub(OAuth2UserRequest userRequest) {
        String uri = "https://api.github.com/user/emails";
        String accessToken = userRequest.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> emails = response.getBody();

        if (emails != null) {
            for (Map<String, Object> emailInfo : emails) {
                Boolean primary = (Boolean) emailInfo.get("primary");
                Boolean verified = (Boolean) emailInfo.get("verified");
                if (primary != null && primary && verified != null && verified) {
                    return (String) emailInfo.get("email");
                }
            }
        }

        return null;
    }
}