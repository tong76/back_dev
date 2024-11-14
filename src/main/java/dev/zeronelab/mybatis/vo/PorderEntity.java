package dev.zeronelab.mybatis.vo;

import lombok.Data;

@Data
public class PorderEntity {
    private String paymentKey;
    private String post;
    private String podate;
    private int pono;
    private int poq;
    private int poprc;
    private int mno;

    // 상품테이블 조인 결과를 받기위함..
    private int pno;
    private int sno;
    private String pname;
    private String pimg;

    // 배송테이블 조인 결과를 받기위함..
    private String dadd;
    private String dname;
    private String dcell;
}
