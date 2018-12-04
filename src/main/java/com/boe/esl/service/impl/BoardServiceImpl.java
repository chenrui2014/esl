package com.boe.esl.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.BoardDao;
import com.boe.esl.model.Board;
import com.boe.esl.service.BoardService;
import com.boe.esl.vo.BoardVO;

@Transactional
@Service
public class BoardServiceImpl extends BaseServiceImpl<Board, Long, BoardVO> implements BoardService {

	@Autowired
	private BoardDao boardDao;

	@Override
	public BoardVO convertEntity(Board board) {
		BoardVO boardVO = new BoardVO();
		boardVO.setId(board.getId());
		boardVO.setCode(board.getCode());
		boardVO.setName(board.getName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatter.format(board.getUpdateTime());
		boardVO.setUpdateTime(formattedDate);
		return boardVO;
	}

	@Override
	public Board convertVO(BoardVO boardVO) {
		Board board = new Board();
		board.setId(boardVO.getId());
		board.setCode(boardVO.getCode());
		board.setName(boardVO.getName());
		Timestamp ts = new Timestamp(System.currentTimeMillis()); 
		try {   
            ts = Timestamp.valueOf(boardVO.getUpdateTime());     
        } catch (Exception e) {   
            e.printStackTrace();   
        } 
		board.setUpdateTime(ts);
		return board;
	}

	@Override
	public List<BoardVO> convertEntityList(List<Board> boardList) {
		List<BoardVO> boardVOList = new ArrayList<BoardVO>();
		for (Board board : boardList) {
			BoardVO boardVO = convertEntity(board);
			boardVOList.add(boardVO);
		}
		return boardVOList;
	}

	@Override
	public List<Board> convertVOList(List<BoardVO> entityVOList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseDao<Board, Long> getDAO() {
		return this.boardDao;
	}

}
