package dev.zeronelab.mybatis.vo;

import lombok.Data;

@Data
public class BoardEntity {
    private Long bno;

    private Long mno;

    private String btitle;

    private String bcon;

    private String bwriter;

    private String bdate;

    private String bfile;

    private Long bcnt;

    private Long bccnt;

    private String[] files;
}


