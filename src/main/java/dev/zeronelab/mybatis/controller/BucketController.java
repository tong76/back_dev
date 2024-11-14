package dev.zeronelab.mybatis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.zeronelab.mybatis.dao.BucketMapper;
import dev.zeronelab.mybatis.dto.BucketDTO;
import dev.zeronelab.mybatis.vo.BucketEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BucketController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private BucketMapper bucketMapper;

    @RequestMapping(value = "/bucket/addbucket", method = RequestMethod.POST)
    public ResponseEntity<String> addBucket(@RequestBody BucketEntity bucketEntity) throws Exception {

        ResponseEntity<String> entity = null;
        try {
            bucketMapper.addBucket(bucketEntity);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;

    }

    @RequestMapping(value = "/bucket/updatebucket", method = RequestMethod.POST)
    public ResponseEntity<String> updateBucket(@RequestBody BucketEntity bucketEntity) throws Exception {

        ResponseEntity<String> entity = null;
        try {
            bucketMapper.updateBucket(bucketEntity);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;

    }

    @RequestMapping(value = "/bucket/chkbucket", method = RequestMethod.POST)
    public Map<String, Object> chkBucket(@RequestBody BucketEntity bucketEntity) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        rtnObj.put("chkbucket", bucketMapper.chkBucket(bucketEntity));

        return rtnObj;

    }

    @RequestMapping(value = "/bucket/bucketlist", method = RequestMethod.POST)
    public Map<String, Object> bucketList(@RequestBody BucketDTO bucketDTO) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        Integer mno = bucketDTO.getMno();
        Integer sno = bucketDTO.getSno();
        Integer pno = bucketDTO.getPno();
        Integer bkcnt = bucketDTO.getBkcnt();
        Integer[] selects = bucketDTO.getSelects();

        if (sno != null && pno != null) {
            rtnObj.put("bucketlist", bucketMapper.selectOnebucketList(mno, sno, pno));
        } else if (selects != null && selects.length > 0) {
            rtnObj.put("bucketlist", bucketMapper.selectBucketList(mno, selects));
            return rtnObj;
        } else {
            rtnObj.put("bucketlist", bucketMapper.bucketList(mno));
            return rtnObj;
        }

        // 장바구니에 담겨있지 않으면 담는 코드
        // List<?> : 제너릭 타입의 와일드카드, bucketlist의 반환값을 List로만 받겠다는 뜻이고 그 안의 타입은 신경쓰지않음
        if (((List<?>) rtnObj.get("bucketlist")).isEmpty()) {
            BucketEntity bucketEntity = new BucketEntity();

            bucketEntity.setMno(mno);
            bucketEntity.setPno(pno);
            bucketEntity.setBkcnt(bkcnt);

            bucketMapper.addBucket(bucketEntity);
            rtnObj.put("bucketlist", bucketMapper.selectOnebucketList(mno, sno, pno));
        }

        return rtnObj;
    }

    @RequestMapping(value = "/bucket/increasequan", method = RequestMethod.POST)
    public ResponseEntity<String> increaseQuan(@RequestBody BucketEntity bucketEntity) {

        ResponseEntity<String> entity = null;

        try {
            bucketMapper.increaseQuan(bucketEntity);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

    @RequestMapping(value = "/bucket/decreasequan", method = RequestMethod.POST)
    public ResponseEntity<String> decreaseQuan(@RequestBody BucketEntity bucketEntity) {

        ResponseEntity<String> entity = null;

        try {
            bucketMapper.decreaseQuan(bucketEntity);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

    @RequestMapping(value = "/bucket/deletebucket", method = RequestMethod.POST)
    public ResponseEntity<String> deleteBucket(@RequestBody BucketEntity bucketEntity) {

        ResponseEntity<String> entity = null;

        try {
            bucketMapper.deleteBucket(bucketEntity);
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

    @RequestMapping(value = "/bucket/deletebuckets", method = RequestMethod.POST)
    public ResponseEntity<String> deleteBuckets(@RequestBody BucketEntity bucketEntity) {

        int mno = bucketEntity.getMno();

        // 선택 된 순번 저장
        Integer[] products = bucketEntity.getProducts();
        for (int product : products) {
            logger.info("products: " + product);
        }

        // 삭제 할 장바구니 번호의 상품번호 저장
        int[] pnos = new int[products.length];

        ResponseEntity<String> entity = null;

        try {
            for (int i = 0; i < products.length; i++) {
                pnos[i] = bucketMapper.getPnos(mno, products[i]);
                logger.info(i + "번째 pno: " + pnos[i]);
            }
            for (int i = 0; i < pnos.length; i++) {
                bucketEntity.setMno(mno);
                bucketEntity.setPno(pnos[i]);
                bucketMapper.deleteBucket(bucketEntity);
            }
            entity = new ResponseEntity<String>("succ", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return entity;
    }

}
