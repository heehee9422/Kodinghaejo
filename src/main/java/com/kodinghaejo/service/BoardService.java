package com.kodinghaejo.service;

import java.util.List;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ReplyEntity;

public interface BoardService {

	//게시물 등록 하기
	public void write(BoardDTO board) throws Exception;
	
	//게시물 목록 보기
	public List<BoardEntity> getPosts();
	
	//게시물 상세 내용 보기
	public BoardDTO view(Long idx) throws Exception;
	
	//게시물 수정
	public void modify(BoardDTO board)throws Exception;
	
	//게시물 비활성화 (isUse y->n)
	public void deactivePost(BoardDTO board)throws Exception;
	
	//댓글 목록보기
	public List<ReplyEntity>replyView(Long prntIdx);
	
	//댓글등록
	public void replyWrite(ReplyDTO reply)throws Exception;
	
	// 댓글 삭제 (isUse를 "N"으로 설정)
	public void replyDeactive(ReplyInterface reply); 
	
	//댓글 수정
	public void replyUpdate(ReplyInterface replyDTO);
	
}