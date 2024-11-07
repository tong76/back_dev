package dev.zeronelab.mybatis.controller;

import dev.zeronelab.mybatis.dao.BoardMapper;
import dev.zeronelab.mybatis.dao.MemberMapper;
import dev.zeronelab.mybatis.dao.ReplyMapper;
import dev.zeronelab.mybatis.dto.ReplyDTO;
import dev.zeronelab.mybatis.vo.ReplyEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
public class ReplyController {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private MemberMapper memberMapper;

    @GetMapping("/replyList/{bno}")
    public Map<String, Object> replyList(@PathVariable Long bno){
        Map<String, Object> map = new HashMap<>();
        log.info("bno = "+bno);
        List<ReplyEntity> list = replyMapper.replyList(bno);

        map.put("replyList",list);

        return map;
    }

    @Transactional
    @PostMapping("/replyRegist/{bno}")
    public String replyRegist(@PathVariable Long bno, @RequestBody ReplyEntity entity)throws Exception{
        log.info("entity = "+entity);
        Long mno = memberMapper.findMno(entity.getCwriter());
        entity.setMno(mno);
        replyMapper.replyRegist(entity);
        boardMapper.boardBccnt(bno);
        return "succ";
    }

    @PostMapping("/replyModify")
    public String replyModify(@RequestBody ReplyDTO dto) throws Exception{
        log.info("ReplyDTO = "+dto);
        replyMapper.replyModify(dto);
        return "succ";
    }

    @Transactional
    @GetMapping("/replyDelete/{cno}")
    public String replyDelete(@PathVariable Long cno) throws Exception{
        log.info("cno = "+cno);
        Long bno = boardMapper.boardFindBno(cno);
        log.info("bno = "+bno);
        replyMapper.replyDelete(cno);
        boardMapper.boardBccntm(bno);
        return "succ";
    }
}
