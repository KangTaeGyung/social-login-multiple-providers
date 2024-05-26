package org.example.simpledms.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * fileName : MemberDto
 * author : GGG
 * date : 2024-03-27
 * description : 검증된 유저 클래스 정의
 *      TODO
 *          1) 스프링에서 제공한 유저 클래스 : User, UserDetails 2중에 1개 상속
 *          2) 검증된 유저 객체에는 권한이 있음 : 권한 넣기
 *          3) 로그인 ID 는 email 로 사용( spring security : username 사용 )
 */
@Setter
@Getter
@ToString
public class MemberDto extends User {

    private String email; // 로그인 ID

    public MemberDto(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.email = email;
    }
}
