package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.dto.TestQuestionAnswerDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.BoardRecommendEntity;
import com.kodinghaejo.entity.BoardRecommendEntityID;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;
import com.kodinghaejo.entity.TestQuestionEntity;
import com.kodinghaejo.entity.repository.BoardRecommendRepository;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.CommonCodeRepository;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.ReplyRepository;
import com.kodinghaejo.entity.repository.TestLngRepository;
import com.kodinghaejo.entity.repository.TestQuestionAnswerRepository;
import com.kodinghaejo.entity.repository.TestQuestionRepository;
import com.kodinghaejo.entity.repository.TestRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final BoardRecommendRepository boardRecommendRepository;
	private final MemberRepository memberRepository;
	private final ReplyRepository replyRepository;
	private final CommonCodeRepository commonCodeRepository;
	private final TestRepository testRepository;
	private final TestLngRepository testLngRepository;
	private final TestQuestionRepository testQuestionRepository;
	private final TestQuestionAnswerRepository testQuestionAnswerRepository;

	//게시물 등록하기
	@Override
	public Long write(String cat, String email, String writer, String title, String content) throws Exception {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		BoardEntity boardEntity = BoardEntity
																.builder()
																.cat(cat)
																.email(memberEntity)
																.writer(writer)
																.title(title)
																.content(content)
																.hitCnt(0)
																.regdate(LocalDateTime.now())
																.isUse("Y")
																.build();
		boardRepository.save(boardEntity);
		
		return boardEntity.getIdx();
	}

	//게시물 리스트 보기
	@Override
	public Page<BoardDTO> getBoardList(int pageNum, int postNum, String email) {
		MemberEntity memberEntity = (email == null || email.equals("")) ? null : memberRepository.findById(email).get();

		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		List<BoardEntity> boardEntities = boardRepository.findByCatNotAndIsUseOrderByIdxDesc("CAT-0001", "Y");

		if (memberEntity != null) {
			for (int i = 0; i < boardEntities.size(); i++) {
				if (boardRecommendRepository.countByEmailAndBoardIdxAndBadChkAndIsUse(memberEntity, boardEntities.get(i), "Y", "Y") > 0)
					boardEntities.remove(i);
			}
		}
		List<BoardDTO> boardDTOs = new ArrayList<>();

		for (BoardEntity boardEntity : boardEntities) {
			BoardDTO boardDTO = new BoardDTO(boardEntity);
			boardDTO.setReplyCnt(replyRepository.countByRePrntAndPrntIdxAndIsUse("FR", boardEntity.getIdx(), "Y"));
			boardDTO.setGoodChk((memberEntity == null) ? "N" : (boardRecommendRepository.countByEmailAndBoardIdxAndGoodChkAndIsUse(memberEntity, boardEntity, "Y", "Y") == 0) ? "N" : "Y");
			boardDTO.setGoodCnt(boardRecommendRepository.countByBoardIdxAndGoodChkAndIsUse(boardEntity, "Y", "Y"));
			boardDTO.setCatName(commonCodeRepository.findById(boardEntity.getCat()).get().getVal());
			boardDTOs.add(boardDTO);
		}

		int startPoint = (int) pageRequest.getOffset();
		int endPoint = (startPoint + pageRequest.getPageSize()) > boardDTOs.size() ? boardDTOs.size() : (startPoint + pageRequest.getPageSize());

		return new PageImpl<>(boardDTOs.subList(startPoint, endPoint), pageRequest, boardDTOs.size());
	}

	//게시물 내용 보기
	@Override
	public BoardDTO view(Long idx, String email) throws Exception {
		BoardEntity boardEntity = boardRepository.findById(idx).get();
		MemberEntity memberEntity = (email == null || email.equals("")) ? null : memberRepository.findById(email).get();

		BoardDTO boardDTO = new BoardDTO(boardEntity);
		boardDTO.setReplyCnt(replyRepository.countByRePrntAndPrntIdxAndIsUse("FR", boardEntity.getIdx(), "Y"));
		boardDTO.setGoodChk((memberEntity == null) ? "N" : (boardRecommendRepository.countByEmailAndBoardIdxAndGoodChkAndIsUse(memberEntity, boardEntity, "Y", "Y") == 0) ? "N" : "Y");
		boardDTO.setGoodCnt(boardRecommendRepository.countByBoardIdxAndGoodChkAndIsUse(boardEntity, "Y", "Y"));
		boardDTO.setBadChk((memberEntity == null) ? "N" : (boardRecommendRepository.countByEmailAndBoardIdxAndBadChkAndIsUse(memberEntity, boardEntity, "Y", "Y") == 0) ? "N" : "Y");
		boardDTO.setCatName(commonCodeRepository.findById(boardEntity.getCat()).get().getVal());

		return boardDTO;
	}

	//게시물 수정하기
	@Override
	public void modify(BoardDTO board) throws Exception {
		BoardEntity boardEntity = boardRepository.findById(board.getIdx()).get();
		boardEntity.setTitle(board.getTitle());
		boardEntity.setContent(board.getContent());
		boardEntity.setCat(board.getCat());
		boardRepository.save(boardEntity);
	}

	//게시물 비활성화 (isUse Y -> N)
	@Override
	public void deactivePost(BoardDTO board) throws Exception {
		BoardEntity boardEntity = boardRepository.findById(board.getIdx()).get();
		boardEntity.setIsUse("N"); //isUse 값을 N으로 설정하여 비활성화
		boardRepository.save(boardEntity);
	}

	@Override
	public void deleteBoard(Long idx) throws Exception {
		BoardEntity boardEntity = boardRepository.findById(idx).get();
		boardEntity.setIsUse("N"); //isUse 값을 N으로 설정하여 비활성화
		boardRepository.save(boardEntity);
	}

	//댓글 목록보기
	@Override
	public Page<ReplyEntity> viewReply(int pageNum, int postNum, Long boardIdx) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		return replyRepository.findByRePrntAndPrntIdxAndIsUse("FR", boardIdx, "Y", pageRequest);
	}

	//댓글 등록
	@Override
	public ReplyEntity writeReply(ReplyInterface reply) throws Exception {
		MemberEntity memberEntity = memberRepository.findById(reply.getEmail()).get();

		ReplyEntity replyEntity = ReplyEntity
																.builder()
																.rePrnt(reply.getRePrnt())
																.prntIdx(reply.getPrntIdx())
																.email(memberEntity)
																.writer(reply.getWriter())
																.content(reply.getContent())
																.regdate(LocalDateTime.now())
																.isUse("Y")
																.build();
		replyRepository.save(replyEntity);

		return replyEntity;
	}

	//댓글 수정
	@Override
	public void modifyReply(ReplyInterface reply) throws Exception {
		ReplyEntity replyEntity = replyRepository.findById(reply.getIdx()).get();
		replyEntity.setContent(reply.getContent());
		replyRepository.save(replyEntity);
	}

	//댓글 삭제 (isUse를 "N"으로 설정)
	@Override
	public void deleteReply(ReplyInterface reply) {
		ReplyEntity replyEntity = replyRepository.findById(reply.getIdx()).get();
		replyEntity.setIsUse("N");
		replyRepository.save(replyEntity);
	}

	//게시물 내용 조회수 증가
	@Override
	public void hitno(Long seqno) throws Exception {
		boardRepository.hitno(seqno);
	}

	//게시물 추천 처리
	@Override
	public void recommend(Long boardIdx, String email, String kind) {
		BoardRecommendEntity boardRecommendEntity;

		if (boardRecommendRepository.findById(new BoardRecommendEntityID(email, boardIdx)).isEmpty()) {
			boardRecommendEntity = BoardRecommendEntity
															.builder()
															.email(memberRepository.findById(email).get())
															.boardIdx(boardRepository.findById(boardIdx).get())
															.goodChk(kind)
															.goodDate(LocalDateTime.now())
															.isUse("Y")
															.build();
		} else {
			boardRecommendEntity = boardRecommendRepository.findById(new BoardRecommendEntityID(email, boardIdx)).get();
			boardRecommendEntity.setGoodChk(kind);
			boardRecommendEntity.setGoodDate(LocalDateTime.now());
		}

		boardRecommendRepository.save(boardRecommendEntity);
	}

	//신고하기
	@Transactional
	@Override
	public String reportPost(String email, Long boardIdx) {
		BoardRecommendEntity boardRecommendEntity = boardRecommendRepository.findById(new BoardRecommendEntityID(email, boardIdx)).orElse(null);

		if (boardRecommendEntity == null) {
			//새로운 신고 등록
			boardRecommendEntity = BoardRecommendEntity
															.builder()
															.email(memberRepository.findById(email).get())
															.boardIdx(boardRepository.findById(boardIdx).get())
															.badChk("Y")
															.badDate(LocalDateTime.now())
															.isUse("Y")
															.build();
			boardRecommendRepository.save(boardRecommendEntity);

			return "{ \"message\": \"good\" }";
		} else if (boardRecommendEntity.getBadChk() == null || !boardRecommendEntity.getBadChk().equals("Y")) {
			boardRecommendEntity.setGoodChk("N");
			boardRecommendEntity.setBadChk("Y");
			boardRecommendEntity.setBadDate(LocalDateTime.now());
			boardRecommendRepository.save(boardRecommendEntity);

			return "{ \"message\": \"good\" }";
		} else {
			//이미 신고된 경우
			return "{ \"message\": \"ALREADY_REPORTED\" }";
		}
	}

	//공지사항 화면
	@Override
	public Page<BoardDTO> getAllNotices(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		Page<BoardEntity> boardEntities = boardRepository.findByCat("CAT-0001", pageRequest);
		List<BoardDTO> boardDTOs = new ArrayList<>();
		for (BoardEntity boardEntity : boardEntities) {
			BoardDTO boardDTO = new BoardDTO(boardEntity);
			boardDTO.setIsNew(LocalDateTime.now().isBefore(boardEntity.getRegdate().plusDays(2)) ? "Y" : "N");
			boardDTOs.add(boardDTO);
		}
		return new PageImpl<>(boardDTOs, pageRequest, boardEntities.getTotalElements());
	}

	//==========질문게시판===============
	//질문쓰기
	@Override
	public Long questionWrite(Long tlIdx, String email, String writer, String title, String content) throws Exception {
		TestLngEntity testLngEntity = testLngRepository.findById(tlIdx).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		TestQuestionEntity testQuestionEntity = TestQuestionEntity
																							.builder()
																							.tlIdx(testLngEntity)
																							.email(memberEntity)
																							.writer(writer)
																							.title(title)
																							.content(content)
																							.regdate(LocalDateTime.now())
																							.isUse("Y")
																							.build();
		testQuestionRepository.save(testQuestionEntity);
		
		return testQuestionEntity.getIdx();
	}
	
	//질문 가져오기(NEW)
	@Override
	public Page<TestQuestionDTO> getQuestionList(int pageNum, int postNum, String kind, String keyword, String email) {
		MemberEntity memberEntity = (email == null || email.equals("")) ? null : memberRepository.findById(email).get();
		
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		List<TestQuestionEntity> testQuestionEntities;
		
		if (kind.equals("A")) { //모든 질문
			testQuestionEntities = testQuestionRepository.findByIsUseOrderByIdxDesc("Y")
																										.stream().filter((e) -> (e.getTlIdx().getTestIdx().getTitle().contains(keyword) || e.getTitle().contains(keyword) || e.getContent().contains(keyword))).toList();
		} else if (kind.equals("N")) { //답변 필요
			testQuestionEntities = testQuestionRepository.findByIsUseOrderByIdxDesc("Y")
																										.stream().filter((e) -> (testQuestionAnswerRepository.countByTqIdxAndIsUse(e, "Y") == 0) && ((e.getTlIdx().getTestIdx().getTitle().contains(keyword) || e.getTitle().contains(keyword) || e.getContent().contains(keyword)))).toList();
		} else if (kind.equals("M")) { //내 질문
			testQuestionEntities = testQuestionRepository.findByEmailAndTitleContainingAndIsUseOrderByIdxDesc(memberEntity, keyword, "Y");
		} else if (kind.chars().allMatch(Character :: isDigit)) { //언어별 문제 인덱스
			Long tlIdx = Long.valueOf(kind);
			TestLngEntity testLngEntity = testLngRepository.findById(tlIdx).get();
			testQuestionEntities = testQuestionRepository.findByTlIdxAndTitleContainingAndIsUseOrderByIdxDesc(testLngEntity, keyword, "Y");
		} else {
			return null;
		}
		
		List<TestQuestionDTO> testQuestionDTOs = new ArrayList<>();
		
		for (TestQuestionEntity testQuestionEntity : testQuestionEntities) {
			TestQuestionDTO testQuestionDTO = new TestQuestionDTO(testQuestionEntity);
			testQuestionDTO.setAnswerCount(testQuestionAnswerRepository.countByTqIdxAndIsUse(testQuestionEntity, "Y"));
			testQuestionDTO.setLngName(commonCodeRepository.findById(testQuestionEntity.getTlIdx().getLng()).get().getVal());
			testQuestionDTO.setIsNew(LocalDateTime.now().isBefore(testQuestionEntity.getRegdate().plusDays(2)) ? "Y" : "N");
			testQuestionDTOs.add(testQuestionDTO);
		}

		int startPoint = (int) pageRequest.getOffset();
		int endPoint = (startPoint + pageRequest.getPageSize()) > testQuestionDTOs.size() ? testQuestionDTOs.size() : (startPoint + pageRequest.getPageSize());

		return new PageImpl<>(testQuestionDTOs.subList(startPoint, endPoint), pageRequest, testQuestionDTOs.size());
	}
	
	//질문 상세보기
	@Override
	public TestQuestionDTO getQuestionInfo(Long idx) {
		TestQuestionEntity testQuestionEntity = testQuestionRepository.findById(idx).get(); //.findByIdxAndIsUse(idx, "Y").get();
		
		TestQuestionDTO testQuestionDTO = new TestQuestionDTO(testQuestionEntity);
		testQuestionDTO.setReply(replyRepository.findByRePrntAndPrntIdxAndIsUse("Q", testQuestionEntity.getIdx(), "Y"));
		
		List<TestQuestionAnswerEntity> testQuestionAnswerEntities = testQuestionAnswerRepository.findByTqIdxAndIsUse(testQuestionEntity, "Y");
		List<TestQuestionAnswerDTO> testQuestionAnswerDTOs = new ArrayList<>();
		for (TestQuestionAnswerEntity testQuestionAnswerEntity : testQuestionAnswerEntities) {
			TestQuestionAnswerDTO testQuestionAnswerDTO = new TestQuestionAnswerDTO(testQuestionAnswerEntity);
			testQuestionAnswerDTO.setReply(replyRepository.findByRePrntAndPrntIdxAndIsUse("QA", testQuestionAnswerEntity.getIdx(), "Y"));
			
			testQuestionAnswerDTOs.add(testQuestionAnswerDTO);
		}
		testQuestionDTO.setAnswer(testQuestionAnswerDTOs);
		
		return testQuestionDTO;
	}
	
	//모든 질문 가져오기
	@Override
	public List<TestQuestionEntity> getAllQuestionWithTestInfo() {
		return null;//testQuestionRepository.findAllWithTestInfo();
	}

	//특정문제 정보 가져오기
	public TestEntity getTestByIdx(Long testIdx) {
		return testRepository.findById(testIdx).orElseThrow(() -> new RuntimeException("해당 문제를 찾을 수 없습니다."));
	}

	//특정문제의 질문 가져오기
	public List<TestQuestionEntity> getQuestionsByTestIdx(Long testIdx) {
		return testQuestionRepository.findQuestionsByTestIdx(testIdx);
	}

	//모든 질문 개수 가져오기
	@Override
	public long getQuestionCount() {
		return testQuestionRepository.count();
	}

	//특정 문제의 질문 개수 가져오기
	@Override
	public long getQuestionCountByTestIdx(Long testIdx) {
		return testQuestionRepository.countByTestIdx(testIdx);
	}

	//이메일로 질문 가져오기
	@Override
	public List<TestQuestionEntity> getMyQuestions(String email) {
		return testQuestionRepository.findByEmail(email);
	}

	//이메일로 질문의 갯수
	@Override
	public long getMyQuestionCount(String email) {
		return testQuestionRepository.countByEmail(email);
	}

	//이메일로 질문의 문제 정보 가져오기
	@Override
	public List<TestEntity> getTestsByMyQuestions(String email) {
		return testRepository.findTestsByEmail(email);
	}

	//질문 상세보기
	@Override
	public TestQuestionDTO questionview(Long questionIdx) throws Exception {
		return testQuestionRepository.findById(questionIdx).map(question -> new TestQuestionDTO(question)).get();
	}

	//questionIdx를 통해 문제 정보 조회
	@Override
	public TestEntity getTestByQuestionIdx(Long questionIdx) {
		return testQuestionRepository.findTestByQuestionIdx(questionIdx)
				.orElseThrow(() -> new NoSuchElementException("해당 질문과 연관된 문제를 찾을 수 없습니다. ID: " + questionIdx));
	}

	//질문 수정
	@Override
	public void questionModify(TestQuestionDTO question) throws Exception {
		TestQuestionEntity questionEntity = testQuestionRepository.findById(question.getIdx()).get();
		questionEntity.setTitle(question.getTitle());
		questionEntity.setContent(question.getContent());
		testQuestionRepository.save(questionEntity);
	}

	//질문 비활성화
	@Override
	public void questionDelete(Long idx) throws Exception {
		TestQuestionEntity questionEntity = testQuestionRepository.findById(idx).get();
		questionEntity.setIsUse("N");
		testQuestionRepository.save(questionEntity);
	}

	//답변쓰기
	@Override
	public TestQuestionAnswerEntity answerWrite(Long tqIdx, String email, String writer, String content) throws Exception {
		TestQuestionEntity testQuestionEntity = testQuestionRepository.findById(tqIdx).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		TestQuestionAnswerEntity testQuestionAnswerEntity = TestQuestionAnswerEntity
																													.builder()
																													.tqIdx(testQuestionEntity)
																													.email(memberEntity)
																													.writer(writer)
																													.content(content)
																													.regdate(LocalDateTime.now())
																													.isUse("Y")
																													.build();
		testQuestionAnswerRepository.save(testQuestionAnswerEntity);
		
		return testQuestionAnswerEntity;
	}

	//답변목록보기
	public List<TestQuestionAnswerDTO> answerlist(Long questionIdx) {
		return testQuestionAnswerRepository.findByTqIdx(questionIdx).stream().map(TestQuestionAnswerDTO::new)
				.collect(Collectors.toList());
	}

	//답변 개수보기
	public int getAnswerCountByQuestionId(Long questionIdx) {
		return testQuestionAnswerRepository.countAnswerByTqIdx(questionIdx);
	}

	//답변 수정
	@Override
	public void answerModify(Long idx, String content) {
		TestQuestionAnswerEntity answerEntity = testQuestionAnswerRepository.findById(idx).get();
		answerEntity.setContent(content);
		testQuestionAnswerRepository.save(answerEntity);
	}

	//답변 삭제(isUse를 "N"으로 설정)
	public void answerDelete(Long idx) {
		TestQuestionAnswerEntity answerEntity = testQuestionAnswerRepository.findById(idx).get();
		answerEntity.setIsUse("N");
		testQuestionAnswerRepository.save(answerEntity);
	}

	//답변없는 질문 가져오기
	@Override
	public List<TestQuestionEntity> getQuestionsWithNoAnswers() {
		return testQuestionRepository.findQuestionsWithNoAnswers();
	}

	//답변없는 질문 갯수 가져오기
	@Override
	public long getQuestionWithNoAnswersCount() {
		return testQuestionRepository.countQuestionsWithNoAnswers();
	}

	//문제 제목 또는 질문 제목으로 검색
	@Override
	public List<TestQuestionEntity> searchQuestions(String keyword) {
		return testQuestionRepository.searchQuestionsByKeyword(keyword);
	}
}