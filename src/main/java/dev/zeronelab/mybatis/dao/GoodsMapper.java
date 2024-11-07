package dev.zeronelab.mybatis.dao;

import dev.zeronelab.mybatis.vo.GoodsEntity;
import dev.zeronelab.mybatis.vo.PopupEntity;
import dev.zeronelab.mybatis.vo.ReviewEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {

    public List<PopupEntity> goodsPopupList() throws Exception;

    public List<GoodsEntity> goodsList(Integer sno) throws Exception;

    public List<GoodsEntity> goodsDetail(Integer sno, Integer pno) throws Exception;

    public List<ReviewEntity> reviewList(Integer pno) throws Exception;

    public void writeReview(ReviewEntity reviewEntity) throws Exception;

    public void modifyReview(ReviewEntity reviewEntity) throws Exception;

    public void deleteReview(Integer rno) throws Exception;
}
