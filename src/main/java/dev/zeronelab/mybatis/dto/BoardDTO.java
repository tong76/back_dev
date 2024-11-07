package dev.zeronelab.mybatis.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDTO {
    private Long bno;
    private Long mno;
    private String btitle;
    private String bcon;
    private String bwriter;
}
