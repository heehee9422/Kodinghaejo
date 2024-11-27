package com.kodinghaejo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.BannerDTO;
import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.CommonCodeDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestLngDTO;
import com.kodinghaejo.entity.BannerEntity;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.CommonCodeEntity;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;
import com.kodinghaejo.entity.TestQuestionEntity;
import com.kodinghaejo.entity.repository.BannerRepository;
import com.kodinghaejo.entity.repository.BoardRecommendRepository;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.ChatRepository;
import com.kodinghaejo.entity.repository.CommonCodeRepository;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.ReplyRepository;
import com.kodinghaejo.entity.repository.TestLngRepository;
import com.kodinghaejo.entity.repository.TestQuestionAnswerRepository;
import com.kodinghaejo.entity.repository.TestQuestionRepository;
import com.kodinghaejo.entity.repository.TestRepository;
import com.kodinghaejo.entity.repository.TestSubmitRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final TestRepository testRepository;
	private final TestLngRepository testLngRepository;
	private final BoardRepository boardRepository;
	private final TestQuestionRepository questionRepository;
	private final ReplyRepository replyRepository;
	private final ChatRepository chatRepository;
	private final MemberRepository memberRepository;
	private final BoardRecommendRepository boardRecommendRepository;
	private final TestQuestionAnswerRepository questionAnswerRepository;
	private final TestSubmitRepository submitRepository;
	private final CommonCodeRepository codeRepository;
	private final BannerRepository bannerRepository;

	//문제 작성
	@Override
	public void saveTestWrite(TestDTO testDTO) {
		TestEntity testEntity = testDTO.dtoToEntity(testDTO);
		testEntity.setRegdate(LocalDateTime.now());
		testRepository.save(testEntity);

		if (testDTO.getTestLngList() != null) {
			for (TestLngDTO langDTO : testDTO.getTestLngList()) {
				TestLngEntity langEntity = langDTO.dtoToEntity(langDTO);
				langEntity.setTestIdx(testEntity);
				testLngRepository.save(langEntity);
			}
		}
	}

	//문제 보여주기
	@Override
	public Page<TestDTO> testAllList(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		Page<TestEntity> testEntities = testRepository.findAll(pageRequest);
		List<TestDTO> testDTOs = new ArrayList<>();

		for (TestEntity test : testEntities) {
			TestDTO testDTO = new TestDTO(test);

			List<TestLngDTO> testLngDTOs = new ArrayList<>();
			testLngRepository.findByTestIdx(test).stream().forEach((e) -> testLngDTOs.add(new TestLngDTO(e)));

			testDTO.setTestLngList(testLngDTOs);

			long submitCount = submitRepository.countByTestIdx(test.getIdx());
			testDTO.setSubmitCount(submitCount);

			long correctCount = submitRepository.countByTestIdxAndSubmSts(test.getIdx(), "Y");
			testDTO.setCorrectCount(correctCount);

			double correctRate = (submitCount > 0) ? (correctCount * 100.0) / submitCount : 0;
			testDTO.setCorrectRate(correctRate);
			testDTOs.add(testDTO);
		}

		return new PageImpl<>(testDTOs, pageRequest, testEntities.getTotalElements());
	}

	//문제 수정
	@Override
	public void saveTestModify(TestDTO testDTO) {
		TestEntity testEntity = testRepository.findById(testDTO.getIdx()).get();

		testEntity.setDiff(testDTO.getDiff());
		testEntity.setTitle(testDTO.getTitle());
		testEntity.setDescr(testDTO.getDescr());

		testRepository.save(testEntity);

		for (TestLngDTO langDTO : testDTO.getTestLngList()) {
			TestLngEntity testLngEntity;

			if (testLngRepository.findByTestIdxAndLng(testEntity, langDTO.getLng()).isEmpty()) {
				testLngEntity = langDTO.dtoToEntity(langDTO);
				testLngEntity.setTestIdx(testEntity);
			} else {
				testLngEntity = testLngRepository.findByTestIdxAndLng(testEntity, langDTO.getLng()).get();

				//언어 정보 수정
				testLngEntity.setContent(langDTO.getContent());
				testLngEntity.setCorrect(langDTO.getCorrect());
				testLngEntity.setRunSrc(langDTO.getRunSrc());
				testLngEntity.setSubmSrc(langDTO.getSubmSrc());
			}
			//수정된 엔티티 저장
			testLngRepository.save(testLngEntity);
		}
	}

	//ID로 문제 데이터 조회
	@Override
	public TestDTO getTestById(Long id) {
		TestEntity testEntity = testRepository.findById(id).get();

		List<TestLngDTO> testLngList = new ArrayList<>();
		testLngRepository.findByTestIdx(testEntity).stream().forEach((e) -> testLngList.add(new TestLngDTO(e)));

		TestDTO testDTO = new TestDTO(testEntity);
		testDTO.setTestLngList(testLngList);

		//TestEntity를 TestDTO로 변환하여 반환
		return testDTO;
	}

	//회원정보 관리 화면
	@Override
	public Page<MemberEntity> memberAllList(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "regdate"));

		return memberRepository.findAll(pageRequest);
	}

	//자유게시판 관리 화면
	@Override
	public Page<BoardDTO> freeboardList(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		Page<BoardEntity> boardEntities = boardRepository.findByCatNot("CAT-0001", pageRequest);
		List<BoardDTO> boardDTOs = new ArrayList<>();

		for (BoardEntity board : boardEntities) {
			BoardDTO boardDTO = new BoardDTO(board);

			boardDTO.setGoodCnt(boardRecommendRepository.countByBoardIdxAndGoodChk(board, "Y"));
			boardDTO.setBadCnt(boardRecommendRepository.countByBoardIdxAndBadChk(board, "Y"));
			boardDTOs.add(boardDTO);
		}

		return new PageImpl<>(boardDTOs, pageRequest, boardEntities.getTotalElements());

	}

	//공지사항 관리 화면
	@Override
	public Page<BoardEntity> noticeboardList(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));

		return boardRepository.findByCat("CAT-0001", pageRequest);

	}

	//공지사항 작성
	@Override
	public void write(BoardDTO board) {
		board.setRegdate(LocalDateTime.now());
		board.setHitCnt(0);
		boardRepository.save(board.dtoToEntity(board));
	}

	//공지사항 수정
	@Override
	public void savenoticeModify(BoardDTO boardDTO) {
		BoardEntity boardEntity = boardRepository.findById(boardDTO.getIdx()).get();

		boardEntity.setCat(boardDTO.getCat());
		boardEntity.setTitle(boardDTO.getTitle());
		boardEntity.setContent(boardDTO.getContent());

		boardRepository.save(boardEntity);
	}

	//게시글 삭제(자유게시판,공지사항)
	@Override
	public void deleteBoard(Long idx) {
		BoardEntity boardEntity = boardRepository.findById(idx).get();
		boardRepository.delete(boardEntity);
	}

	//질문게시판 관리 화면
	@Override
	public Page<TestQuestionEntity> questionList(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));

		return questionRepository.findAll(pageRequest);

	}

	//질문 삭제
	@Override
	public void deleteQBoard(Long idx) {
		TestQuestionEntity questionEntity = questionRepository.findById(idx).get();
		questionRepository.delete(questionEntity);
	}

	//댓글 관리 화면
	@Override
	public Page<ReplyDTO> replyList(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		Page<ReplyEntity> replyEntities = replyRepository.findAll(pageRequest);
		List<ReplyDTO> replyDTOs = new ArrayList<>();

		for (ReplyEntity reply : replyEntities) {
			ReplyDTO replyDTO = new ReplyDTO(reply);
			switch (reply.getRePrnt()) {
				case "FR":
					BoardEntity board = boardRepository.findById(reply.getPrntIdx()).orElse(null);
					if (board != null) {
						replyDTO.setPrntTitle(boardRepository.findById(reply.getPrntIdx()).get().getTitle());
					} else {
						replyDTO.setPrntTitle("원글이 삭제됨");
					}
					break;
				case "Q":
					TestQuestionEntity question = questionRepository.findById(reply.getPrntIdx()).orElse(null);
					if (question != null) {
						replyDTO.setPrntTitle(questionRepository.findById(reply.getPrntIdx()).get().getTitle());
					} else {
						replyDTO.setPrntTitle("원글이 삭제됨");
					}
					break;
				case "QA":
					TestQuestionAnswerEntity answer = questionAnswerRepository.findById(reply.getPrntIdx()).orElse(null);
					if (answer != null) {
						replyDTO.setPrntTitle(questionAnswerRepository.findById(reply.getPrntIdx()).get().getContent());
					} else {
						replyDTO.setPrntTitle("원글이 삭제됨");
					}
					break;
			}
			replyDTOs.add(replyDTO);
		}
		return new PageImpl<>(replyDTOs, pageRequest, replyEntities.getTotalElements());
	}

	//채팅방 관리 화면
	@Override
	public Page<ChatEntity> chatList(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "regdate"));

		return chatRepository.findAll(pageRequest);
	}

	//참여인원 0인 채팅방 삭제
	@Override
	public void deleteEmptyChats() {
		List<ChatEntity> emptyChats = chatRepository.findChatsByLimit(0);

		for (ChatEntity chat : emptyChats) {
			chatRepository.deleteById(chat.getIdx());
		}
	}

	//댓글 삭제
	@Override
	public void deleteReply(Long idx) {
		ReplyEntity replyEntity = replyRepository.findById(idx).get();
		replyRepository.delete(replyEntity);
	}

	//문제 검색
	@Override
	public Page<TestDTO> searchtestListByTitle(int pageNum, int postNum, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "regdate"));

		Page<TestEntity> testEntities = testRepository.findByTitleContaining(searchKeyword, pageRequest);
		List<TestDTO> testDTOs = new ArrayList<>();

		for (TestEntity test : testEntities) {
			TestDTO testDTO = new TestDTO(test);
			List<TestLngDTO> testLngDTOs = new ArrayList<>();
			testLngRepository.findByTestIdx(test).stream().forEach((e) -> testLngDTOs.add(new TestLngDTO(e)));

			testDTO.setTestLngList(testLngDTOs);
			long submitCount = submitRepository.countByTestIdx(test.getIdx());
			testDTO.setSubmitCount(submitCount);

			long correctCount = submitRepository.countByTestIdxAndSubmSts(test.getIdx(), "Y");

			double correctRate = (submitCount > 0) ? (correctCount * 100.0) / submitCount : 0;
			testDTO.setCorrectRate(correctRate);
			testDTOs.add(testDTO);
		}

		return new PageImpl<>(testDTOs, pageRequest, testEntities.getTotalElements());
	}

	//회원정보 검색
	@Override
	public Page<MemberEntity> searchMembers(int pageNum, int postNum, String searchType, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "regdate"));

		//검색어가 없을 경우 전체 회원 목록 조회
		if (searchKeyword == null || searchKeyword.isEmpty()) {
			return memberRepository.findAll(pageRequest);
		} else {
			//검색어가 있을 경우 조건에 따라 검색
			switch (searchType) {
			case "email":
				return memberRepository.findByEmailContaining(searchKeyword, pageRequest);
			case "nickname":
				return memberRepository.findByNicknameContaining(searchKeyword, pageRequest);
			case "name":
				return memberRepository.findByUsernameContaining(searchKeyword, pageRequest);
			}
		}

		return null;
	}

	//ID로 공지사항 데이터 조회
	@Override
	public BoardDTO getNoticeById(Long id) {
		BoardEntity boardEntity = boardRepository.findById(id).get();

		BoardDTO boardDTO = new BoardDTO(boardEntity);

		return boardDTO;
	}

	//자유게시판 검색
	@Override
	public Page<BoardDTO> searchFreeboardListByTitle(int pageNum, int postNum, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "regdate"));
		Page<BoardEntity> boardEntities = boardRepository.findByTitleContainingAndCatNot(searchKeyword, "CAT-0001",
				pageRequest);
		List<BoardDTO> boardDTOs = new ArrayList<>();

		for (BoardEntity board : boardEntities) {
			BoardDTO boardDTO = new BoardDTO(board);

			boardDTOs.add(boardDTO);
		}

		return new PageImpl<>(boardDTOs, pageRequest, boardEntities.getTotalElements());
	}

	//공지 검색
	@Override
	public Page<BoardEntity> searchNoticeListByTitle(int pageNum, int postNum, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "regdate"));

		return boardRepository.findByTitleContainingAndCat(searchKeyword, "CAT-0001", pageRequest);
	}

	//질문게시판 검색
	@Override
	public Page<TestQuestionEntity> searchQboardListByTitle(int pageNum, int postNum, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));

		return questionRepository.findByTitleContaining(searchKeyword, pageRequest);

	}

	//댓글 검색
	@Override
	public Page<ReplyDTO> searchReplyListByContent(int pageNum, int postNum, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));

		Page<ReplyEntity> replyEntities = replyRepository.findByContentContaining(searchKeyword, pageRequest);
		List<ReplyDTO> replyDTOs = new ArrayList<>();

		for (ReplyEntity reply : replyEntities) {
			ReplyDTO replyDTO = new ReplyDTO(reply);
			switch (reply.getRePrnt()) {
			case "FR":
				BoardEntity board = boardRepository.findById(reply.getPrntIdx()).orElse(null);
				if (board != null) {
					replyDTO.setPrntTitle(boardRepository.findById(reply.getPrntIdx()).get().getTitle());
				} else {
					replyDTO.setPrntTitle("원글이 삭제됨");
				}
				break;
			case "Q":
				TestQuestionEntity question = questionRepository.findById(reply.getPrntIdx()).orElse(null);
				if (question != null) {
					replyDTO.setPrntTitle(questionRepository.findById(reply.getPrntIdx()).get().getTitle());
				} else {
					replyDTO.setPrntTitle("원글이 삭제됨");
				}
				break;
			case "QA":
				TestQuestionAnswerEntity answer = questionAnswerRepository.findById(reply.getPrntIdx()).orElse(null);
				if (answer != null) {
					replyDTO.setPrntTitle(questionAnswerRepository.findById(reply.getPrntIdx()).get().getContent());
				} else {
					replyDTO.setPrntTitle("원글이 삭제됨");
				}
				break;
			}
			replyDTOs.add(replyDTO);
		}

		return new PageImpl<>(replyDTOs, pageRequest, replyEntities.getTotalElements());
	}

	//채팅방 검색
	@Override
	public Page<ChatEntity> searchChatListByTitle(int pageNum, int postNum, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "regdate"));

		return chatRepository.findByTitleContaining(searchKeyword, pageRequest);
	}

	//일별 가입자수 체크
	@Override
	public long getTodaySignups() {
		LocalDateTime startOfday = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime endOfday = LocalDateTime.now().with(LocalTime.MAX);

		return memberRepository.countByRegdateBetween(startOfday, endOfday);
	}

	//일별 자유게시판 작성수
	@Override
	public long getTodayFreeBoardCount() {
		LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

		return boardRepository.countByCatAndRegdateBetween("자유게시판", startOfDay, endOfDay);
	}

	private Set<String> userIps = new HashSet<>();

	//일별 방문자 수 증가
	@Override
	public void upTodayVisitorCount(HttpServletRequest request) {
		LocalDate today = LocalDate.now();

		String ip = getUserIp(request);

		if (!today.equals(LocalDate.now())) {

			userIps.clear();
			today = LocalDate.now();
		}

		if (!userIps.contains(ip)) {
			userIps.add(ip);
		}
	}

	//일별 방문자 수 체크
	@Override
	public long getTodayVisitorCount(HttpServletRequest request) {
		return userIps.size();
	}

	//방문자 IP
	@Override
	public String getUserIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	//일별 푼 문제 수
	public long getTodayTestCount() {
		LocalDateTime startOfday = LocalDateTime.now().with(LocalTime.MIN);
		LocalDateTime endOfday = LocalDateTime.now().with(LocalTime.MAX);

		return submitRepository.countByRegdateBetween(startOfday, endOfday);
	}

	//해당년도 월별가입자수
	@Override
	public Map<Integer, Long> getMonthlySignups() {
		int currentYear = Year.now().getValue();
		List<Object[]> results = memberRepository.findMonthlySignups(currentYear);
		Map<Integer, Long> monthlySignups = new HashMap<>();
		for (Object[] result : results) {
			Integer month = (Integer) result[0];
			Long count = (Long) result[1];
			monthlySignups.put(month, count);

		}
		return monthlySignups;
	}

	//문제풀이에 사용된 언어
	public Map<String, Integer> getLngSubmitCount() {
		List<Object[]> results = submitRepository.countSubmitByLng();
		Map<String, Integer> lngCount = new HashMap<>();

		for (Object[] result : results) {
			String language = "LNG-0001".equals(result[0]) ? "JAVA" : "JavaScript";
			lngCount.put(language, ((Long) result[1]).intValue());
		}
		return lngCount;
	}

	//회원 탈퇴
	@Transactional
	@Override
	public void deleteMember(String email) {
		Optional<MemberEntity> memberOpt = memberRepository.findById(email);

		memberOpt.ifPresent(member -> {
			memberRepository.delete(member);
		});
	}

	//공통코드 관리화면
	@Override
	public Page<CommonCodeEntity> codeList(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "code"));

		return codeRepository.findAll(pageRequest);
	}

	//공통코드 검색
	@Override
	public Page<CommonCodeEntity> searchCodeListByCode(int pageNum, int postNum, String searchKeyword) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "code"));

		return codeRepository.findByCodeContaining(searchKeyword, pageRequest);
	}

	//타입에 따른 공통코드 조회
	public Page<CommonCodeEntity> getCodeListByType(int pageNum, int postNum, String type) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "code"));

		return codeRepository.findByType(type, pageRequest);

	}

	//공통코드 추가
	@Override
	public void codewrite(CommonCodeDTO code) {
		codeRepository.save(code.dtoToEntity(code));
	}

	//공통코드 삭제
	@Override
	public boolean deleteCommonCode(String code) {
		try {
			int deletedRows = codeRepository.deleteByCode(code);
			return deletedRows > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//타입에 따른 댓글 조회
	@Override
	public Page<ReplyDTO> getReplyListByType(int pageNum, int postNum, String rePrnt) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "rePrnt"));

		Page<ReplyEntity> replyEntities = replyRepository.findByRePrnt(rePrnt, pageRequest);

		List<ReplyDTO> replyDTOs = new ArrayList<>();

		for (ReplyEntity reply : replyEntities) {
			ReplyDTO replyDTO = new ReplyDTO(reply);

			switch (reply.getRePrnt()) {
			case "FR":
				BoardEntity board = boardRepository.findById(reply.getPrntIdx()).orElse(null);
				if (board != null) {
					replyDTO.setPrntTitle(board.getTitle());
				} else {
					replyDTO.setPrntTitle("원글이 삭제됨");
				}
				break;
			case "Q":
				TestQuestionEntity question = questionRepository.findById(reply.getPrntIdx()).orElse(null);
				if (question != null) {
					replyDTO.setPrntTitle(question.getTitle());
				} else {
					replyDTO.setPrntTitle("원글이 삭제됨");
				}
				break;
			case "QA":
				TestQuestionAnswerEntity answer = questionAnswerRepository.findById(reply.getPrntIdx()).orElse(null);
				if (answer != null) {
					replyDTO.setPrntTitle(answer.getContent());
				} else {
					replyDTO.setPrntTitle("원글이 삭제됨");
				}
				break;
			}
			replyDTOs.add(replyDTO);
		}

		return new PageImpl<>(replyDTOs, pageRequest, replyEntities.getTotalElements());
	}

	//회원 상세보기
	@Override
	public MemberDTO getMemberDetailByEmail(String email) {
		MemberEntity memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        return new MemberDTO(memberEntity);
	}

	//배너 저장
	@Override
	public void saveBanner(BannerEntity banner) {
		banner.setRegdate(LocalDateTime.now());
		bannerRepository.save(banner);
	}

	//배너 목록 조회
	@Override
	public Page<BannerEntity> getAllBanners(int pageNum, int postNum) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));

		return bannerRepository.findAll(pageRequest);
	}

	//메인페이지 배너
	@Override
	public List<BannerEntity> getBanner() {
		return bannerRepository.findByIsUse("Y");
	}

	//배너 isUse 상태 변경
	@Override
	public void updateBannerIsUse(Long idx, String isUse) {
		BannerEntity banner = bannerRepository.findById(idx).orElseThrow(() -> new RuntimeException("배너를 찾을 수 없습니다."));

		banner.setIsUse(isUse);
		bannerRepository.save(banner);
	}

	//배너 삭제
	@Override
	public void deleteBanner(Long idx) {
		BannerEntity bannerEntity = bannerRepository.findById(idx).get();
		bannerRepository.delete(bannerEntity);
	}

	//배너 종료일자에 따른 isUse 업데이트
	@Scheduled(cron = "0/30 * * * * *")
	@Override
	public void updateBannerEndDate() {
		LocalDateTime now = LocalDateTime.now();
		List<BannerEntity> banners = bannerRepository.findByEndDateBeforeAndIsUse(now, "Y");

		for (BannerEntity banner : banners) {
			banner.setIsUse("N");
			bannerRepository.save(banner);
		}
	}

	//배너 수정
	@Override
	public void saveBannerModify(BannerDTO bannerDTO) {
		BannerEntity bannerEntity = bannerRepository.findById(bannerDTO.getIdx()).get();
		bannerEntity.setName(bannerDTO.getName());
		bannerEntity.setUrl(bannerDTO.getUrl());
		bannerEntity.setImg(bannerDTO.getImg());
		bannerEntity.setStartDate(bannerDTO.getStartdate());
		bannerEntity.setEndDate(bannerDTO.getEnddate());
		bannerEntity.setRegdate(LocalDateTime.now());
		bannerEntity.setDesc(bannerDTO.getDescription());
		bannerRepository.save(bannerEntity);
	}

	//ID로 배너 데이터 조회
	@Override
	public BannerEntity getBannerById(Long id) {
		BannerEntity bannerEntity = bannerRepository.findById(id).get();

		return bannerEntity;

	}

}

