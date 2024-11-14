package dev.zeronelab.mybatis.dto;

import dev.zeronelab.mybatis.vo.BucketEntity;
import lombok.Data;

@Data
public class PaymentRequest {
    // 결제 승인
    private String paymentKey;
    private String orderId;
    private int amount;

    // 결제 조회
    private BucketEntity bucketEntity;
    private String status;

    // 배송
    private String address;
    private String name;
    private String cell;
}
