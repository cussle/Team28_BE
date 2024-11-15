package com.devcard.devcard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 설정 적용
            .allowedOriginPatterns("*")  // 모든 도메인에서 오는 요청 허용
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")  // 허용할 HTTP 메서드 목록
            .allowedHeaders("*")  // 모든 요청 헤더 허용
            .exposedHeaders(HttpHeaders.LOCATION)  // 응답 헤더 중 'Location' 헤더를 클라이언트에서 사용할 수 있도록 허용
            .allowCredentials(true)  // 자격 증명(쿠키, 인증 정보 등)을 포함한 요청 허용
            .maxAge(3600);  // pre-flight 요청의 캐시 시간을 3600초(1시간)로 설정
    }
}
