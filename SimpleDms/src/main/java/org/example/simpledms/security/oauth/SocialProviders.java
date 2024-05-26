package org.example.simpledms.security.oauth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.simpledms.model.entity.auth.Member;

import java.util.Map;

/**
 * fileName : OAuthsocialUser
 * author : kangtaegyung
 * date : 2022/12/16
 * description : name 는 email 의 @ 앞부분을 잘라서 저장
 * 각각 : email 저장된 속성명
 * google : (String) socialUser.get("email")
 * naver  :  Map<String, Object> response = (Map<String, Object>) socialUser.get("response") 안에 email 속성이 있음
 * (String) response.get("email")
 * kakao  : (String) socialUser.get("account_email")
 */

@Getter
public class SocialProviders {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    private Map<String, Object> socialUser;
    private String socialKey;
    private String name;
    private String email;

    @Builder
    public SocialProviders(Map<String, Object> socialUser,
                           String socialKey, String name,
                           String email) {
        this.socialUser = socialUser;
        this.socialKey = socialKey;
        this.name = name;
        this.email = email;
    }

    //    registrationId 를 체크해서 구글 / 네이버 / 카카오 정보 가져오기
    public static SocialProviders of(String registrationId,
                                     String nameAttributeName,
                                     Map<String, Object> socialUser) {
        switch (registrationId) {
            case "google":
                return ofGoogle(nameAttributeName, socialUser);
            case "naver":
                return ofNaver(nameAttributeName, socialUser);
            case "kakao":
                return ofKakao(nameAttributeName, socialUser);
            default:
                return ofGoogle(nameAttributeName, socialUser);
        }
    }

    private static SocialProviders ofGoogle(String nameAttributeName,
                                            Map<String, Object> socialUser) {
        return SocialProviders.builder()
                .name(((String) socialUser.get("email")).split("@")[0])
                .email((String) socialUser.get("email"))
                .socialUser(socialUser)
                .socialKey(nameAttributeName)
                .build();
    }

    private static SocialProviders ofNaver(String nameAttributeName,
                                           Map<String, Object> socialUser) {
        Map<String, Object> response = (Map<String, Object>) socialUser.get("response");

        return SocialProviders.builder()
                .name(((String) response.get("email")).split("@")[0])
                .email((String) response.get("email"))
                .socialUser(socialUser)
                .socialKey(nameAttributeName)
                .build();
    }

    private static SocialProviders ofKakao(String nameAttributeName,
                                           Map<String, Object> socialUser) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) socialUser.get("kakao_account");

        return SocialProviders.builder()
                .name(((String) kakaoAccount.get("email")).split("@")[0])
                .email((String) kakaoAccount.get("email"))
                .socialUser(socialUser)
                .socialKey(nameAttributeName)
                .build();
    }

    //    OAuthsocialUser에서 엔티티를 생성하는 시점은 처음 가입할 때
//    User 엔티티를 생성
    public Member getDefaultUser() {

        return Member.builder()
                .name(this.email)
                .email(this.email)
                .password(encoder.encode("123456"))
                .codeName("ROLE_USER")
                .build();
    }
}
