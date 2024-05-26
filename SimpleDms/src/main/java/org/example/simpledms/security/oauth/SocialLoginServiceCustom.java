package org.example.simpledms.security.oauth;

import lombok.extern.slf4j.Slf4j;
import org.example.simpledms.model.entity.auth.Member;
import org.example.simpledms.repository.auth.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * fileName : CustomOAuth2UserService
 * author : kangtaegyung
 * date : 2022/12/16
 * description :
 * 알고리즘
 * 1) OAuth2UserService : 유저정보 있는 클래스
 * 2) registrationId : google, naver, kakao 같은 이름이 있음, 이것으로 각 서비스를 구분함
 * 3) OAuth2 로그인 진행시 키가 되는 필드값, 인증토큰(PK 와 같음)
 * 4) registrationId 에 따라 구글함수, 네이버함수, 카카오함수를 실행하는 함수
 * 5) 소셜 기본정보 DB 저장, 유저가 있으면 무시
 * 6) 소셜유저 생성 및 내보내기
 */
@Slf4j
@Service
public class SocialLoginServiceCustom implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> socialLoginService = new DefaultOAuth2UserService();              // 1)
        OAuth2User socialLogin = socialLoginService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();                                   // 2)
        String socialKey = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();                                                         // 3)

        SocialProviders socialProviders = SocialProviders.of(registrationId, socialKey, socialLogin.getAttributes());      // 4)

        saveSocialIdOrSkip(socialProviders);                                                                               // 5)

        return new DefaultOAuth2User(                                                                                      // 6)
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                socialProviders.getSocialUser(),
                socialProviders.getSocialKey());
    }

    private void saveSocialIdOrSkip(SocialProviders socialProviders) {
        try {
            if(memberRepository.existsById(socialProviders.getEmail()) == false) {
                memberRepository.save(socialProviders.getDefaultUser());
            }
        } catch (Exception e) {
            log.debug("saveSocialIdOrSkip 에러" ,e.getMessage());
        }
    }
}