package org.example.simpledms.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.example.simpledms.security.oauth.SocialLoginSuccess;
import org.example.simpledms.security.oauth.SocialLoginServiceCustom;
import org.example.simpledms.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @fileName : WebSecurityConfig
 * @author : GGG
 * @since : 2024-04-15
 * description :
 *  1) DB 인증을 위한 함수    : passwordEncoder()
 *  2) 패스워드 암호화 함수     : 필수 정의
 *    @Bean : IOC (스프링이 객체를 생성해주는 것), 함수의 리턴객체를 생성함
 *      => (참고) 용어 : 스프링 생성한 객체 == 빈(Bean==콩)
 *  3) JWT 웹토큰 자동인증 함수 : authenticationJwtTokenFilter()
 *  4) img, css, js 등 인증 무시 설정 함수 : webSecurityCustomizer()
 *      => 사용법 : (web) -> web.ignoring().requestMatchers("경로", "경로2"...)
 *  5) 스프링 시큐리티 규칙 정의 함수(***) : filterChain(HttpSecurity http)
 *    5-1) cors 사용
 *    5-2) csrf 해킹 보안 비활성화(쿠키/세션 사용않함)
 *    5-3) 쿠키/세션 안함(비활성화) -> 로컬스토리지/웹토큰
 *    5-4) form 태그 action 을 이용한 로그인 사용않함 -> axios 통신함
 *    5-5) /api/auth/**  : 이 url 은 모든 사용자 접근 허용, ** (하위 url 모두 포함)
 *    5-8) / : 이 url 은 모든 사용자 접근 허용
 *    5-9) TODO : 웹토큰 클래스를 스프링시큐리티 설정에 끼워넣기 : 모든 게시판 조회(CRUD)에서 아래 인증을 실행함
 *
 *  6) 소셜 로그인
 *    6-1) 소셜 로그인 성공후 처리할 리다이렉션해서 구글 인가코드 받음
 *    6-2) 구글 인가코드 확인 후에 DB 인증, 웹토큰 발행, 프론트로 전송
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final SocialLoginSuccess socialLoginSuccess;

    private final SocialLoginServiceCustom socialLoginServiceCustom ;

    private final AuthTokenFilter authTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//  img, css, js 등 인증 무시 설정 함수
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/img/**",
                "/css/**",
                "/js/**"
        );
    }

    //    TODO: 스프링 시큐리티 규칙 정의 함수(***)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults());                         // 5-1)
        http.csrf((csrf) -> csrf.disable());                          // 5-2)
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 5-3)
        http.formLogin(req -> req.disable());                         // 5-4)

        http.authorizeHttpRequests(req -> req
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers("/api/auth/**").permitAll()        // 5-5)
                .requestMatchers("/").permitAll()                   // 5-8)
                .anyRequest()
                .authenticated());

//        TODO: 소셜 로그인 설정 부분
        http.oauth2Login(req  -> req
                .successHandler(socialLoginSuccess)                                     // 6-1)
                .userInfoEndpoint(arg  -> arg.userService(socialLoginServiceCustom))    // 6-2)
        );

//        5-9)
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
