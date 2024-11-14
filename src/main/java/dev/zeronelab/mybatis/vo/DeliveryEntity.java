package dev.zeronelab.mybatis.vo;

import java.util.Date;

import lombok.Data;

@Data
public class DeliveryEntity {
    private int dno;
    private int pono;
    private Date ddate;
    private String dadd;
    private String dname;
    private String dcell;
}
