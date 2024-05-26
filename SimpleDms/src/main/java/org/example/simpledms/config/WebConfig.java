package org.example.simpledms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @fileName : WebConfig
 * @author : GGG
 * @since : 2024-04-02
 * description : CORS 보안 설정
 *      TODO: 
 *        사이트에서 ip 주소 또는 포트번호가 다르더라도 허용하게 해주는 설정파일
 *        CORS 보안 : 1개의 사이트에서 ip주소 또는 포트번호가 다르면 강제 차단
 *         (웹 브라우저에 기본 적용되어 있음)
 *         예) 차단 에러 메세지 : cors ...~ error ~
 *         - WebMvcConfigurer : cors 보안 설정 함수 있음(상속)
 *         - addCorsMappings() 함수 재정의 : cors 보안 설정 함수
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")              // 모든 경로 설정
                .allowedOrigins("http://localhost:8080")  // vue 의 주소
                .allowedMethods(                          // 허용
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                );
    }
}
