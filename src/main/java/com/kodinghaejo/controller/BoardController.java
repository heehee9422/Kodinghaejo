package com.kodinghaejo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
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
	public void getFreeboardList(Model model,HttpSession session) { 
		 
			// 세션에서 이메일 가져오기
			String sessionEmail = (String) session.getAttribute("email");
	 
			
			List<BoardEntity> posts = service.getPosts();
			model.addAttribute("posts", posts);

			// 게시물 ID 목록
			List<Long> postIds = posts.stream().map(BoardEntity::getIdx).collect(Collectors.toList());

			// 게시물별 댓글 수 조회
			Map<Long, Integer> replyCounts = service.getReplyCounts(postIds);
			model.addAttribute("replyCounts", replyCounts);
			  
			//게시물별 좋아요 수 조회
			Map<Long, Long> likeCounts = service.getAllBoardLikeCounts(postIds);
			model.addAttribute("likeCounts", likeCounts);	
		
			
			// 현재 사용자의 게시물별 좋아요 상태 조회
			Map<Long, String> likeStatusMap = posts.stream().collect(Collectors.toMap(
					BoardEntity::getIdx,
					post -> service.isPostLikedByUser(sessionEmail, post.getIdx())
			));
			
			model.addAttribute("likeStatusMap", likeStatusMap);
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
		model.addAttribute("view", service.view(idx));
	}
	
	//게시물 수정
	@ResponseBody
	@PostMapping("/board/m/modify")
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
		
		// 댓글 수
		int replyCount = service.getReplyCountByPostId(idx);
		model.addAttribute("replyCount", replyCount);
		
		//세션 email값 가져 오기
			String sessionEmail = (String)session.getAttribute("email");
		
			//조회수 증가
		if (sessionEmail != null) {
		 	if(!sessionEmail.equals(service.view(idx).getEmail().getEmail())){
		 		service.hitno(idx);
			}
		}
		 
		//좋아요 갯수 보여주는
		model.addAttribute("likeCount",service.getLikeCount(idx));	
			
		String isLiked = service.isPostLikedByUser(sessionEmail, idx);
		model.addAttribute("isLiked", isLiked);
		
	
	}
	
	//게시물 비활성화
	@GetMapping("/board/m/deactive")
	public String deactivePost(BoardDTO board)throws Exception	{
		service.deactivePost(board);
		Long idx = board.getIdx();
		return "redirect:/board/freeboard";
	}

	//댓글 등록
	@ResponseBody
	@PostMapping("/board/m/replyWrite")
	public String replyWrite(ReplyDTO reply) throws Exception {
		service.replyWrite(reply);
		return "{\"message\":\"good\"}";
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

		long likeCount = service.getLikeCount(postIdx);

		Map<String, Object> response = new HashMap<>();
		response.put("isLiked", isLiked);
		response.put("likeCount", likeCount);

		return response;
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

	@GetMapping("/board/noticeboard")
	public String getNoticeboard(Model model) {
		List<BoardDTO> boardDTOs = service.getAllNotices();
		model.addAttribute("notices", boardDTOs);
		
		return "/board/noticeboard";
	}

	//==================== 마이 페이지 ====================
	
	//내 게시판
	@GetMapping("/member/mypage/myboard")
	public void getMypageMyboard(Model model, @RequestParam(name = "boardPage", defaultValue = "1") int boardPageNum, 
			@RequestParam(name = "replyPage", defaultValue = "1") int replyPageNum, HttpSession session) throws Exception {
		int postNum = 5;
		int pageListCount = 5;
		
		PageUtil page = new PageUtil();
		Page<BoardDTO> boardList = service.mypageBoardList((String) session.getAttribute("email"), boardPageNum, postNum);
		Page<ReplyDTO> replyList = service.mypageReplyList((String) session.getAttribute("email"), replyPageNum, postNum);
		int boardTotalCount = (int) boardList.getTotalElements();
		int replyTotalCount = (int) replyList.getTotalElements();
		
		model.addAttribute("boardList", boardList); //게시글 리스트
		model.addAttribute("replyList", replyList); //댓글 리스트
		model.addAttribute("boardTotalElement", boardTotalCount); //게시글 총 개수
		model.addAttribute("replyTotalElement", replyTotalCount); //댓글 총 개수
		model.addAttribute("postNum", postNum); //한 페이지에 보일 목록 개수
		model.addAttribute("boardPage", boardPageNum); //게시글 페이지
		model.addAttribute("replyPage", replyPageNum); //댓글 페이지
		model.addAttribute("boardPageList", page.getMypageMyboardPageList("board", boardPageNum, replyPageNum, postNum, pageListCount, boardTotalCount, replyTotalCount)); //게시글 페이징 처리
		model.addAttribute("replyPageList", page.getMypageMyboardPageList("reply", boardPageNum, replyPageNum, postNum, pageListCount, boardTotalCount, replyTotalCount)); //댓글 페이징 처리
	}

}