package com.kodinghaejo.service;

import java.util.List;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ChatDTO;
import com.kodinghaejo.dto.ChatMemberDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestQuestionDTO;

public interface AdminService {

	//문제 작성
	void saveTestWrite(TestDTO testDTO);
	
	//문제 보여주기
	public List<TestDTO> testAllList();
	
	//문제 수정
	public void saveTestModify(TestDTO testDTO);
	
	//ID로 문제 데이터 조회
	public TestDTO getTestById(Long id);
	
	//회원정보 관리화면
	public List<MemberDTO> memberAllList();
	
	//자유게시판 관리화면
	public List<BoardDTO> freeboardList();
	
	//공지사항 관리화면
	public List<BoardDTO> noticeboardList();
	
	//공지사항 작성
	public void write(BoardDTO board);
	
	//ID로 공지사항 데이터 조회
	public BoardDTO getNoticeById(Long id);
	
	//공지사항 수정
	public void savenoticeModify(BoardDTO boardDTO);
	
	//질문게시판 관리화면
	public List<TestQuestionDTO> questionList();
	
	//댓글 관리화면
	public List<ReplyDTO> replyList();
	
	//채팅방 관리화면
	public List<ChatDTO> chatList();
	
	//참여인원 0인 채팅방 삭제
	public void deleteEmptyChats();
	
	//게시글 삭제(자유게시판,공지사항)
	public void deleteBoard(Long idx);
	
	//게시글 삭제(질문게시판)
	public void deleteQBoard(Long idx);
	
	//댓글 삭제
	public void deleteReply(Long idx);
	
	//문제 검색
	public List<TestDTO> searchtestListByTitle(String searchKeyword);
	
	//회원정보 검색
	public List<MemberDTO> searchMembers(String searchType, String searchKeyword);
	
	//자유게시판 검색
	public List<BoardDTO> searchFreeboardListByTitle(String searchKeyword);
	
	//공지사항 검색
	public List<BoardDTO> searchNoticeListByTitle(String searchKeyword);
	
	//질문게시판 검색
	public List<TestQuestionDTO> searchQboardListByTitle(String searchKeyword);
	
	//댓글 검색
	public List<ReplyDTO> searchReplyListByContent(String searchKeyword);
	
	//채팅방 검색
	public List<ChatDTO> searchChatListByTitle(String searchKeyword);

	public List<ChatMemberDTO> getChatMembers();
	
}