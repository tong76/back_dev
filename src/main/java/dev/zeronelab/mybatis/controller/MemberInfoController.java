package dev.zeronelab.mybatis.controller;

import dev.zeronelab.mybatis.dao.MemberMapper;
import dev.zeronelab.mybatis.util.JWTUtil;
import dev.zeronelab.mybatis.vo.MemberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class MemberInfoController {

    private Logger logger = LoggerFactory.getLogger(MemberInfoController.class);

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private JWTUtil jwtUtil;

    @ResponseBody
    @PostMapping("/api/passwordcheck")
    public ResponseEntity<String> checkPassword(@RequestBody Map<String, Object> payload) throws Exception {
        Integer mno = (Integer) payload.get("mno");   // 회원 번호
        String inputPassword = (String) payload.get("mpw");   // 사용자가 입력한 현재 비밀번호
        MemberEntity member = memberMapper.selectPasswordByMno(mno);


        logger.info("mno로 수집한 패스워드:"+ member);
        logger.info("입력된 비밀번호: " + inputPassword);
        logger.info("저장된 비밀번호 (암호화된): " + member.getMpw());

        // 회원이 존재하지 않으면 404 응답
        if (member == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 비밀번호 비교 (입력한 비밀번호 vs 저장된 암호화된 비밀번호)
        if (passwordEncoder.matches(inputPassword, member.getMpw())) {

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api/infoemailcheck", method = RequestMethod.POST)
    public Map<String, Object> infoEmailCheck(@RequestBody MemberEntity memberEntity) throws Exception{

        Map<String, Object> rtnObj = new HashMap<>();

        List<MemberEntity> count = memberMapper.infoEmailCheck(memberEntity);

        rtnObj.put("emailCheck", count);

        return rtnObj;
    }
    @ResponseBody
    @PostMapping("/api/modifyinfo")
    public ResponseEntity<String> modifyMemberInfo(@RequestBody MemberEntity memberEntity) {


        try {

            // 수정된 비밀번호가 있을 경우, 암호화하여 저장
            if (memberEntity.getMpw() != null && !memberEntity.getMpw().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(memberEntity.getMpw());
                memberEntity.setMpw(encodedPassword);
            }
            System.out.println("membervo="+memberEntity);
            // 회원 정보 업데이트
            memberMapper.updateMemberInfo(memberEntity);

            return new ResponseEntity<>("회원정보가 성공적으로 수정되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("회원정보 수정 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/api/deleteinfo")
    public ResponseEntity<String> deleteMemberInfo(@RequestBody Map<String, Object> payload) {
        Integer mno = (Integer) payload.get("mno");   // 회원 번호

        try {
            // 1. 회원 정보 삭제
            int deletedRows = memberMapper.deleteMemberByMno(mno);

            if (deletedRows > 0) {
                return new ResponseEntity<>("회원탈퇴가 완료되었습니다.", HttpStatus.OK);  // 200 OK
            } else {
                return new ResponseEntity<>("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);  // 404 Not Found
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("탈퇴 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);  // 500 Internal Server Error
        }
    }
}


