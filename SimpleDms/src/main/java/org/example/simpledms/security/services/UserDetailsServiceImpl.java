package org.example.simpledms.security.services;

import lombok.RequiredArgsConstructor;
import org.example.simpledms.model.entity.auth.Member;
import org.example.simpledms.repository.auth.MemberRepository;
import org.example.simpledms.security.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @fileName : UserDetailsServiceImpl
 * @author : GGG
 * @since  : 2024-03-27
 * description : DB 에 사용자 있는지 확인하는 클래스(함수)
 *   - 스프링에서 제공한 인터페이스 상속(함수명 고정)
 *   (참고) js 화살표 함수 : function(x){return x+1;} : (x) => x + 1;
 *         java 람다식   : interface x(함수())       : (x) -> x + 1;
 *   사용법 : 자료형 변수 = 옵셔널객체.orElseThrow(new 예외처리클래스("에러메세지"));
 *          => 옵셔널객체의 결과가 null 이면 에러메세지 출력, 아니면 변수에 저장됨
 *
 *   1) GrantedAuthority : 스프링시큐리티에서 사용하는 권한클래스, 새로운 권한은 생성해서 사용
 *      => codeName      : 권한명 (ROLE_ADMIN, ROLE_USER)
 *      => 사용법 : GrantedAuthority 변수 = new SimpleGrantedAuthority(생성될권한명);
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member
                = memberRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("email 없음:" + email));

        GrantedAuthority authority = new SimpleGrantedAuthority(member.getCodeName());   // 1)

//        TODO:  2-2) 권한 배열에(List, Set 등) 넣기 : Set 에 넣기
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);    // Set 배열에 권한 넣기

//        생성자 : (email, password, 권한배열)
        return new MemberDto(member.getEmail(),
                            member.getPassword(),
                            authorities
                );
    }
}
