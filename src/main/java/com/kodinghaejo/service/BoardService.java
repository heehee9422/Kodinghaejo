package com.kodinghaejo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.dto.TestQuestionAnswerDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;
import com.kodinghaejo.entity.TestQuestionEntity;

public interface BoardService {

	//게시물 등록 하기
	public Long write(String cat, String email, String writer, String title, String content) throws Exception;

	//게시물 목록 보기
	public Page<BoardDTO> getBoardList(int pageNum, int postNum, String email);

	//게시물 상세 내용 보기
	public BoardDTO view(Long idx, String email) throws Exception;

	//게시물 수정
	public void modify(BoardDTO board) throws Exception;

	//게시물 비활성화 (isUse y->n)
	public void deactivePost(BoardDTO board) throws Exception;

	public void deleteBoard(Long idx) throws Exception;

	//댓글 목록보기
	public Page<ReplyEntity> viewReply(int pageNum, int postNum, Long boardIdx);

	//댓글등록
	public ReplyEntity writeReply(ReplyInterface reply) throws Exception;

	//댓글 수정
	public void modifyReply(ReplyInterface reply) throws Exception;

	//댓글 삭제 (isUse를 "N"으로 설정)
	public void deleteReply(ReplyInterface reply);

	//게시물 조회수 증가
	public void hitno(Long idx) throws Exception;

	//게시물 추천 처리
	public void recommend(Long boardIdx, String email, String kind);

	//신고하기
	public String reportPost(String email, Long boardIdx);

	//공지사항 화면
	public Page<BoardDTO> getAllNotices(int pageNum, int postNum);

	//=========질문게시판==========
	//질문 등록
	public Long questionWrite(Long tlIdx, String email, String writer, String title, String content) throws Exception;
	
	//질문 가져오기(NEW)
	public Page<TestQuestionDTO> getQuestionList(int pageNum, int postNum, String kind, String keyword, String email);
	
	//질문 상세보기
	public TestQuestionDTO getQuestionInfo(Long idx);

	//모든 질문 가져오기
	public List<TestQuestionEntity> getAllQuestionWithTestInfo();

	//특정문제의 정보 가져오기
	public TestEntity getTestByIdx(Long testIdx);

	//특정문제의 질문 가져오기
	public List<TestQuestionEntity> getQuestionsByTestIdx(Long testIdx);

	//모든 질문 개수 가져오기
	public long getQuestionCount();

	//특정 문제의 질문 개수 가져오기
	public long getQuestionCountByTestIdx(Long testIdx);

	//로그인된 사용자의 질문 가져오기
	public List<TestQuestionEntity> getMyQuestions(String email);

	//이메일로 질문의 갯수
	public long getMyQuestionCount(String email);

	//이메일로 질문의 문제 정보 가져오기
	public List<TestEntity> getTestsByMyQuestions(String email);

	//질문 상세보기
	public TestQuestionDTO questionview(Long questionIdx) throws Exception;

	//questionIdx를 통해 문제 정보 조회
	public TestEntity getTestByQuestionIdx(Long questionIdx);

	//질문 수정
	public void questionModify(TestQuestionDTO question) throws Exception;

	//질문 비활성화 (isUse Y->N)
	public void questionDelete(Long idx) throws Exception;

	//답변쓰기
	public TestQuestionAnswerEntity answerWrite(Long tqIdx, String email, String writer, String content) throws Exception;

	//답변 리스트 보기
	public List<TestQuestionAnswerDTO> answerlist(Long questionIdx);

	//답변 개수보기
	public int getAnswerCountByQuestionId(Long questionIdx);

	//답변 삭제
	public void answerDelete(Long idx);

	//답변 수정
	public void answerModify(Long idx, String content);

	//답변없는 질문 가져오기
	public List<TestQuestionEntity> getQuestionsWithNoAnswers();
	
	//답변없는 질문 갯수 가져오기
	public long getQuestionWithNoAnswersCount();
	
	// 문제 제목 또는 질문 제목으로 검색
	public List<TestQuestionEntity> searchQuestions(String keyword);

}