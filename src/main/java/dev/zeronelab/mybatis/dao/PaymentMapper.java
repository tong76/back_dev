package dev.zeronelab.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import dev.zeronelab.mybatis.dto.PaymentRequest;
import dev.zeronelab.mybatis.vo.DeliveryEntity;
import dev.zeronelab.mybatis.vo.PorderEntity;

@Mapper
public interface PaymentMapper {
    void addPaymentDetail(PaymentRequest paymentRequest);

    List<PorderEntity> orderList(int mno);

    void deleteOrderSummary(PaymentRequest paymentRequest);

    List<PorderEntity> findPaymentKey(PaymentRequest paymentRequest);

    void deliveryRegist(DeliveryEntity deliveryEntity);

    List<PorderEntity> deliveryInfo(PaymentRequest paymentRequest);
}
