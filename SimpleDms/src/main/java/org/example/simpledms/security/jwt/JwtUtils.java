package org.example.simpledms.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.example.simpledms.security.dto.MemberDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @fileName : JwtUtils
 * @author : GGG
 * @since  : 2024-04-15
 * description :
 * - 검증된(인증된) 유저객체 = MemberDto
 * => MemberDto + 추가속성들 == authentication(id, 패스워드, ip, 인증여부등) 객체
 * => MemberDto == principal
 * => 패스워드    == credential
 * - 검증된(인증된) 유저객체 보관하는 클래스(필통, 홀더) : SecurityContextHolder
 * - 검증(인증)을 실제 담당하는 관리 클래스           : AuthenticationManagerBuilder
 * - 로그인ID / 패스워드 인증을 위한 클래스           : UsernamePasswordAuthenticationToken
 * <p>
 * 1) JWT(웹토큰) 구조
 * - Json Web Token 구조 : 헤더(header).내용(payload).서명(signature)
 * 헤더 : 토큰타입, 알고리즘
 * 내용 : 주제(subject(로그인ID)), 토큰발급시간(issuedAt, 생략), 만료기간(expiration), 토큰수령자(생략)
 * 서명 : Jwts.builder().signWith(암호화알고리즘, 비밀키값)
 * 생성 : Jwts.builder().compact()
 * <p>
 * 2) JWT(웹토큰) 생성 함수 : 알고리즘
 * 2-0) 헤더 생략
 * 2-1) 내용
 * 가) authentication 에서 인증유저(principal) 꺼냄
 * 나) 주제       : 이메일 넣기
 * 다) 토큰발급시간 : 현재시간
 * 라) 만료일자 적용 : 현재시간 + 10분
 * 마) 디지털서명   : 공개키 암호화 적용(HS512 사용 , 비밀키 넣기)
 * 바) 토큰 생성
 * 참고) 기타 함수 설명
 * setSigningKey(비밀키) 함수     : 비밀키를 넣어 웹토큰 디코딩하기(해석)
 * parseClaimsJws(웹토큰이름) 함수 : 웹토큰을 분리하여 유효성 점검하는 함수
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${simpleDms.app.jwtSecret}")
    private String jwtSecret;                                                      // 비밀키

    @Value("${simpleDms.app.jwtExpirationMs}")
    private int jwtExpirationMs;                                                   // 만료시간

    // JWT 생성 함수
    public String generateJwtToken(Authentication authentication) {
        MemberDto memberDto = (MemberDto) authentication.getPrincipal();           // 2-1)

        return Jwts.builder()
                .setSubject((memberDto.getEmail()))                                // 나
                .setIssuedAt(new Date())                                           // 다
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // 라
                .signWith(SignatureAlgorithm.HS512, jwtSecret)                     // 마
                .compact();                                                        // 바
    }

    // oAuth2 인증 : JWT 토큰 만들기
    public String generateJwtToken(String email) {
        return Jwts.builder()
                .setSubject(email.split("@")[0])
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 암호화 적용 서명
                .compact(); // 토큰 생성
    }

    //  웹토큰에서 이메일 가져오기 함수
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()                                                       // 웹토큰 파싱
                .setSigningKey(jwtSecret)                                          // 비밀키 넣기
                .parseClaimsJws(token)                                             // 웹토큰 검증
                .getBody()                                                         // 내용 접근
                .getSubject();                                                     // 주제(이메일) 가져오기
    }

    //  JWT(웹토큰) 이 유효한지 검증하는 함수
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);                                    // 웹토큰 검증
            return true;
        } catch (SignatureException e) {
            log.error("디지털서명이 훼손되었습니다.: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("웹토큰 유효하지 않습니다.: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("웹토큰 만료되었습니다. : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("웹토큰을 지원하지 않습니다.: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("웹토큰 내용이 비었습니다. : {}", e.getMessage());
        }

        return false;
    }
}
