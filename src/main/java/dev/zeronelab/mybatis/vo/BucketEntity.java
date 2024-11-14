package dev.zeronelab.mybatis.vo;

import lombok.Data;

@Data
public class BucketEntity {
    private int bkno;
    private int mno;
    private int pno;
    private int bkcnt;

    //상품테이블과 조인한 결과 받기 위함..
    private int sno;
    private String pname;
    private int pprice;
    private String pdate;
    private int pquan;
    private String pimg;
    private String pcon;

    //전체 삭제
    private Integer[] products;
}
