package dev.zeronelab.mybatis.controller;

import dev.zeronelab.mybatis.dao.GoodsMapper;
import dev.zeronelab.mybatis.vo.ReviewEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GoodsController {

    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private GoodsMapper goodsMapper;

    @RequestMapping(value = "goods/goodspopuplist", method = RequestMethod.GET)
    public Map<String, Object> goodsPopupList() throws Exception{

        Map<String, Object> rtnObj = new HashMap<>();

        rtnObj.put("goodspopuplist", goodsMapper.goodsPopupList());

        return rtnObj;
    }

    @RequestMapping(value = "goods/goodslist/{sno}", method = RequestMethod.GET)
    public Map<String, Object> goodsList(@PathVariable("sno") Integer sno) throws Exception{

        Map<String, Object> rtnObj = new HashMap<>();

        rtnObj.put("goodslist", goodsMapper.goodsList(sno));

        return rtnObj;
    }

    @RequestMapping(value = "goods/goodsdetail/{sno}/{pno}", method = RequestMethod.GET)
    public Map<String, Object> goodsDetail(@PathVariable("sno") Integer sno, @PathVariable("pno") Integer pno) throws Exception{

        Map<String, Object> rtnObj = new HashMap<>();

        rtnObj.put("goodsdetail", goodsMapper.goodsDetail(sno, pno));

        return rtnObj;
    }

    @RequestMapping(value = "goods/reviewlist/{pno}", method = RequestMethod.GET)
    public Map<String, Object> reviewList(@PathVariable Integer pno) throws Exception{

        Map<String, Object> rtnObj = new HashMap<>();

        rtnObj.put("reviewlist", goodsMapper.reviewList(pno));

        return rtnObj;
    }

    @RequestMapping(value = "goods/writereview", method = RequestMethod.POST)
    public ResponseEntity<String> writeReview(@RequestBody ReviewEntity reviewEntity) throws Exception{

        ResponseEntity<String> entity = null;
        try {
            goodsMapper.writeReview(reviewEntity);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

    @RequestMapping(value = "goods/modifyreview/{rno}", method = RequestMethod.POST)
    public ResponseEntity<String> modifyReview(@PathVariable Integer rno, @RequestBody ReviewEntity reviewEntity) throws Exception{

        ResponseEntity<String> entity = null;
        try {
            reviewEntity.setRno(rno);
            goodsMapper.modifyReview(reviewEntity);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

    @RequestMapping(value = "goods/deletereview/{rno}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReview(@PathVariable Integer rno) throws Exception{

        ResponseEntity<String> entity = null;
        try {
            goodsMapper.deleteReview(rno);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }
}
