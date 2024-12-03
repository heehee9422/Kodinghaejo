package com.kodinghaejo.controller;

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
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;
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
	public String postFreeboardWrite(@RequestParam("cat") String cat, @RequestParam("title") String title,
			@RequestParam("content") String content, HttpSession session) throws Exception {
		String email = (String) session.getAttribute("email");
		String writer = (String) session.getAttribute("nickname");

		Long idx = service.write(cat, email, writer, title, content);
		return "{ \"message\": \"good\", \"idx\": \"" + idx + "\" }";
	}

	//게시물 수정화면 보기
	@GetMapping("/board/m/freeboardModify")
	public void getModify(Model model, @RequestParam("idx") Long idx, HttpSession session) throws Exception {
		String email = (String) session.getAttribute("email");
		Map<String, Object> commonCode = baseService.loadUsedCommonCode();

		model.addAttribute("view", service.view(idx, email));
		model.addAttribute("commonCode", commonCode);
	}

	//게시물 수정
	@ResponseBody
	@PostMapping("/board/m/modify")
	public String postModify(BoardDTO board) throws Exception {
		service.modify(board);
		return "{ \"message\": \"good\" }";
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

	//게시물 삭제
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
	@PostMapping("/board/m/replyModify")
	public String replyModify(ReplyInterface reply) throws Exception {
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
	public String recommend(@RequestParam("idx") Long idx, @RequestParam("kind") String kind, HttpSession session) {
		String email = (String) session.getAttribute("email");
		service.recommend(idx, email, kind);

		return "{ \"message\": \"good\" }";
	}

	//게시물 신고
	@ResponseBody
	@PostMapping("/board/m/report")
	public String reportPost(@RequestParam("idx") Long idx, HttpSession session) {
		String email = (String) session.getAttribute("email");
		log.info("===== idx: {} / email: {} =====", idx, email);
		return service.reportPost(email, idx);
	}

	//공지사항 리스트
	@GetMapping("/board/noticeboard")
	public void getNoticeboard(Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 10;
		int pageListCount = 5;

		Page<BoardDTO> boards = service.getAllNotices(pageNum, postNum);

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
	@GetMapping("/test/m/questionWrite")
	public void getQuestionWrite(Model model, @RequestParam("tl_idx") Long tlIdx) throws Exception {
		model.addAttribute("lng", testService.loadTestLngByIdx(tlIdx));
	}

	//질문 등록
	@ResponseBody
	@PostMapping("/test/m/questionWrite")
	public String postQuestionWrite(@RequestParam("tl_idx") Long tlIdx, @RequestParam("title") String title,
			@RequestParam("content") String content, HttpSession session) throws Exception {
		String email = (String) session.getAttribute("email");
		String writer = (String) session.getAttribute("nickname");
		Long idx = service.questionWrite(tlIdx, email, writer, title, content);
		return "{ \"message\": \"good\", \"idx\": \"" + idx + "\" }";
	}

	//질문 리스트 보기
	//kind : A(모든 질문), N(답변 필요), M(내 질문)
	//상기 이외의 값(숫자) : 해당 언어별 문제에 대한 질문
	@GetMapping("/test/questionBoard")
	public void getQuestionList(Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum,
			@RequestParam(name = "kind", defaultValue = "A") String kind,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@SessionAttribute(name = "email", required = false) String email) throws Exception {
		int postNum = 10;
		int pageListCount = 5;

		Page<TestQuestionDTO> list = service.getQuestionList(pageNum, postNum, kind, keyword, email);
		int totalCount = (int) list.getTotalElements();
		PageUtil page = new PageUtil();

		String params = "";
		params += (kind.equals("")) ? "" : "&kind=" + kind;
		params += (keyword.equals("")) ? "" : "&keyword=" + keyword;

		model.addAttribute("list", list);
		model.addAttribute("page", pageNum);
		model.addAttribute("totalElements", totalCount);
		model.addAttribute("kind", kind);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getPageList("/test/questionBoard", "page", pageNum, postNum, pageListCount, totalCount, params));
	}

	//질문 상세화면 보기
	@GetMapping("/test/questionBoardView")
	public void getQuestionBoardView(Model model, @RequestParam("idx") Long idx) throws Exception {
		model.addAttribute("view", service.getQuestionInfo(idx));
	}

	//질문 수정화면 보기
	@GetMapping("/test/m/questionModify")
	public void getQuestionModify(Model model, @RequestParam("idx") Long idx, HttpSession session) throws Exception {
		model.addAttribute("view", service.getQuestionInfo(idx));
	}

	//질문 수정
	@ResponseBody
	@PostMapping("/test/m/questionModify")
	public String postQuestionModify(TestQuestionDTO question) throws Exception {
		service.questionModify(question);
		return "{ \"message\": \"good\" }";
	}

	//질문 삭제
	@GetMapping("/test/m/questionDelete")
	public String questionDelete(@RequestParam("idx") Long idx) throws Exception {
		service.questionDelete(idx);
		return "{ \"message\": \"good\" }";
	}

	//답변 등록
	@ResponseBody
	@PostMapping("/test/m/answerWrite")
	public TestQuestionAnswerEntity answerWrite(@RequestParam("tq_idx") Long tqIdx,
			@RequestParam("content") String content, HttpSession session) throws Exception {
		String email = (String) session.getAttribute("email");
		String writer = (String) session.getAttribute("nickname");

		return service.answerWrite(tqIdx, email, writer, content);
	}

	//답변 수정
	@ResponseBody
	@PostMapping("/test/m/answerModify")
	public String answerModify(@RequestParam("idx") Long idx, @RequestParam("content") String content) throws Exception {
		service.answerModify(idx, content);
		return "{ \"message\": \"good\" }";
	}

	//답글 비활성화
	@ResponseBody
	@PostMapping("/test/m/answerDelete")
	public String answerDeactive(@RequestParam("idx") Long idx) throws Exception {
		service.answerDelete(idx);
		return "{ \"message\": \"good\" }";
	}

	//질문게시판 댓글 등록
	@ResponseBody
	@PostMapping("/test/m/replyWrite")
	public ReplyEntity testReplyWrite(ReplyInterface reply) throws Exception {
		return service.writeReply(reply);
	}

	//질문게시판 댓글 수정
	@ResponseBody
	@PostMapping("/test/m/replyModify")
	public String testReplyModify(ReplyInterface reply) throws Exception {
		log.info("===== reply.getIdx(): {} =====", reply.getIdx());
		log.info("===== reply.getContent(): {} =====", reply.getContent());
		service.modifyReply(reply);
		return "{ \"message\": \"good\" }";
	}

	//질문게시판 댓글 삭제
	@ResponseBody
	@PostMapping("/test/m/replyDelete")
	public String testReplyDelete(ReplyInterface reply) throws Exception {
		log.info("===== reply.getIdx(): {} =====", reply.getIdx());
		service.deleteReply(reply);
		return "{ \"message\": \"good\" }";
	}

}