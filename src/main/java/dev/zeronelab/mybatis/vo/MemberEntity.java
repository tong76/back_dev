package dev.zeronelab.mybatis.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MemberEntity {
    private int mno;
    private String mid;
    private String mpw;
    private String mname;
    private Date mdate;
    private int mpoint;
    private String mcell;
    private String mcell1;
    private String mcell2;
    private String mcell3;
    private String memail;
    private String memail1;
    private String memail2;
    private int mgrade;

    private int count;
}
