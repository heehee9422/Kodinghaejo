package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestBookmarkDTO;
import com.kodinghaejo.dto.TestSubmitDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.BoardRecommendEntity;
import com.kodinghaejo.entity.ChatMemberEntity;
import com.kodinghaejo.entity.FileEntity;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.MemberLogEntity;
import com.kodinghaejo.entity.NotificationEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestBookmarkEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;
import com.kodinghaejo.entity.TestQuestionEntity;
import com.kodinghaejo.entity.TestSubmitEntity;
import com.kodinghaejo.entity.repository.BoardRecommendRepository;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.ChatMemberRepository;
import com.kodinghaejo.entity.repository.CommonCodeRepository;
import com.kodinghaejo.entity.repository.FileRepository;
import com.kodinghaejo.entity.repository.MemberLogRepository;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.NotificationRepository;
import com.kodinghaejo.entity.repository.ReplyRepository;
import com.kodinghaejo.entity.repository.TestBookmarkRepository;
import com.kodinghaejo.entity.repository.TestQuestionAnswerRepository;
import com.kodinghaejo.entity.repository.TestQuestionRepository;
import com.kodinghaejo.entity.repository.TestSubmitRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final MemberLogRepository memberLogRepository;
	private final BoardRepository boardRepository;
	private final BoardRecommendRepository boardRecommendRepository;
	private final ChatMemberRepository chatMemberRepository;
	private final FileRepository fileRepository;
	private final NotificationRepository notificationRepository;
	private final ReplyRepository replyRepository;
	private final TestBookmarkRepository testBookmarkRepository;
	private final TestQuestionAnswerRepository testQuestionAnswerRepository;
	private final TestQuestionRepository testQuestionRepository;
	private final TestSubmitRepository testSubmitRepository;
	private final CommonCodeRepository commonCodeRepository;

	//회원가입
	@Override
	public void join(MemberDTO member) {
		MemberEntity memberEntity = MemberEntity
																.builder()
																.email(member.getEmail())
																.emailAuth("N")
																.username(member.getUsername())
																.nickname(member.getUsername())
																.password(member.getPassword())
																.tel(member.getTel())
																.lvl("LVL-0002")
																.score(0L)
																.imgSize(0L)
																.regdate(LocalDateTime.now())
																.pwdate(LocalDateTime.now())
																.notifdate(LocalDateTime.now().plusDays(30))
																.scoredate(LocalDateTime.now())
																.joinRoute("email")
																.isUse("Y")
																.build();

		memberRepository.save(memberEntity);
	}

	//기본정보 수정
	@Override
	public void modifyMemberInfo(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.modifyInfo(member);
		memberRepository.save(memberEntity);
	}

	//주요 기술 변경(마이 페이지)
	@Override
	public void modifyTec(String email, String tec1, String tec2, String tec3) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		memberEntity.setTec1(tec1);
		memberEntity.setTec2(tec2);
		memberEntity.setTec3(tec3);
		memberRepository.save(memberEntity);
	}

	//희망 직무 변경(마이 페이지)
	@Override
	public void modifyJob(String email, String job1, String job2, String job3) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		memberEntity.setJob1(job1);
		memberEntity.setJob2(job2);
		memberEntity.setJob3(job3);
		memberRepository.save(memberEntity);
	}

	//비밀번호 변경
	//비밀번호 찾기(임시비밀번호 발급)
	@Override
	public void modifyPassword(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setPassword(member.getPassword());
		if (!memberEntity.getJoinRoute().equals("email")) member.setJoinRoute("email");
		memberRepository.save(memberEntity);
	}

	//이메일 중복 확인
	@Override
	public int checkEmail(String email) {
		Optional<MemberEntity> memberEntity = memberRepository.findById(email);

		if (memberEntity.isEmpty()) return 0;
		else if (memberEntity.get().getIsUse().equals("N")) return -1;
		else return 1;
	}

	//회원 기본정보
	@Override
	public MemberDTO memberInfo(String email) {
		return memberRepository.findById(email).map((member) -> new MemberDTO(member)).get();
	}

	//회원 기본정보
	@Override
	public MemberDTO memberInfoByIsUse(String email) {
		return memberRepository.findByEmailAndIsUse(email, "Y").map((member) -> new MemberDTO(member)).get();
	}

	//회원 로그인, 로그아웃, 패스워드변경 일자 등록(Update)
	@Override
	public void lastdateUpdate(String email, String status) {
		MemberEntity memberEntity = memberRepository.findById(email).get();

		switch (status) {
			case "login":
				memberEntity.setLogindate(LocalDateTime.now());
				break;
			case "logout":
				memberEntity.setLogoutdate(LocalDateTime.now());
				break;
			case "password":
				memberEntity.setPwdate(LocalDateTime.now());
				break;
		}

		memberRepository.save(memberEntity);
	}

	//회원 로그 등록
	@Override
	public void memberLogRegistry(String email, String status) {
		MemberEntity memberEntity = memberRepository.findById(email).get();

		MemberLogEntity memberLogEntity = MemberLogEntity
											.builder()
											.email(memberEntity)
											.regdate(LocalDateTime.now())
											.status(status)
											.build();

		memberLogRepository.save(memberLogEntity);
	}

	//회원 아이디(이메일) 찾기
	@Override
	public List<String> findId(MemberDTO member) {
		List<String> emailList = new ArrayList<>();
		memberRepository.findByUsernameAndTelAndIsUse(member.getUsername(), member.getTel(), "Y")
										.stream().forEach((m) -> emailList.add(m.getEmail()));

		return emailList;
	}

	//비밀번호 변경 알림 연기(30일)
	@Override
	public void modifyPasswordAfter30(String email) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		memberEntity.setNotifdate(LocalDateTime.now().plusDays(30));
		memberRepository.save(memberEntity);
	}

	//회원 첨부파일 목록
	@Override
	public List<FileEntity> getMemberFileList(String email) {
		return fileRepository.findByEmail(email);
	}

	//본인이 방장인 채팅방의 갯수 확인
	@Override
	public Long countChatManager(String email) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		return chatMemberRepository.countByEmailAndManager(memberEntity, "Y");
	}

	//계정 삭제(사용여부만 Y --> N 변경, 실제로 Delete 처리는 X)
	@Override
	public void deleteAccount(String email) {
		//회원정보
		MemberEntity memberEntity = memberRepository.findById(email).get();
		memberEntity.setScore(0L);
		memberEntity.setScoredate(LocalDateTime.now());
		memberEntity.setIsUse("N");
		memberRepository.save(memberEntity);

		//게시물
		List<BoardEntity> boardEntities = boardRepository.findByEmailAndIsUse(memberEntity, "Y");
		for (BoardEntity boardEntity : boardEntities) {
			boardEntity.setIsUse("D");
			boardRepository.save(boardEntity);
		}

		//게시물 추천/신고
		List<BoardRecommendEntity> boardRecommendEntities = boardRecommendRepository.findByEmailAndIsUse(memberEntity, "Y");
		for (BoardRecommendEntity boardRecommendEntity : boardRecommendEntities) {
			boardRecommendEntity.setIsUse("D");
			boardRecommendRepository.save(boardRecommendEntity);
		}

		//채팅방 참여 멤버
		List<ChatMemberEntity> chatMemberEntities = chatMemberRepository.findByEmail(memberEntity);
		for (ChatMemberEntity chatMemberEntity : chatMemberEntities) {
			chatMemberRepository.delete(chatMemberEntity);
		}

		//업로드한 첨부 파일
		List<FileEntity> fileEntities = fileRepository.findByEmailAndIsUse(email, "Y");
		for (FileEntity fileEntity : fileEntities) {
			fileEntity.setIsUse("D");
			fileRepository.save(fileEntity);
		}

		//알림
		List<NotificationEntity> notificationEntities = notificationRepository.findByEmailAndIsUse(memberEntity, "Y");
		for (NotificationEntity notificationEntity : notificationEntities) {
			notificationEntity.setIsUse("D");
			notificationRepository.save(notificationEntity);
		}

		//댓글
		List<ReplyEntity> replyEntities = replyRepository.findByEmailAndIsUse(memberEntity, "Y");
		for (ReplyEntity replyEntity : replyEntities) {
			replyEntity.setIsUse("D");
			replyRepository.save(replyEntity);
		}

		//문제 북마크(즐겨찾기)
		List<TestBookmarkEntity> testBookmarkEntities = testBookmarkRepository.findByEmailAndIsUse(memberEntity, "Y");
		for (TestBookmarkEntity testBookmarkEntity : testBookmarkEntities) {
			testBookmarkEntity.setIsUse("D");
			testBookmarkRepository.save(testBookmarkEntity);
		}

		//문제 질문 답변
		List<TestQuestionAnswerEntity> testQuestionAnswerEntities = testQuestionAnswerRepository.findByEmailAndIsUse(memberEntity, "Y");
		for (TestQuestionAnswerEntity testQuestionAnswerEntity : testQuestionAnswerEntities) {
			testQuestionAnswerEntity.setIsUse("D");
			testQuestionAnswerRepository.save(testQuestionAnswerEntity);
		}

		//문제 질문
		List<TestQuestionEntity> testQuestionEntities = testQuestionRepository.findByEmailAndIsUse(memberEntity, "Y");
		for (TestQuestionEntity testQuestionEntity : testQuestionEntities) {
			testQuestionEntity.setIsUse("D");
			testQuestionRepository.save(testQuestionEntity);
		}

		//문제 제출
		List<TestSubmitEntity> testSubmitEntities = testSubmitRepository.findByEmail(memberEntity);
		for (TestSubmitEntity testSubmitEntity : testSubmitEntities) {
			testSubmitRepository.delete(testSubmitEntity);
		}

	}

	//계정 복구(사용여부 N --> Y 변경)
	@Override
	public void restoreAccount(String email) {
		//회원정보
		MemberEntity memberEntity = memberRepository.findById(email).get();
		memberEntity.setIsUse("Y");
		memberRepository.save(memberEntity);

		//게시물
		List<BoardEntity> boardEntities = boardRepository.findByEmailAndIsUse(memberEntity, "D");
		for (BoardEntity boardEntity : boardEntities) {
			boardEntity.setIsUse("Y");
			boardRepository.save(boardEntity);
		}

		//게시물 추천/신고
		List<BoardRecommendEntity> boardRecommendEntities = boardRecommendRepository.findByEmailAndIsUse(memberEntity, "D");
		for (BoardRecommendEntity boardRecommendEntity : boardRecommendEntities) {
			boardRecommendEntity.setIsUse("Y");
			boardRecommendRepository.save(boardRecommendEntity);
		}

		//업로드한 첨부 파일
		List<FileEntity> fileEntities = fileRepository.findByEmailAndIsUse(email, "D");
		for (FileEntity fileEntity : fileEntities) {
			fileEntity.setIsUse("Y");
			fileRepository.save(fileEntity);
		}

		//알림
		List<NotificationEntity> notificationEntities = notificationRepository.findByEmailAndIsUse(memberEntity, "D");
		for (NotificationEntity notificationEntity : notificationEntities) {
			notificationEntity.setIsUse("Y");
			notificationRepository.save(notificationEntity);
		}

		//댓글
		List<ReplyEntity> replyEntities = replyRepository.findByEmailAndIsUse(memberEntity, "D");
		for (ReplyEntity replyEntity : replyEntities) {
			replyEntity.setIsUse("Y");
			replyRepository.save(replyEntity);
		}

		//문제 북마크(즐겨찾기)
		List<TestBookmarkEntity> testBookmarkEntities = testBookmarkRepository.findByEmailAndIsUse(memberEntity, "D");
		for (TestBookmarkEntity testBookmarkEntity : testBookmarkEntities) {
			testBookmarkEntity.setIsUse("Y");
			testBookmarkRepository.save(testBookmarkEntity);
		}

		//문제 질문 답변
		List<TestQuestionAnswerEntity> testQuestionAnswerEntities = testQuestionAnswerRepository.findByEmailAndIsUse(memberEntity, "D");
		for (TestQuestionAnswerEntity testQuestionAnswerEntity : testQuestionAnswerEntities) {
			testQuestionAnswerEntity.setIsUse("Y");
			testQuestionAnswerRepository.save(testQuestionAnswerEntity);
		}

		//문제 질문
		List<TestQuestionEntity> testQuestionEntities = testQuestionRepository.findByEmailAndIsUse(memberEntity, "D");
		for (TestQuestionEntity testQuestionEntity : testQuestionEntities) {
			testQuestionEntity.setIsUse("Y");
			testQuestionRepository.save(testQuestionEntity);
		}

	}

	//내가 작성한 게시글
	@Override
	public Page<BoardDTO> mypageBoardList(String email, int pageNum, int postNum) {
		MemberEntity memberEntity = memberRepository.findById(email).get();

		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		Page<BoardEntity> boardEntities = boardRepository.findByEmailAndIsUse(memberEntity, "Y", pageRequest);
		List<BoardDTO> boardDTOs = new ArrayList<>();

		for (BoardEntity boardEntity : boardEntities) {
			BoardDTO board = new BoardDTO(boardEntity);
			board.setGoodCnt(boardRecommendRepository.countByBoardIdxAndGoodChk(boardEntity, "Y"));
			board.setCatName(commonCodeRepository.findById(boardEntity.getCat()).get().getVal());
			boardDTOs.add(board);
		}

		return new PageImpl<>(boardDTOs, pageRequest, boardEntities.getTotalElements());
	}

	//내가 작성한 댓글
	@Override
	public Page<ReplyDTO> mypageReplyList(String email, int pageNum, int postNum) {
		MemberEntity memberEntity = memberRepository.findById(email).get();

		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		Page<ReplyEntity> replyEntities = replyRepository.findByEmailAndIsUse(memberEntity, "Y", pageRequest);
		List<ReplyDTO> replyDTOs = new ArrayList<>();

		for (ReplyEntity replyEntity : replyEntities) {
			ReplyDTO reply = new ReplyDTO(replyEntity);
			String prntTitle = (replyEntity.getRePrnt().equals("QA")) ? testQuestionAnswerRepository.findById(replyEntity.getPrntIdx()).get().getContent() :
													(replyEntity.getRePrnt().equals("Q")) ? testQuestionRepository.findById(replyEntity.getPrntIdx()).get().getTitle() :
													(replyEntity.getRePrnt().equals("FR")) ? boardRepository.findById(replyEntity.getPrntIdx()).get().getTitle() : "";
			reply.setPrntTitle(prntTitle);
			reply.setAqIdx((replyEntity.getRePrnt().equals("QA")) ? testQuestionAnswerRepository.findById(replyEntity.getPrntIdx()).get().getTqIdx().getIdx() : 0);
			replyDTOs.add(reply);
		}

		return new PageImpl<>(replyDTOs, pageRequest, replyEntities.getTotalElements());
	}

	//마이페이지 나의 랭킹
	@Override
	public MemberDTO memberTest(String email) {
		MemberEntity memberEntity = memberRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("email not found"));

		MemberDTO memberDTO = new MemberDTO(memberEntity);

//		Long correctCount = testSubmitRepository.countSubmitByEmail(email);
		Long correctCount = testSubmitRepository.countByEmailAndSubmSts(memberEntity, "Y");
		memberDTO.setCorrectCount(correctCount != null ? correctCount : 0);
		Long submitCount = testSubmitRepository.countByEmail(memberEntity);
		memberDTO.setSubmitCount(submitCount);
		double correctRate = (submitCount > 0) ? (correctCount * 100.0) / submitCount : 0;
		memberDTO.setCorrectRate(correctRate);

		return memberDTO;

	}

	//모든회원
	@Override
	public List<MemberDTO> getAllMember() {
		List<MemberEntity> memberEntities = memberRepository.findAll();

		List<MemberDTO> memberDTOs = new ArrayList<>();

		for (MemberEntity member : memberEntities) {
			MemberDTO memberDTO = memberTest(member.getEmail());
			memberDTOs.add(memberDTO);
		}

		return memberDTOs;
	}

//회원의 풀어본 문제
	@Override
	public Page<TestSubmitDTO> myTest(int pageNum, int postNum, String email) {
		MemberEntity memberEntity = memberRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("email not found"));

		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));

		List<TestSubmitEntity> submitEntities = testSubmitRepository.findByEmail(memberEntity);

		List<TestSubmitDTO> testSubmitDTOs = new ArrayList<>();

		for (TestSubmitEntity submit : submitEntities) {
			TestSubmitDTO testSubmitDTO = new TestSubmitDTO(submit);
			testSubmitDTOs.add(testSubmitDTO);
		}

		int startPoint = (int) pageRequest.getOffset();
		int endPoint = (startPoint + pageRequest.getPageSize()) > testSubmitDTOs.size() ? testSubmitDTOs.size() : (startPoint + pageRequest.getPageSize());

		return new PageImpl<>(testSubmitDTOs.subList(startPoint, endPoint), pageRequest, testSubmitDTOs.size());
	}

	//회원의 북마크 문제
	@Override
	public Page<TestBookmarkDTO> myBookmark(int pageNum, int postNum, String email) {
		MemberEntity memberEntity = memberRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("email not found"));

		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));

		List<TestBookmarkEntity> bookmarkEntities = testBookmarkRepository.findByEmailAndIsUse(memberEntity, "Y");

		List<TestBookmarkDTO> testBookmarkDTOs = new ArrayList<>();

		for (TestBookmarkEntity bookmarkEntity : bookmarkEntities) {
			TestBookmarkDTO testBookmarkDTO = new TestBookmarkDTO(bookmarkEntity);
			testBookmarkDTOs.add(testBookmarkDTO);
		}

		int startPoint = (int) pageRequest.getOffset();
		int endPoint = (startPoint + pageRequest.getPageSize()) > testBookmarkDTOs.size() ? testBookmarkDTOs.size() : (startPoint + pageRequest.getPageSize());

		return new PageImpl<>(testBookmarkDTOs.subList(startPoint, endPoint), pageRequest, testBookmarkDTOs.size());
	}

	//메일 인증 완료처리
	@Override
	public void updateEmailAuth(String email) {
		MemberEntity member = memberRepository.findById(email).get();
		member.setEmailAuth("Y");
		memberRepository.save(member);
	}

}