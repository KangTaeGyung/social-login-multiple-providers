package org.example.simpledms.model.dto;

import lombok.*;

/**
 * fileName : UserReq
 * author : GGG
 * date : 2024-04-15
 * description : 프론트에서 전달한 정보를 받을 DTO 객체
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserReq {
    private String email;     // 로그인 ID
    private String password;
}
