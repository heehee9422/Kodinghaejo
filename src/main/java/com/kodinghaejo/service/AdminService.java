package com.kodinghaejo.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.kodinghaejo.dto.BannerDTO;
import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.CommonCodeDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.BannerEntity;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.CommonCodeEntity;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestQuestionEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface AdminService {

	//문제 작성
	void saveTestWrite(TestDTO testDTO);

	//문제 보여주기
	public Page<TestDTO> testAllList(int pageNum, int postNum);

	//문제 수정
	public void saveTestModify(TestDTO testDTO);

	//ID로 문제 데이터 조회
	public TestDTO getTestById(Long id);

	//회원정보 관리화면
	public Page<MemberEntity> memberAllList(int pageNum, int postNum);

	//자유게시판 관리화면
	public Page<BoardDTO> freeboardList(int pageNum, int postNum);

	//공지사항 관리화면
	public Page<BoardEntity> noticeboardList(int pageNum, int postNum);

	//공지사항 작성
	public void write(BoardDTO board);

	//ID로 공지사항 데이터 조회
	public BoardDTO getNoticeById(Long id);

	//공지사항 수정
	public void savenoticeModify(BoardDTO boardDTO);

	//질문게시판 관리화면
	public Page<TestQuestionEntity> questionList(int pageNum, int postNum);

	//댓글 관리화면
	public Page<ReplyDTO> replyList(int pageNum, int postNum);

	//채팅방 관리화면
	public Page<ChatEntity> chatList(int pageNum, int postNum);

	//참여인원 0인 채팅방 삭제
	public void deleteEmptyChats();

	//게시글 삭제(자유게시판,공지사항)
	public void deleteBoard(Long idx);

	//게시글 삭제(질문게시판)
	public void deleteQBoard(Long idx);

	//댓글 삭제
	public void deleteReply(Long idx);

	//문제 검색
	public Page<TestDTO> searchtestListByTitle(int pageNum, int postNum, String searchKeyword);

	//회원정보 검색
	public Page<MemberEntity> searchMembers(int pageNum, int postNum, String searchType, String searchKeyword);

	//자유게시판 검색
	public Page<BoardDTO> searchFreeboardListByTitle(int pageNum, int postNum, String searchKeyword);

	//공지사항 검색
	public Page<BoardEntity> searchNoticeListByTitle(int pageNum, int postNum, String searchKeyword);

	//질문게시판 검색
	public Page<TestQuestionEntity> searchQboardListByTitle(int pageNum, int postNum, String searchKeyword);

	//댓글 검색
	public Page<ReplyDTO> searchReplyListByContent(int pageNum, int postNum, String searchKeyword);

	//채팅방 검색
	public Page<ChatEntity> searchChatListByTitle(int pageNum, int postNum, String searchKeyword);

	//일별가입자수 체크
	public long getTodaySignups();

	//일별 자유게시판 작성 수
	public long getTodayFreeBoardCount();

	//일별 방문자 수 체크
	public long getTodayVisitorCount(HttpServletRequest request);

	//일별 방문자 수 증가
	public void upTodayVisitorCount(HttpServletRequest request);

	//방문자 IP
	public String getUserIp(HttpServletRequest request);

	//일별 푼 문제 수
	public long getTodayTestCount();

	//월별 가입자수 체크
	public Map<Integer, Long> getMonthlySignups();

	//문제풀이에 사용된 언어
	public Map<String, Integer> getLngSubmitCount();

	//회원탈퇴
	public void deleteMember(String email);

	//공통코드 관리화면
	public Page<CommonCodeEntity> codeList(int pageNum, int postNum);

	//공통코드 검색
	public Page<CommonCodeEntity> searchCodeListByCode(int pageNum, int postNum, String searchKeyword);

	//공통코드 필터 타입
	public Page<CommonCodeEntity> getCodeListByType(int pageNum, int postNum, String type);

	//공통코드 추가
	public void codewrite(CommonCodeDTO code);

	//공통코드 삭제
	public boolean deleteCommonCode(String code);

	//회원 상세보기
	public MemberDTO getMemberDetailByEmail(String email);

	//댓글관리 필터 타입
	public Page<ReplyDTO> getReplyListByType(int pageNum, int postNum, String rePrnt);

	//배너 저장
	public void saveBanner(BannerEntity banner);

	//배너 목록 조회
	public Page<BannerEntity> getAllBanners(int pageNum, int postNum);

	//메인페이지 배너
	public List<BannerEntity> getBanner();

	//배너 isUse 상태 변경
	public void updateBannerIsUse(Long idx, String isUse);

	//배너 삭제
	public void deleteBanner(Long idx);

	//배너 종료일자에 따른 isUse 업데이트
	public void updateBannerEndDate();

	//배너 수정
	public void saveBannerModify(BannerDTO bannerDTO);

	//ID로 배너 데이터 조회
	public BannerEntity getBannerById(Long id);

}