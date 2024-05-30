package org.example.simpledms.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simpledms.model.dto.NewUser;
import org.example.simpledms.model.dto.UserReq;
import org.example.simpledms.model.dto.UserRes;
import org.example.simpledms.model.entity.auth.Member;
import org.example.simpledms.security.jwt.JwtUtils;
import org.example.simpledms.service.auth.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @fileName : AuthController
 * @author : GGG
 * @since : 2024-04-15
 * description : 인증(로그인/회원가입) 컨트롤러
 *
 *  로그인 함수 알고리즘
 *    1) id/패스워드로 인증 => 인증된 객체(리턴)
 *    2) 인증된 객체는 홀더(필통)에 넣어서 관리됨
 *    3) 웹토큰 발행(생성) => 프론트 전송
 *    4) 권한 정보 : ROLE_USER, ROLE_ADMIN
 *      => Set -> List => new ArrayList(Set객체) : 변경됨
 *      => authentication.getAuthorities() : 권한 배열 집합(set)
 *    5) DTO : jwt(웹토큰), 이메일, 권한명 => 프론트에 전송
 *
 *  회원가입 함수 알고리즘
 *    1) 이메일이 DB 있는지 확인해서 있으면 에러발생시킴
 *    2) 새 사용자 생성
 *    3) 새 사용자 저장
 *    4) 새 사용자 프론트 전송
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;                                 // 인증관리(스프링시큐리티 제공)

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserReq userReq) {
            Authentication authentication                                                       // 1)
                    = authenticationManagerBuilder.getObject().authenticate(
                    new UsernamePasswordAuthenticationToken(userReq.getEmail(), userReq.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);                // 2)

            String jwt = jwtUtils.generateJwtToken(authentication);                              // 3)

            List<GrantedAuthority> authorities = new ArrayList(authentication.getAuthorities()); // 권한배열
            String codeName = authorities.get(0).toString();                                     // 4)

            UserRes userRes = new UserRes(                                                       // 5)
                    jwt,
                    userReq.getEmail(),
                    codeName
            );
            return new ResponseEntity<>(userRes, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody NewUser newUser) {
            if(memberService.existsById(newUser.getEmail())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Member member = new Member(
                    newUser.getEmail(),                                                     // 로그인 ID
                    passwordEncoder.encode(newUser.getPassword()),                          // 암호화 적용
                    newUser.getName(),                                                      // 이름
                    newUser.getCodeName()                                                   // 권한
            );

            memberService.save(member);

            return new ResponseEntity<>("회원저장 성공", HttpStatus.OK);
    }
}
