package com.kodinghaejo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ChatDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestQuestionDTO;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.TestRepository;
import com.kodinghaejo.service.AdminService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class AdminController {
	
	private final AdminService service;
	
	private TestRepository testRepository;
	private BoardRepository boardRepository;
	private MemberRepository memberRepository;
	
	//시스템 관리 메인화면
	@GetMapping("/admin/systemMain")
	public void getSystemMain() { }
	
	//회원정보 관리
	@GetMapping("/admin/systemMemberInfo")
	public void getSystemMeberInfo(@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model) {
		List<MemberDTO> memberDTOs;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			memberDTOs = service.searchMembers(searchType, searchKeyword);

			model.addAttribute("members", memberDTOs);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchKeyword", searchKeyword);
			model.addAttribute("memberCount", memberDTOs.size());
		} else {
			memberDTOs = service.memberAllList();
		}

		model.addAttribute("members", memberDTOs);

		long memberCount = memberRepository.count();
		model.addAttribute("memberCount", memberCount);
	}
	
	//문제 리스트
	@GetMapping("/admin/systemTest")
	public void getSystemTest(@RequestParam(required = false) String searchKeyword, Model model) {
		List<TestDTO> testDTOs; // service.testAllList(); // 문제 리스트
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			testDTOs = service.searchtestListByTitle(searchKeyword);
		} else {
			testDTOs = service.testAllList();
		}
		
		model.addAttribute("tests", testDTOs);
		
		long testCount = testDTOs.size();
		model.addAttribute("testCount", testCount);
	}
	
	//채팅방 관리
	@GetMapping("/admin/systemChat")
	public void getSystemChat(@RequestParam(required = false) String searchKeyword, Model model) {
		List<ChatDTO> chatDTOs;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			chatDTOs = service.searchChatListByTitle(searchKeyword);
		} else {
			chatDTOs = service.chatList();
		}
		
		model.addAttribute("chats",chatDTOs);
		
		long chatCount = chatDTOs.size();
		model.addAttribute("chatCount", chatCount);
	}
	
	@Transactional
	@DeleteMapping("/admin/systemChatDelete")
	public ResponseEntity<String> deleteEmptyChat() {
		try {
			service.deleteEmptyChats();
			return ResponseEntity.ok("인원 없는 채팅방이 정리되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("채팅방 정리에 실패했습니다.");
		}
	}
	
	//공지 관리
	@GetMapping("/admin/systemNotice")
	public void getSystemNotice(@RequestParam(required = false) String searchKeyword, Model model) {
		List<BoardDTO> boardDTOs;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			boardDTOs = service.searchNoticeListByTitle(searchKeyword);
		} else {
			boardDTOs = service.noticeboardList();
		}
		
		model.addAttribute("boards",boardDTOs);
		
		long boardCount = boardDTOs.size();
		model.addAttribute("boardCount", boardCount);
	}
	
	//공지사항 수정화면
	@GetMapping("/admin/noticeboardModify")
	public String noticeModify(@RequestParam("id") Long id, Model model) {
		try {
			BoardDTO boardDTO = service.getNoticeById(id);
			model.addAttribute("board", boardDTO);
			
			return "/admin/noticeboardModify";
		} catch (Exception e) {
			log.error("Error during noticeModify", e);
			return "{\"message\": \"fail\"}";
		}
	}
	
	//공지사항 수정
	@ResponseBody
	@PostMapping("/admin/noticeboardModify")
	public String noticeModify(@RequestBody BoardDTO boardDTO) {
		try {
			
			service.savenoticeModify(boardDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during noticeModify", e);
			return "{\"message\": \"fail\"}";
		}
	}
	
	//게시물 등록
	@ResponseBody
	@PostMapping("/admin/noticeWrite")
	public String noticeWrite(BoardDTO board) throws Exception {
		service.write(board);
		return "{\"message\":\"good\"}";
	}	
	
	@Transactional
	@DeleteMapping("/admin/systemBoardDelete/{idx}") 
	public ResponseEntity<String> getBoardDelete(@PathVariable("idx") Long idx) {
		try {
			service.deleteBoard(idx);
			return ResponseEntity.ok("게시글이 정상적으로 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제에 실패했습니다.");
		}
	}
	
	//자유게시판 관리
	@GetMapping("/admin/systemFreeBoard")
	public void getSystemFreeBoard(@RequestParam(required = false) String searchKeyword, Model model) {
		List<BoardDTO> boardDTOs;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
				boardDTOs = service.searchFreeboardListByTitle(searchKeyword);
		} else {
				boardDTOs = service.freeboardList();
		}
		model.addAttribute("boards", boardDTOs);
		
		long boardCount = boardDTOs.size();
		model.addAttribute("boardCount", boardCount);
	}
	
	//질문게시판 관리
	@GetMapping("/admin/systemQBoard")
	public void getSystemQBoard(@RequestParam(required = false) String searchKeyword, Model model) {
		List<TestQuestionDTO> questionDTOs;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			questionDTOs = service.searchQboardListByTitle(searchKeyword);
		} else {
			questionDTOs = service.questionList();
		}
		
		model.addAttribute("questions", questionDTOs);
		
		long questionCount = questionDTOs.size();
		model.addAttribute("questionCount", questionCount);
	}
	
	@Transactional
	@DeleteMapping("/admin/systemQBoardDelete/{idx}") 
	public ResponseEntity<String> getQBoardDelete(@PathVariable("idx") Long idx) {
		try {
			service.deleteQBoard(idx);
			return ResponseEntity.ok("게시글이 정상적으로 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제에 실패했습니다.");
		}
	}
	
	//댓글 관리
	@GetMapping("/admin/systemReply")
	public void getSystemReply(@RequestParam(required = false) String searchKeyword, Model model) {
		List<ReplyDTO> replyDTOs;
		
		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			replyDTOs = service.searchReplyListByContent(searchKeyword);
		} else {
			replyDTOs = service.replyList();
		}
		
		model.addAttribute("replys", replyDTOs);
		
		long replyCount = replyDTOs.size();
		model.addAttribute("replyCount", replyCount);
	}

	@DeleteMapping("/admin/systemReplyDelete/{idx}") 
	public ResponseEntity<String> getReplyDelete(@PathVariable("idx") Long idx) {
		try {
			service.deleteReply(idx);
			return ResponseEntity.ok("댓글이 정상적으로 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제에 실패했습니다.");
		}
	}
	
	//공지작성화면
	@GetMapping("/admin/noticeboardWrite")
	public void getNoticeboardWrite() { }
	
	//문제 작성화면
	@GetMapping("/admin/testboardWrite")
	public void getTestboardWrite() { }
	
	//문제 작성
	@ResponseBody
	@PostMapping("/admin/testboardWrite")
	public String testWrite(@RequestBody TestDTO testDTO) {
		try {
			service.saveTestWrite(testDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during testWrite", e);
			return "{\"message\": \"fail\"}";
		}
	}
	
	//문제 수정화면
	@GetMapping("/admin/testboardModify")
	public String modifyTest(@RequestParam("id") Long id, Model model) {
		try {
			
			TestDTO testDTO = service.getTestById(id); // 서비스에서 데이터 조회
			model.addAttribute("test", testDTO);
			
			List<String> diffList = List.of("0", "1", "2");
			model.addAttribute("diffList", diffList);
			
			
			return "/admin/testboardModify";
		} catch (Exception e) {
			log.error("Error during modifyTest", e);
			return "{\"message\": \"fail\"}";
		}
	}
	//문제 수정
	@ResponseBody
	@PostMapping("/admin/testboardModify")
	public String modifyTest(@RequestBody TestDTO testDTO) {
		try {
			
			service.saveTestModify(testDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during testWrite", e);
			return "{\"message\": \"fail\"}";
		}
	}

	/*
	@GetMapping("/boardList")
	public String getBoardList(@RequestParam(defaultValue = "1") int page, Model model) {
		int pageSize = 10;	// 페이지 당 게시글 수
		int offset = (page - 1) * pageSize; // 시작 인덱스 계산
		int totalCount = boardRepository.countAllBoards(); // 총 게시글 수
		List<BoardEntity> boards = boardRepository.findBoards(offset, pageSize); // 해당 페이지의 게시글 조회

		//총 페이지 수 계산
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);

		model.addAttribute("boards", boards);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);

		return "boardList";	// Thymeleaf HTML 템플릿
	}
	*/

}