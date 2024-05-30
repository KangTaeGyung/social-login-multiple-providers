package org.example.simpledms.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simpledms.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * fileName : AuthTokenFilter
 * author : GGG
 * date : 2024-04-16
 * description : 게시판 조회시 자동 웹토큰 인증을 위한 클래스
 * 1) 게시판 조회시도 로그인 인증이 필요한데 자동으로 인증받기 위한 클래스임
 *    => 게시판 조회시 spring security 가 자동으로 AuthTokenFilter 클래스를 실행함
 * 2) OncePerRequestFilter 클래스 상속받아 개발자가 직접 구현함
 * 3) 자동 인증 알고리즘
 *   3-1) 프론트에서 보내준 웹토큰을 받아서 문자열로 변환
 *   3-2) 웹토큰(jwt) 이 있고, 유효하면 인증을 진행함
 *   3-3) DB 인증 : 유저ID 로 조회해서 있으면 우리 회원(인증됨)
 *      => 웹토큰 : 헤더.내용(유저ID).서명
 *      3-3-1) DB 인증(조회) : UserDetails(인증된객체) == MemberDto(인증된객체) == User(인증된객체)
 *      3-3-2) 스프링 시큐리티에 DB 인증되었다고 알림 => 인증 강제 설정 ( authenticated : false -> true )
 *   3-4) 인증된 객체는 필통(홀더) 에 관리 : SecurityContextHolder 클래스
 *   3-5) 스프링시큐리티 자동 인증기능에 연결하는 함수
 *       (참고) 필터  : 스프링시큐리티 인증 클래스들
 *       필터체인 : 스프링시큐리티 인증 클래스들을 연결해서 연쇄적으로 인증할때 사용
 *
 * 함수 설명 :
 * 가) StringUtils.hasText(문자열) : 문자열 유효성 체크 함수
 *    - null 이 아니어야 하고
 *    - 길이가 0보다 커야하고
 *    - 공백이 아닌 문자가 포함되면
 *    => true 아니면 false
 * 나) "문자열".substring(시작 인데스번호, 끝인덱스번호) : 시작 ~ 끝사이 문자열 자르기
 */
@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;                                  // DB 인증

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);                                             // 3-1)

            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {                         // 3-2)
                String email = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email); // 3-3-1)

//             3-3-2)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);   // 3-4)
            }

        } catch (Exception e) {
            log.debug("인증을 정의할 수 없습니다. : " + e);
        }

        filterChain.doFilter(request, response);                                         // 3-5)
    }

    //    프론트에서 전송한 헤더에서 웹토큰을 꺼내는 함수
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());    // 잘라낸 문자열 : 웹토큰임
        }

        return null;
    }

}
