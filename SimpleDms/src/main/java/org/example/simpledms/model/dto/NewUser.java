package org.example.simpledms.model.dto;

import lombok.*;

/**
 * @fileName : NewUser
 * @author : GGG
 * @since : 2024-04-16
 * description : 회원가입 용 DTO
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {
    private String email;    // 로그인 ID
    private String password;
    private String name;
    private String codeName; // 권한명
}
