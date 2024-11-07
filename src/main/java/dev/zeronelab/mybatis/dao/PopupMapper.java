package dev.zeronelab.mybatis.dao;

import dev.zeronelab.mybatis.vo.PopupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Mapper
public interface PopupMapper {

    public List<PopupEntity> listPopup();

    public PopupEntity read(Long sno);
}
