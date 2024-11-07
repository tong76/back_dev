package dev.zeronelab.mybatis.dto;

import lombok.Data;

@Data
public class MemberDTO {
    private String token1;
    private String token2;
    private String sessionInfo;
    private String sessionLogin;
}
