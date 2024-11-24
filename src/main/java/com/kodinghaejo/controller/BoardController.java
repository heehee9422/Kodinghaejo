package com.kodinghaejo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.dto.TestQuestionAnswerDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestQuestionEntity;
import com.kodinghaejo.service.BaseService;
import com.kodinghaejo.service.BoardService;
import com.kodinghaejo.service.TestService;
import com.kodinghaejo.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class BoardController {

	private final BoardService service;
	private final BaseService baseService;
	private final TestService testService;

	//게시물 목록 보기
	@GetMapping("/board/freeboard")
	public void getFreeboard(Model model, @SessionAttribute(name = "email", required = false) String email) {
		int postNum = 12;

		Page<BoardDTO> list = service.getBoardList(1, postNum, email);

		model.addAttribute("list", list);
		model.addAttribute("totalPage", list.getTotalPages());
	}

	@ResponseBody
	@PostMapping("/board/freeboard")
	public Page<BoardDTO> postFreeboard(@RequestParam("page") int page, @RequestParam("email") String email) {
		int postNum = 12;

		return service.getBoardList(page, postNum, email);
	}

	//게시물 등록 화면 보기
	@GetMapping("/board/m/freeboardWrite")
	public void getFreeboardWrite(Model model) {
		Map<String, Object> commonCode = baseService.loadUsedCommonCode();
		model.addAttribute("commonCode", commonCode);
	}

	//게시물 등록
	@ResponseBody
	@PostMapping("/board/m/write")
	public String postUpload(BoardDTO board) throws Exception {
		service.write(board);
		return "{\"message\":\"good\"}";
	}

	//게시물 수정화면 보기
	@GetMapping("/board/m/freeboardModify")
	public void getModify(Model model, @RequestParam("idx") Long idx, HttpSession session) throws Exception {
		Map<String, Object> commonCode = baseService.loadUsedCommonCode();
		model.addAttribute("view", service.view(idx, (String) session.getAttribute("email")));
		model.addAttribute("commonCode", commonCode);
	}

	//게시물 수정
	@ResponseBody
	@PostMapping("/board/m/modify")
	public String postModify(BoardDTO board) throws Exception {
		service.modify(board);
		return "{\"message\":\"good\"}";
	}

	//게시물 상세화면 보기
	@GetMapping("/board/freeboardView")
	public void getFreeboardView(Model model, @RequestParam("idx") Long idx,
			@SessionAttribute(name = "email", required = false) String email) throws Exception {
		BoardDTO view = service.view(idx, email);

		model.addAttribute("view", view);

		//조회수 증가
		if (email != null && !email.equals(view.getEmail().getEmail()))
			service.hitno(idx);
	}

	//게시물 비활성화
	@ResponseBody
	@PostMapping("/board/m/delete")
	public String deleteBoard(@RequestParam("idx") Long idx) throws Exception {
		service.deleteBoard(idx);
		return "{ \"message\": \"good\" }";
	}

	//댓글 목록
	@ResponseBody
	@PostMapping("/board/replyView")
	public Page<ReplyEntity> replyView(@RequestParam("page") int page, @RequestParam("board_idx") Long boardIdx)
			throws Exception {
		int postNum = 12;

		return service.viewReply(page, postNum, boardIdx);
	}

	//댓글 등록
	@ResponseBody
	@PostMapping("/board/m/replyWrite")
	public ReplyEntity replyWrite(ReplyInterface reply) throws Exception {
		return service.writeReply(reply);
	}

	//댓글 수정
	@ResponseBody
	@PostMapping("/board/m/replyEdit")
	public String replyEdit(ReplyInterface reply) throws Exception {
		log.info("===== reply.getIdx(): {} =====", reply.getIdx());
		log.info("===== reply.getContent(): {} =====", reply.getContent());
		service.modifyReply(reply);
		return "{ \"message\": \"good\" }";
	}

	//댓글 삭제
	@ResponseBody
	@PostMapping("/board/m/replyDelete")
	public String replyDelete(ReplyInterface reply) throws Exception {
		log.info("===== reply.getIdx(): {} =====", reply.getIdx());
		service.deleteReply(reply);
		return "{ \"message\": \"good\" }";
	}

	//게시물 추천/취소
	@ResponseBody
	@PostMapping("/board/m/recommend")
	public String postRecommend(@RequestParam("board_idx") Long boardIdx, @RequestParam("email") String email,
			@RequestParam("kind") String kind) {
		service.recommend(boardIdx, email, kind);

		return "{ \"message\": \"good\" }";
	}

	//게시물 신고
	@ResponseBody
	@PostMapping("/board/m/report")
	public String reportPost(@RequestParam("board_idx") Long boardIdx, HttpSession session) {
		String email = (String) session.getAttribute("email");
		log.info("===== boardIdx: {} / email: {} =====", boardIdx, email);
		return service.reportPost(email, boardIdx);
	}

	//공지사항 리스트
	@GetMapping("/board/noticeboard")
	public void getNoticeboard(Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 10;
		int pageListCount = 5;

		Page<BoardEntity> boards = service.getAllNotices(pageNum, postNum);

		PageUtil page = new PageUtil();
		int totalCount = (int) boards.getTotalElements();

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("notices", boards);
		model.addAttribute("pageList", page.getPageList("/board/noticeboard", "page", pageNum, postNum, pageListCount, totalCount, ""));
	}

	//공지사항 상세화면 보기
	@GetMapping("/board/noticeboardView")
	public void getNoticeboardView(Model model, @RequestParam("idx") Long idx,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@SessionAttribute(name = "email", required = false) String email) throws Exception {
		BoardDTO view = service.view(idx, email);
		model.addAttribute("view", view);
		model.addAttribute("page", page);
	}
	
	//=============== 질문보드 ==================
	
	//질문 쓰기화면 보기
	@GetMapping("/test/questionWrite")
	public void getQuestionWrite(Model model, @RequestParam("test_idx") Long testidx) throws Exception {
		model.addAttribute("test", testService.loadTest(testidx));
	}

	//질문 등록
	@ResponseBody
	@PostMapping("/test/questionWrite")
	public String questionUpload(TestQuestionDTO question) throws Exception {
		service.questionWrite(question);
		return "{\"message\":\"good\"}";
	}

	//질문 리스트 보기
	//파라미터 test_idx=all 일경우 모든 질문 보기
	//파라미터 test_idx=숫자 일경우 해당 질문만 보기
	@GetMapping("/test/questionBoard")
	public String getQuestionList(@RequestParam("test_idx") String testIdx, Model model, HttpSession session) {

		String email = (String) session.getAttribute("email");

		if ("all".equals(testIdx)) {
			long totalQuestionCount = service.getQuestionCount(); //모든 질문 개수
			List<TestQuestionEntity> allQuestions = service.getAllQuestionWithTestInfo();
			model.addAttribute("allQuestions", allQuestions);
			model.addAttribute("viewType", "all");
			model.addAttribute("totalQuestionCount", totalQuestionCount);
		} else if ("myquestion".equals(testIdx)) {
			if (email == null) {
				model.addAttribute("errorMessage", "로그인이 필요합니다.");
			}

			List<TestQuestionEntity> myQuestions = service.getMyQuestions(email);
			long myQuestionCount = service.getMyQuestionCount(email);
			List<TestEntity> myQuestionTests = service.getTestsByMyQuestions(email);

			model.addAttribute("myQuestions", myQuestions);
			model.addAttribute("myQuestionCount", myQuestionCount);
			model.addAttribute("myQuestionTests", myQuestionTests);
			model.addAttribute("viewType", "myquestion");
		} else {

			try {
				Long testIdxNum = Long.parseLong(testIdx);

				//특정 문제의 질문 가져오기
				List<TestQuestionEntity> specificQuestions = service.getQuestionsByTestIdx(testIdxNum);
				//특정 문제의 정보 가져오기
				TestEntity testInfo = service.getTestByIdx(testIdxNum);
				long specificQuestionCount = service.getQuestionCountByTestIdx(testIdxNum);

				model.addAttribute("specificQuestionCount", specificQuestionCount);
				model.addAttribute("viewType", "single");
				model.addAttribute("specificQuestions", specificQuestions);
				model.addAttribute("testInfo", testInfo);

			} catch (NumberFormatException e) {
				model.addAttribute("errorMessage", "올바르지 않은 요청입니다.");
			} catch (RuntimeException e) {
				model.addAttribute("errorMessage", e.getMessage());
			}
		}
		return "/test/questionBoard";
	}

	//질문 상세화면 보기
	@GetMapping("/test/questionBoardView")
	public void getquestionboardView(@RequestParam("question_idx") Long questionIdx, Model model, HttpSession session)
			throws Exception {
		TestEntity testInfo = service.getTestByQuestionIdx(questionIdx);

		//댓글 수
		int answerCount = service.getAnswerCountByQuestionId(questionIdx);
		model.addAttribute("answerCount", answerCount);

		model.addAttribute("questionview", service.questionview(questionIdx));
		model.addAttribute("testInfo", testInfo);
		model.addAttribute("answerList", service.answerlist(questionIdx));

	}

	//질문 수정화면보기
	@GetMapping("/test/questionModify")
	public void getquestionModify(@RequestParam("question_idx") Long questionIdx, Model model, HttpSession session)
			throws Exception {

		TestEntity testInfo = service.getTestByQuestionIdx(questionIdx);

		model.addAttribute("questionview", service.questionview(questionIdx));
		model.addAttribute("testInfo", testInfo);
	}

	//질문 수정
	@ResponseBody
	@PostMapping("/test/questionModify")
	public String questionModify(TestQuestionDTO question) throws Exception {
		service.questionmodify(question);
		return "{\"message\":\"good\"}";
	}

	//질문글 비활성화
	@GetMapping("/test/questiondeactive")
	public String deactiveQuestion(@RequestParam("question_idx") Long questionIdx) throws Exception {
		service.deactiveQuestion(questionIdx);
		return "redirect:/test/questionBoard?test_idx=all";
	}

	//답글 등록
	@ResponseBody
	@PostMapping("/test/answerWrite")
	public String answerWrite(TestQuestionAnswerDTO answer) throws Exception {
		service.answerWrite(answer);
		return "{ \"message\": \"good\" }";
	}

	//답글 비활성화
	@GetMapping("/test/answerDeactive")
	public String answerDeactive(@RequestParam("answerIdx") Long answerIdx, @RequestParam("tqIdx") Long tqIdx)
			throws Exception {
		service.answerDeactive(answerIdx);
		return "redirect:/test/questionBoardView?question_idx=" + tqIdx;
	}

}