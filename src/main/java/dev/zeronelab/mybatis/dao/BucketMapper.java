package dev.zeronelab.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;

import dev.zeronelab.mybatis.vo.BucketEntity;

import java.util.List;

@Mapper
public interface BucketMapper {
    public void addBucket(BucketEntity bucketEntity) throws Exception;

    public void updateBucket(BucketEntity bucketEntity) throws Exception;

    public List<BucketEntity> chkBucket(BucketEntity bucketEntity) throws Exception;

    public List<BucketEntity> bucketList(Integer mno) throws Exception;

    // 상품상세에서 고른것을 구매창에 보이게하는것
    public List<BucketEntity> selectOnebucketList(Integer mno, Integer sno, Integer pno) throws Exception;

    // 장바구니에서 고른것을 구매창에 보이게하는것
    public List<BucketEntity> selectBucketList(Integer mno, Integer[] selects) throws Exception;

    public void increaseQuan(BucketEntity bucketEntity) throws Exception;

    public void decreaseQuan(BucketEntity bucketEntity) throws Exception;

    public void deleteBucket(BucketEntity bucketEntity) throws Exception;

    public int getPnos(int mno, int product) throws Exception;
}
