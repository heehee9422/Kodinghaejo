package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.FileRepository;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.ReplyRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {
	
	private final BoardRepository boardRepository;
	private final FileRepository fileRepository;
	private final MemberRepository memberRepository;
	private final ReplyRepository replyRepository;

	//게시물 등록하기
	@Override
	public void write(BoardDTO board) throws Exception {
		board.setRegdate(LocalDateTime.now());
		board.setHitCnt(0);
		boardRepository.save(board.dtoToEntity(board));	
	}
	
	//게시물 리스트보기
	@Override
	public List<BoardEntity> getPosts(){
		return boardRepository.findByIsUseOrderByRegdateDesc("Y");
	}
	
	//게시물 내용 보기
	@Override
	public BoardDTO view(Long idx) throws Exception {
		return boardRepository.findById(idx).map(view -> new BoardDTO(view)).get();
	}
	
	//게시물 수정하기
	@Override
	public void modify(BoardDTO board)throws Exception{
		BoardEntity boardEntity = boardRepository.findById(board.getIdx()).get();
		boardEntity.setTitle(board.getTitle());
		boardEntity.setContent(board.getContent());
		boardRepository.save(boardEntity);
		
	}
	
	//게시물 비활성화 (isUse y->n)
	@Override
	public void deactivePost(BoardDTO board)throws Exception {
		BoardEntity boardEntity = boardRepository.findById(board.getIdx()).get();
		boardEntity.setIsUse("N"); // isUse 값을 N으로 설정하여 비활성화
		boardRepository.save(boardEntity);
	}
	
	
	

	//댓글 목록보기
	public List<ReplyEntity>replyView(Long prntIdx){
		return replyRepository.findByPrntIdx(prntIdx);
	}
	

	//댓글 등록
	@Override
	public void replyWrite(ReplyDTO reply)throws Exception{
		reply.setRegdate(LocalDateTime.now());
		replyRepository.save(reply.dtoToEntity(reply));
	}
			
	// 댓글 삭제 (isUse를 "N"으로 설정)
	@Override
	public void replyDeactive(ReplyInterface reply) {
		ReplyEntity replyEntity = replyRepository.findById(reply.getIdx()).get();
		replyEntity.setIsUse("N");
		replyRepository.save(replyEntity);
	}
	
	//댓글 수정
	@Override
	public void replyUpdate(ReplyInterface reply){
		ReplyEntity replyEntity = replyRepository.findById(reply.getIdx()).get();
		replyEntity.setContent(reply.getContent());	
		replyRepository.save(replyEntity);
	}

}