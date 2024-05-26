package org.example.simpledms.model.dto;

import lombok.*;

/**
 * fileName : UserRes
 * author : GGG
 * date : 2024-04-15
 * description : 프론트로 전송할 정보를 담은 DTO
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {
    private String accessToken;          // 웹토큰

    private String tokenType = "Bearer"; // Bearer 고정
    private String email;
    private String codeName;             // 권한명

    public UserRes(String accessToken, String email, String codeName) {
        this.accessToken = accessToken;
        this.email = email;
        this.codeName = codeName;
    }
}
