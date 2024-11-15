package com.kodinghaejo.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

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
	public void replyModify(ReplyInterface replyDTO);
	
  //댓글수 확인
  public int getReplyCountByPostId(Long prntIdx);

  //게시물 조회수 증가
	public void hitno(Long idx) throws Exception;

	// 다수의 게시물 ID에 대한 댓글 수를 조회하여 Map으로 반환
	public Map<Long, Integer> getReplyCounts(List<Long> prntIdx);

	// 개별 게시물의 좋아요 수를 반환하는 메서드
	public long getLikeCount(Long bidx);
	
	// 모든 게시물의 좋아요 수를 조회하는 메서드
public Map<Long, Long> getAllBoardLikeCounts(List<Long> prntIdx);

	 //좋아요 상태 확인
	public String isPostLikedByUser(String email, Long boardIdx);

	//좋아요 up
	public boolean likeUp(String email, Long postIdx);
	
	//좋아요 down
	public boolean likeDown(String email, Long postIdx);

	//신고하기
	public String reportPost(String email, Long boardIdx);

	//공지사항 화면
	public List<BoardDTO> getAllNotices();

	//내가 작성한 게시글(마이 페이지)
	public Page<BoardDTO> mypageBoardList(String email, int pageNum, int postNum);

	//내가 작성한 댓글(마이 페이지)
	Page<ReplyDTO> mypageReplyList(String email, int pageNum, int postNum);
	
}