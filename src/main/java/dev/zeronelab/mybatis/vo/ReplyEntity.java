package dev.zeronelab.mybatis.vo;

import lombok.Data;

@Data
public class ReplyEntity {
    private Long cno;
    private Long bno;
    private Long mno;
    private String ccon;
    private String cwriter;
    private String cdate;
}
