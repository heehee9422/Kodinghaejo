package com.kodinghaejo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.service.BoardService;
import com.kodinghaejo.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class BoardController {

	private final BoardService service;

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

	//게시물 쓰기화면 보기
	@GetMapping("/board/m/freeboardWrite")
	public void getFreeboardWrite() { }

	//게시물 등록
	@ResponseBody
	@PostMapping("/board/m/write")
	public String postUpload(BoardDTO board) throws Exception {
		service.write(board);
		return "{\"message\":\"good\"}";
	}

	//게시물 수정화면 보기
	@GetMapping("/board/m/freeboardModify")
	public void getModify(@RequestParam("idx") Long idx,Model model) throws Exception {
//		model.addAttribute("view", service.view(idx));
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
	public void getFreeboardView(@RequestParam("idx") Long idx,
			Model model, @SessionAttribute(name = "email", required = false) String email)throws Exception {
		BoardDTO view = service.view(idx, email);
		
		model.addAttribute("view", view);

			//조회수 증가
		if (email != null && !email.equals(view.getEmail().getEmail()))
			service.hitno(idx);

	}

	//게시물 비활성화
	@ResponseBody
	@GetMapping("/board/m/deleteBoard")
	public String deleteBoard(@RequestParam("idx") Long idx) throws Exception {
		service.deleteBoard(idx);
		return "{ \"message\": \"good\" }";
	}
	
	//댓글 목록
	@ResponseBody
	@PostMapping("/board/replyView")
	public Page<ReplyEntity> replyView(@RequestParam("page") int page, @RequestParam("board_idx") Long boardIdx) throws Exception {
		int postNum = 12;
		
		return service.viewReply(page, postNum, boardIdx);
	}

	//댓글 등록
	@ResponseBody
	@PostMapping("/board/m/replyWrite")
	public ReplyEntity replyWrite(ReplyInterface reply) throws Exception {
		return service.writeReply(reply);
	}

	//댓글 비활성화
	@GetMapping("/board/m/replyDeactive")
	public String replyDeactive(ReplyInterface reply,@RequestParam("idx") Long idx, @RequestParam("prntIdx") Long prntIdx) throws Exception {
		System.out.println(prntIdx);
		//service.replyDeactive(reply);
		return "redirect:/board/freeboardView?idx=" + prntIdx;
	}

	//댓글 수정
	@ResponseBody
	@PostMapping("/board/m/replyModify")
	public String replyModify(ReplyInterface reply)throws Exception {

		System.out.println(reply.getContent());
		System.out.println(reply.getIdx());
		service.replyModify(reply);
		return "{\"message\":\"good\"}";
	}

	//좋아요 up,dowm
	@ResponseBody
	@PostMapping("/board/m/toggleLike")
	public Map<String, Object> toggleLike(@RequestBody Map<String, Object> requestData, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Long postIdx = Long.valueOf(requestData.get("postIdx").toString());
		String action = (String) requestData.get("action");

		boolean isLiked = "up".equals(action) ? service.likeUp(email, postIdx) : service.likeDown(email, postIdx);

//		long likeCount = service.getLikeCount(postIdx);

		Map<String, Object> response = new HashMap<>();
		response.put("isLiked", isLiked);
//		response.put("likeCount", likeCount);

		return response;
	}
	@ResponseBody
	@PostMapping("/board/m/recommend")
	public String postRecommend(@RequestParam("board_idx") Long boardIdx,
			@RequestParam("email") String email, @RequestParam("kind") String kind) {
		service.recommend(boardIdx, email, kind);
		
		return "{ \"message\": \"good\" }";
	}
	
	//신고하기
	@ResponseBody
	@PostMapping("/board/m/report")
	public Map<String, String> reportPost(@RequestBody Map<String, Object> requestData, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Long postIdx = Long.valueOf(requestData.get("postIdx").toString());

		String result = service.reportPost(email, postIdx);

		Map<String, String> response = new HashMap<>();
		if ("reported".equals(result)) {
			response.put("status", "success");
			response.put("message", "게시물이 신고되었습니다.");
		} else if ("already_reported".equals(result)) {
			response.put("status", "failure");
			response.put("message", "이미 신고한 게시물입니다.");
		}

		return response;
	}

	//공지사항 리스트
	@GetMapping("/board/noticeboard")
	public String getNoticeboard(Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;
		
		Page<BoardEntity> boards = service.getAllNotices(pageNum, postNum);
		
		PageUtil page = new PageUtil();
		int totalCount = (int) boards.getTotalElements();
		
		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("notices", boards);
		
		model.addAttribute("pageList", page.getPageList("/board/noticeboard", "page", pageNum, postNum, pageListCount, totalCount, ""));

		return "/board/noticeboard";
	}

}