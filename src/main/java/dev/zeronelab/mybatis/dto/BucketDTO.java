package dev.zeronelab.mybatis.dto;

import lombok.Data;

@Data
public class BucketDTO{
    private Integer mno;
    private Integer sno;
    private Integer pno;
    private Integer bkcnt;
    private Integer[] selects;
}