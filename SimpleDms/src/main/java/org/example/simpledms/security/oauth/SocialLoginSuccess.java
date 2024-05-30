package org.example.simpledms.security.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simpledms.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * fileName : OAuth2SuccessHandler
 * author : kangtaegyung
 * date : 2022/12/16
 * description : 소셜 로그인 성공 후 처리할 클래스
 * 알고리즘
 * 1) 인증된 객체를 홀더에 저장
 * 2) 인증된 유저 정보를 oAuth2User(소셜로그인 클래스) 에 저장, 소셜로그인은 oAuth2User 사용
 * 3) 권한 정보 가져오기
 * 4) provider 정보 : google, naver, kakao
 * 5) 구글/네이버 등 provider 마다 전달해주는 속성명과 구조가 틀림
 *     구글 : { email: forbob@naver.com , name: 강태경 }
 *     네이버 : { response : { email: forbob@naver.com , id : abcdef } }
 * 6) 토큰 발행
 * 7) 리다이렉션 페이지로 이동 : vue 로 jwt , 유저정보를 전송함
 * <p>
 * 참고) 함수
 * - UriComponentsBuilder.fromUriString("기본url")
 * .queryParam("키", 값)    // 쿼리스트링 변수명, 값
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SocialLoginSuccess extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);                        // 1)
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();                          // 2)

        List<GrantedAuthority> authorities = new ArrayList(authentication.getAuthorities());
        String codeName = authorities.get(0).toString();                                             // 3) 권한

        String email = "";

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;            // 4) provider 정보

        switch (authToken.getAuthorizedClientRegistrationId()) {                                     // 5)
            case "google":
                Map<String, Object> googleUser = oAuth2User.getAttributes();
                email = (String) googleUser.get("email");
                break;
            case "naver":
                Map<String, Object> naverUser = (Map<String, Object>) oAuth2User.getAttributes().get("response");
                email = (String) naverUser.get("email");
                break;
        }

        String jwt = jwtUtils.generateJwtToken(email);                                                // 6)

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/auth-redirect")  // 7)
                .queryParam("accessToken", jwt)
                .queryParam("email", email)
                .queryParam("codeName", codeName)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
