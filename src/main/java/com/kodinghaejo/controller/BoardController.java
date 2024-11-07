package com.kodinghaejo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.service.BoardService;

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
	public void getFreeboardList(Model model) { 
		List<BoardEntity> posts = service.getPosts();
		model.addAttribute("posts", posts);
	}
			
		
	//게시물 쓰기화면 보기
	@GetMapping("/board/freeboardWrite")
	public void getFreeboardWrite() { }
	
	//게시물 등록
	@ResponseBody
	@PostMapping("/board/write")
	public String postUpload(BoardDTO board) throws Exception {
		service.write(board);
		return "{\"message\":\"good\"}";
	}	
	
	//게시물 수정화면 보기
	@GetMapping("/board/freeboardmodify")
	public void getModify(@RequestParam("idx") Long idx,Model model) throws Exception {
		model.addAttribute("view", service.view(idx));
	}
	
	//게시물 수정
	@ResponseBody
	@PostMapping("/board/modify")
	public String postModify(BoardDTO board) throws Exception {
		service.modify(board);
		Long idx = board.getIdx();
		return "{\"message\":\"good\"}";
	}

	//게시물 상세화면 보기
	@GetMapping("/board/freeboardView")
	public void getFreeboardView(@RequestParam("idx") Long idx,
		Model model, HttpSession session)throws Exception {
		model.addAttribute("view", service.view(idx));
		model.addAttribute("replies",service.replyView(idx));
	}
	
	//게시물 비활성화
	@GetMapping("/board/deactive")
	public String deactivePost(BoardDTO board)throws Exception  {
		service.deactivePost(board);
		Long idx = board.getIdx();
		return "redirect:/board/freeboard";
	}

	//댓글 등록
	@ResponseBody
	@PostMapping("/board/replyWrite")
	public String replyWrite(ReplyDTO reply) throws Exception {
		service.replyWrite(reply);
		return "{\"message\":\"good\"}";
	}
		
		
	//댓글 비활성화
	@GetMapping("/board/replyDeactive")
	public String replyDeactive(ReplyInterface reply,@RequestParam("idx") Long idx, @RequestParam("prntIdx") Long prntIdx) throws Exception {						
		System.out.println(prntIdx);
		//service.replyDeactive(reply);
		return "redirect:/board/freeboardView?idx=" + prntIdx;
	}

	@GetMapping("/board/noticeboard")
	public void getNoticeboard() { }

}