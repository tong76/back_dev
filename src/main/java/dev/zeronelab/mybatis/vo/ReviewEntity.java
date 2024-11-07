package dev.zeronelab.mybatis.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewEntity {
    private int rno;
    private int pno;
    private int mno;
    private String rcon;
    private String rimg;
    private Date rdate;
    private int rgrade;

    private String mid;
}
