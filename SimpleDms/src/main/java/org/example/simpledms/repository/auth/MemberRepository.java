package org.example.simpledms.repository.auth;

import org.example.simpledms.model.entity.auth.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @fileName : MemberRepository
 * @author : GGG
 * @since  : 2024-04-15
 * description : 회원 레포지토리
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    //  TODO : oauth2 사용
    Optional<Member> findByEmail(String email);
}
