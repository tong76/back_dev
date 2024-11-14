package dev.zeronelab.mybatis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.zeronelab.mybatis.dao.BucketMapper;
import dev.zeronelab.mybatis.dao.PaymentMapper;
import dev.zeronelab.mybatis.dto.PaymentRequest;
import dev.zeronelab.mybatis.service.PaymentService;
import dev.zeronelab.mybatis.vo.BucketEntity;
import dev.zeronelab.mybatis.vo.DeliveryEntity;
import dev.zeronelab.mybatis.vo.PorderEntity;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BucketMapper bucketMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    // 결제 승인 확인 API
    @RequestMapping(value = "/payment/confirm", method = RequestMethod.POST)
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // 결제 확인 요청 처리
            Object confirmResponse = paymentService.confirmPayment(paymentRequest);
            return ResponseEntity.ok().body(confirmResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("결제 승인 실패: " + e.getMessage());
        }
    }

    // 결제 조회 API
    @RequestMapping(value = "/payment/orderdetail", method = RequestMethod.POST)
    public ResponseEntity<?> checkPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // 결제 확인 요청 처리
            Object checkResponse = paymentService.checkPayment(paymentRequest);
            return ResponseEntity.ok().body(checkResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("결제 승인 실패: " + e.getMessage());
        }
    }

    @Transactional
    @RequestMapping(value = "/payment/paymentprocess", method = RequestMethod.POST)
    public ResponseEntity<String> paymentProcess(@RequestBody PaymentRequest paymentRequest) throws Exception {

        ResponseEntity<String> entity = null;

        Integer mno = paymentRequest.getBucketEntity().getMno();
        Integer sno = paymentRequest.getBucketEntity().getSno();
        Integer pno = paymentRequest.getBucketEntity().getPno();
        Integer[] selects = paymentRequest.getBucketEntity().getProducts();
        String paymentKey = paymentRequest.getPaymentKey();
        String status = paymentRequest.getStatus();

        try {
            if (sno != null && sno != 0
                    && pno != null && pno != 0) {
                List<BucketEntity> bucketList = bucketMapper.selectOnebucketList(mno, sno, pno);

                for (BucketEntity bucket : bucketList) {
                    // 데이터베이스에서 받아온 객체를 저장할 변수 선언
                    PaymentRequest fommatedEntity = new PaymentRequest();

                    // 받아온 값들과 주문번호 저장
                    fommatedEntity.setBucketEntity(bucket);
                    fommatedEntity.setPaymentKey(paymentKey);
                    fommatedEntity.setStatus(status);

                    paymentMapper.addPaymentDetail(fommatedEntity);
                    bucketMapper.deleteBucket(bucket);
                }
            } else if (selects != null && selects.length > 0) {
                List<BucketEntity> bucketList = bucketMapper.selectBucketList(mno, selects);

                for (BucketEntity bucket : bucketList) {
                    PaymentRequest fommatedEntity = new PaymentRequest();

                    fommatedEntity.setBucketEntity(bucket);
                    fommatedEntity.setPaymentKey(paymentKey);
                    fommatedEntity.setStatus(status);

                    paymentMapper.addPaymentDetail(fommatedEntity);
                    bucketMapper.deleteBucket(bucket);
                }
            }
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

    @RequestMapping(value = "/payment/ordersummary", method = RequestMethod.POST)
    public Map<String, Object> orderList(@RequestBody PaymentRequest paymentRequest) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        rtnObj.put("orderlist", paymentMapper.orderList(paymentRequest.getBucketEntity().getMno()));

        return rtnObj;
    }

    @RequestMapping(value = "/payment/deleteordersummary", method = RequestMethod.POST)
    public ResponseEntity<String> deleteOrderSummary(@RequestBody PaymentRequest paymentRequest) throws Exception {

        ResponseEntity<String> entity = null;

        try {
            paymentMapper.deleteOrderSummary(paymentRequest);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

    @Transactional
    @RequestMapping(value = "/payment/deliveryregist", method = RequestMethod.POST)
    public ResponseEntity<String> deliveryRegist(@RequestBody PaymentRequest paymentRequest) throws Exception {

        ResponseEntity<String> entity = null;

        try {
            List<PorderEntity> paymentInfo = paymentMapper.findPaymentKey(paymentRequest);

            for (PorderEntity porder : paymentInfo) {

                DeliveryEntity deliveryEntity = new DeliveryEntity();

                deliveryEntity.setPono(porder.getPono());
                deliveryEntity.setDadd(paymentRequest.getAddress());
                deliveryEntity.setDname(paymentRequest.getName());
                deliveryEntity.setDcell(paymentRequest.getCell());

                paymentMapper.deliveryRegist(deliveryEntity);
            }

            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

    @RequestMapping(value = "/payment/deliveryinfo", method = RequestMethod.POST)
    public Map<String, Object> deliveryInfo(@RequestBody PaymentRequest paymentRequest) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        rtnObj.put("deliveryinfo", paymentMapper.deliveryInfo(paymentRequest));

        return rtnObj;
    }
}
