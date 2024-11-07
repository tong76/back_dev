package dev.zeronelab.mybatis.dao;

import dev.zeronelab.mybatis.dto.ReplyDTO;
import dev.zeronelab.mybatis.vo.ReplyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyMapper {

    public void replyRegist(ReplyEntity entity);

    //bno를 기준으로 댓글 리스트 뽑아오기
    public List<ReplyEntity> replyList(Long bno);

    //cno를 기준으로 댓글 수정
    public void replyModify(ReplyDTO dto);

    //cno를 기준으로 댓글 삭제
    public void replyDelete(Long cno);

    //댓글 전부 삭제
    public void replyAllDelete(Long bno);

}
