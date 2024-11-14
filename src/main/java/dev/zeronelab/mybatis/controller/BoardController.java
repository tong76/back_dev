package dev.zeronelab.mybatis.controller;

import dev.zeronelab.mybatis.dao.BoardMapper;
import dev.zeronelab.mybatis.dao.MemberMapper;
import dev.zeronelab.mybatis.dao.ReplyMapper;
import dev.zeronelab.mybatis.dto.BoardDTO;
import dev.zeronelab.mybatis.vo.BoardEntity;
import dev.zeronelab.mybatis.vo.PageMaker;
import dev.zeronelab.mybatis.vo.ReplyEntity;
import dev.zeronelab.mybatis.vo.SearchCriteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ReplyMapper replyMapper;
    /*
     * @GetMapping("boardList")
     * public @ResponseBody Map<String, Object> boardList(){
     * Map<String, Object> map = new HashMap<>();
     * 
     * List<BoardEntity> list = boardMapper.listBoard();
     * 
     * map.put("boardList", list);
     * 
     * return map;
     * }
     */

    // 게시글 페이지 리스트
    @GetMapping("/list/{page}")
    public ResponseEntity<Map<String, Object>> selectNBoardListPage(@PathVariable("page") Integer page,
            @ModelAttribute(value = "cri") SearchCriteria cri) {
        log.info(cri);
        ResponseEntity<Map<String, Object>> entity = null;

        try {// 검색 조건이 없으면 새로운 SearchCriteria 객체를 생성하여 사용
            if (cri == null) {
                cri = new SearchCriteria();
            }
            cri.setPage(page);

            PageMaker pageMaker = new PageMaker();
            pageMaker.setCri(cri);

            Map<String, Object> map = new HashMap<>();
            List<BoardEntity> list;

            // 검색 조건이 있는 경우와 없는 경우를 구분하여 데이터를 가져옴
            int boardCount;
            if (cri.hasSearchCondition()) {
                // 검색 조건이 있는 경우
                list = boardMapper.listSearch(cri);
                boardCount = boardMapper.listSearchCount(cri);
            } else {
                // 검색 조건이 없는 경우
                list = boardMapper.selectBoardList(cri);
                boardCount = boardMapper.selectBoardListCount(cri);

            }
            pageMaker.setTotalCount(boardCount);

            map.put("list", list);
            map.put("pageMaker", pageMaker);

            entity = new ResponseEntity<>(map, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return entity;
    }

    @GetMapping("boardPage/{bno}")
    public Map<String, Object> boardPage(@PathVariable Long bno) {

        boardMapper.boardCnt(bno);

        Map<String, Object> map = new HashMap<>();

        List<BoardEntity> list = new ArrayList<>();

        list.add(boardMapper.boardPage(bno));

        map.put("boardPage", list);

        return map;
    }

    @Transactional
    @PostMapping("boardRegist")
    public String boardRegist(@RequestBody BoardEntity entity) throws Exception {
        Long mno = memberMapper.findMno(entity.getBwriter());
        entity.setMno(mno);
        log.info("Entity = " + entity);
        boardMapper.boardRegist(entity);

        String[] files = entity.getFiles();

        if (files == null) {
            return "succ";
        } else {
            for (String fullName : files) {
                boardMapper.boardAddAttach(fullName);
            }
        }

        return "succ";
    }

    @PostMapping("boardModify")
    public String boardModify(@RequestBody BoardDTO dto) throws Exception {
        log.info("BoardDTO = " + dto);
        boardMapper.boardModify(dto);

        boardMapper.removeAttach(dto);

        String[] files = dto.getFiles();

        log.info("files: " + files);

        if (files == null) {
            return "succ";
        } else {
            for (String fullName : files) {
                boardMapper.replaceAttach(dto.getBno(), fullName);
            }
        }

        return "succ";
    }

    @Transactional
    @PostMapping("boardDelete")
    public String boardDelete(@RequestBody Map<String, Long> request) {
        // 리액트에서 bno를 json형태로 전달하고 있어서 Map형식으로 받고 거기에 bno를 get으로 얻는다.
        Long bno = request.get("bno");

        BoardDTO dtoBno = new BoardDTO();
        dtoBno.setBno(bno);

        log.info("삭제합니다.");
        boardMapper.removeAttach(dtoBno);
        replyMapper.replyAllDelete(bno);
        boardMapper.boardDelete(bno);
        return "succ";
    }

    @GetMapping("getAttach/{bno}")
    public Map<String, Object> getAttach(@PathVariable Integer bno) {

        Map<String, Object> rtnObj = new HashMap<>();

        rtnObj.put("getAttach", boardMapper.getAttach(bno));

        return rtnObj;
    }
}
