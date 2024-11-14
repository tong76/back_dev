package dev.zeronelab.mybatis.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.zeronelab.mybatis.dto.PaymentRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class PaymentService {

    private static final String SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    private static final String CONFIRM_API_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private static final String CHECK_API_URL = "https://api.tosspayments.com/v1/payments/";

    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object confirmPayment(PaymentRequest paymentRequest) throws Exception {

        // Basic Auth 인증 헤더 생성 (시크릿 키를 base64로 인코딩)
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

        // API 요청에 필요한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("Content-Type", "application/json");

        // 결제 승인 요청 데이터 생성
        String body = String.format(
                "{\"orderId\": \"%s\", \"amount\": %d, \"paymentKey\": \"%s\"}",
                paymentRequest.getOrderId(), paymentRequest.getAmount(), paymentRequest.getPaymentKey());

        // POST 요청 보내기
        ResponseEntity<Object> response = restTemplate.exchange(
                CONFIRM_API_URL,
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(body, headers),
                Object.class);

        // 응답 처리
        return response.getBody();
    }

    public Object checkPayment(PaymentRequest paymentRequest) throws Exception {

        // Basic Auth 인증 헤더 생성 (시크릿 키를 base64로 인코딩)
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

        // API 요청에 필요한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        System.out.println(CHECK_API_URL+paymentRequest.getPaymentKey());
        System.out.println(authHeader);

        // GET 요청 보내기
        ResponseEntity<Object> response = restTemplate.exchange(
                CHECK_API_URL+paymentRequest.getPaymentKey(),
                HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers),
                Object.class);

        // 응답 처리
        return response.getBody();
    }
}
