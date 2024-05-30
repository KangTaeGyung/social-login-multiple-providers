package org.example.simpledms.model.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.example.simpledms.model.common.BaseTimeEntity2;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @fileName : Member
 * @author : GGG
 * @since  : 2024-04-15
 * description : 회원 엔티티, 소프트삭제 사용
 */
@Entity
@Table(name="TB_MEMBER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "DELETE_YN = 'N'")
@SQLDelete(sql ="UPDATE TB_MEMBER SET DELETE_YN = 'Y', DELETE_TIME=TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE EMAIL = ?")
public class Member extends BaseTimeEntity2 {
    @Id
    private String email;    // 로그인 ID
    private String password;
    private String name;
    private String codeName; // 권한명 ( ROLE_USER, ROLE_ADMIN )
}