package com.kodinghaejo.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.kodinghaejo.service.AdminService;
import com.kodinghaejo.service.MailService;
import com.kodinghaejo.service.MemberService;
import com.kodinghaejo.util.PageUtil;
import com.kodinghaejo.util.PasswordMaker;
import com.nimbusds.jose.shaded.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class AdminController {

	private final AdminService service;
	private final MemberService memberService;
	private final BCryptPasswordEncoder pwEncoder;
	private final MailService mailService;

	//시스템 관리 메인화면
	@GetMapping("/admin/systemMain")
	public String getSystemMain(Model model, HttpServletRequest request) {
		long todaySignups = service.getTodaySignups();
		model.addAttribute("todaySignups", todaySignups);

		long todayVisitorCount = service.getTodayVisitorCount(request);
		model.addAttribute("todayVisitorCount", todayVisitorCount);

		long todayFreeboardCount = service.getTodayFreeBoardCount();
		model.addAttribute("todayFreeboardCount", todayFreeboardCount);

		long todayTestCount = service.getTodayTestCount();
		model.addAttribute("todayTestCount", todayTestCount);

		Map<Integer, Long> monthlySignups = service.getMonthlySignups();
		String monthlySignupsJson = new Gson().toJson(monthlySignups);
		model.addAttribute("monthlySignupsJson", monthlySignupsJson);

		Map<String, Integer> lngCount = service.getLngSubmitCount();
		try {
			model.addAttribute("lngCount", new ObjectMapper().writeValueAsString(lngCount));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("lngCount", "{}");
		}

		return "/admin/systemMain";
	}

	//회원정보 관리
	@GetMapping("/admin/systemMemberInfo")
	public void getSystemMeberInfo(@RequestParam(name = "searchType", defaultValue = "") String searchType,
			@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<MemberEntity> members;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			members = service.searchMembers(pageNum, postNum, searchType, searchKeyword);
		} else {
			members = service.memberAllList(pageNum, postNum);
		}

		PageUtil page = new PageUtil();
		int totalCount = (int) members.getTotalElements();

		String params = "";
		params += (searchType.equals("")) ? "" : ("&searchType=" + searchType);
		params += (searchKeyword.equals("")) ? "" : ("&searchKeyword=" + searchKeyword);

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("members", members);

		model.addAttribute("searchType", searchType);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("pageList", page.getPageList("/admin/systemMemberInfo", "page", pageNum, postNum, pageListCount, totalCount, params));
	}

	//문제 리스트
	@GetMapping("/admin/systemTest")
	public void getSystemTest(@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<TestDTO> tests;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			tests = service.searchtestListByTitle(pageNum, postNum, searchKeyword);
		} else {
			tests = service.testAllList(pageNum, postNum);
		}

		PageUtil page = new PageUtil();
		int totalCount = (int) tests.getTotalElements();

		String params = "";
		params += (searchKeyword.equals("")) ? "" : ("&searchKeyword=" + searchKeyword);

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("tests", tests);
		model.addAttribute("searchKeyword", searchKeyword);

		model.addAttribute("pageList", page.getPageList("/admin/systemTest", "page", pageNum, postNum, pageListCount, totalCount, params));
	}

	//채팅방 관리
	@GetMapping("/admin/systemChat")
	public void getSystemChat(@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<ChatEntity> chats;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			chats = service.searchChatListByTitle(pageNum, postNum, searchKeyword);
		} else {
			chats = service.chatList(pageNum, postNum);
		}

		PageUtil page = new PageUtil();
		int totalCount = (int) chats.getTotalElements();

		String params = "";
		params += (searchKeyword.equals("")) ? "" : ("&searchKeyword=" + searchKeyword);

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("chats", chats);
		model.addAttribute("searchKeyword", searchKeyword);

		model.addAttribute("pageList", page.getPageList("/admin/systemChat", "page", pageNum, postNum, pageListCount, totalCount, params));
	}

	//채팅인원 0인 채팅방 삭제
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
	public void getSystemNotice(@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword,
			Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<BoardEntity> boards;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			boards = service.searchNoticeListByTitle(pageNum, postNum, searchKeyword);
		} else {
			boards = service.noticeboardList(pageNum, postNum);
		}

		PageUtil page = new PageUtil();
		int totalCount = (int) boards.getTotalElements();

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("boards", boards);

		model.addAttribute("pageList", page.getPageList("/admin/systemNotice", "page", pageNum, postNum, pageListCount, totalCount, ((searchKeyword.equals("") ? "" : ("&searchKeyword=" + searchKeyword)))));
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
	public String noticeModify(@ModelAttribute BoardDTO boardDTO) {
		try {
			service.savenoticeModify(boardDTO);
			return "{\"message\": \"good\"}";
		} catch (Exception e) {
			log.error("Error during noticeModify", e);
			return "{\"message\": \"fail\"}";
		}
	}

	//공지사항 등록
	@ResponseBody
	@PostMapping("/admin/noticeWrite")
	public String noticeWrite(BoardDTO board) throws Exception {
		service.write(board);
		return "{\"message\":\"good\"}";
	}

	//게시물 삭제
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
	public void getSystemFreeBoard(@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword,
			Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<BoardDTO> boards;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			boards = service.searchFreeboardListByTitle(pageNum, postNum, searchKeyword);
		} else {
			boards = service.freeboardList(pageNum, postNum);
		}
		PageUtil page = new PageUtil();
		int totalCount = (int) boards.getTotalElements();

		String params = "";
		params += (searchKeyword.equals("")) ? "" : ("&searchKeyword=" + searchKeyword);

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("boards", boards);

		model.addAttribute("pageList", page.getPageList("/admin/systemFreeBoard", "page", pageNum, postNum, pageListCount, totalCount, params));
	}

	//질문게시판 관리
	@GetMapping("/admin/systemQBoard")
	public void getSystemQBoard(@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword,
			Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<TestQuestionEntity> questions;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			questions = service.searchQboardListByTitle(pageNum, postNum, searchKeyword);
		} else {
			questions = service.questionList(pageNum, postNum);
		}

		PageUtil page = new PageUtil();
		int totalCount = (int) questions.getTotalElements();

		String params = "";
		params += (searchKeyword.equals("")) ? "" : ("&searchKeyword=" + searchKeyword);

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("questions", questions);

		model.addAttribute("pageList", page.getPageList("/admin/systemQBoard", "page", pageNum, postNum, pageListCount, totalCount, params));
	}

	//질문게시판 글 삭제
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
	public void getSystemReply(@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword, Model model,
			@RequestParam(name = "filter", required = false, defaultValue = "ALL") String filter,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<ReplyDTO> replys;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			replys = service.searchReplyListByContent(pageNum, postNum, searchKeyword);
		} else if (!"ALL".equalsIgnoreCase(filter)) {
			replys = service.getReplyListByType(pageNum, postNum, filter);
		} else {
			replys = service.replyList(pageNum, postNum);
		}

		PageUtil page = new PageUtil();
		int totalCount = (int) replys.getTotalElements();

		String params = "";
		params += (searchKeyword.equals("")) ? "" : ("&searchKeyword=" + searchKeyword);
		params += (filter.equals("")) ? "" : ("&filter=" + filter);

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("replys", replys);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("filter", filter);

		model.addAttribute("pageList", page.getPageList("/admin/systemReply", "page", pageNum, postNum, pageListCount, totalCount, params));
	}

	//댓글 삭제
	@Transactional
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
	public void getNoticeboardWrite() {
	}

	//문제 작성화면
	@GetMapping("/admin/testboardWrite")
	public void getTestboardWrite() {
	}

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
			TestDTO testDTO = service.getTestById(id); //서비스에서 데이터 조회
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

	//회원 탈퇴
	@ResponseBody
	@PostMapping("/admin/systemMemberDelete/{email}")
	public String deleteMember(@PathVariable("email") String email) {

		try {
			service.deleteMember(email);
			return "{ \"message\": \"good\" }";
		} catch (Exception e) {
			return "{\"message\": \"fail\"}";
		}
	}

	//공통코드 관리
	@GetMapping("/admin/systemCommonCode")
	public void getCommonCode(@RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(name = "filter", required = false, defaultValue = "ALL") String filter, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<CommonCodeEntity> codes;

		if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
			codes = service.searchCodeListByCode(pageNum, postNum, searchKeyword);
			filter = "";
		} else if (!"ALL".equalsIgnoreCase(filter)) {
			codes = service.getCodeListByType(pageNum, postNum, filter);
		} else {
			codes = service.codeList(pageNum, postNum);
		}

		PageUtil page = new PageUtil();
		int totalCount = (int) codes.getTotalElements();

		String params = "";
		params += (searchKeyword.equals("")) ? "" : ("&searchKeyword=" + searchKeyword);
		params += (filter.equals("")) ? "" : ("&filter=" + filter);

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("codes", codes);
		model.addAttribute("filter", filter);

		model.addAttribute("pageList", page.getPageList("/admin/systemCommonCode", "page", pageNum, postNum, pageListCount, totalCount, params));
	}

	//공통코드 작성 화면
	@GetMapping("/admin/systemCommonCodeWrite")
	public void getCommonCodeWrite() { }

	//공통코드 추가
	@ResponseBody
	@PostMapping("/admin/systemCommonCodeWrite")
	public String CommonCodeWrite(CommonCodeDTO code) throws Exception {
		service.codewrite(code);
		return "{\"message\":\"good\"}";
	}

	//공통코드 삭제
	@ResponseBody
	@Transactional
	@PostMapping("/admin/systemCommonCodeDelete")
	public void deleteCommonCode(@RequestParam("code") String code) {
		service.deleteCommonCode(code);
	}

	//회원관리 상세보기
	@GetMapping("/admin/MemberDetail/{email}")
	public ResponseEntity<MemberDTO> getMemberDetails(@PathVariable String email) {

		MemberDTO memberDTO = service.getMemberDetailByEmail(email);
		return ResponseEntity.ok(memberDTO);
	}

	//관리자페이지 임시 비밀번호 발급
	@ResponseBody
	@PostMapping("/admin/tempPassword")
	public String TempPassword(@RequestBody MemberDTO findData) {
		log.info("===================", findData.getEmail());
		if (memberService.checkEmail(findData.getEmail()) == 0)
			return "{ \"message\": \"ID_NOT_FOUND\" }";

		String password = new PasswordMaker().tempPasswordMaker(8);
		findData.setPassword(pwEncoder.encode(password));
		memberService.modifyPassword(findData);
		memberService.lastdateUpdate(findData.getEmail(), "password");

		mailService.sendSimpleMailMessage("findPassword", findData.getEmail(), password);

		return "{ \"message\": \"good\" }";
	}

	//광고 배너 관리화면
	@GetMapping("/admin/systemBanner")
	public void getSystemBanner(Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum) {
		int postNum = 5;
		int pageListCount = 5;

		Page<BannerEntity> banners;
		banners = service.getAllBanners(pageNum, postNum);

		PageUtil page = new PageUtil();
		int totalCount = (int) banners.getTotalElements();

		model.addAttribute("page", pageNum);
		model.addAttribute("postNum", postNum);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("banners", banners);

		model.addAttribute("pageList", page.getPageList("/admin/systemBanner", "page", pageNum, postNum, pageListCount, totalCount, ""));
	}

	//광고 배너 추가화면
	@GetMapping("/admin/systemBannerWrite")
	public void getSystemBannerWrite() { }

	//광고 배너 추가
	@ResponseBody
	@PostMapping("/admin/systemBannerWrite")
	public Map<String, Object> postSystemBannerWrite(@RequestParam("name") String name, @RequestParam("url") String url,
			@RequestParam("startDate") LocalDateTime startDate, @RequestParam("endDate") LocalDateTime endDate,
			@RequestParam("img") MultipartFile imageFile, @RequestParam("isUse") String isUse,
			@RequestParam("desc") String desc) {
		Map<String, Object> response = new HashMap<>();

		try {
			//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작
			String os = System.getProperty("os.name").toLowerCase();
			String path;

			if (os.contains("win"))
				path = "Z:\\임시저장소\\프로젝트관리\\1회차\\2조\\Repository\\banner\\";
			else
				path = "/home/hee/Repository/banner";

			//디렉토리가 존재하는지 체크해서 없다면 생성
			File p = new File(path);
			if (!p.exists())
				p.mkdir();
			//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료

			String originalFilename = imageFile.getOriginalFilename();
			String savedFilename = System.currentTimeMillis() + "_" + originalFilename;

			File savedFile = new File(path + savedFilename);
			imageFile.transferTo(savedFile);

			BannerEntity banner = new BannerEntity();
			banner.setName(name);
			banner.setUrl(url);
			banner.setDesc(desc);
			banner.setStartDate(startDate);
			banner.setEndDate(endDate);
			banner.setIsUse(isUse);
			banner.setImg(savedFilename);

			service.saveBanner(banner);

			response.put("message", "good");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "error");
		}
		return response;
	}

	//배너 isUse 상태 업데이트
	@PutMapping("/admin/updateBannerIsUse/{idx}")
	public ResponseEntity<Void> updateBannerIsUse(@PathVariable Long idx, @RequestBody Map<String, String> request) {
		String isUse = request.get("isUse");

		service.updateBannerIsUse(idx, isUse);

		return ResponseEntity.ok().build();
	}

	//배너 삭제
	@ResponseBody
	@Transactional
	@PostMapping("/admin/systemBannerDelete")
	public void deleteBanner(@RequestParam("idx") Long idx) {
		service.deleteBanner(idx);
	}

	//배너 수정화면
	@GetMapping("/admin/systemBannerModify")
	public String getBannerModify(@RequestParam("idx") Long idx, Model model) {
		try {
			BannerEntity bannerEntity = service.getBannerById(idx);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			String startDate = bannerEntity.getStartDate().format(formatter);
			String endDate = bannerEntity.getEndDate().format(formatter);

			model.addAttribute("banner", bannerEntity);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);

			return "/admin/systemBannerModify";
		} catch (Exception e) {
			log.error("Error during BannerModify", e);
			return "{\"message\": \"fail\"}";
		}
	}

	//배너 수정
	@ResponseBody
	@PostMapping("/admin/systemBannerModify")
	public Map<String, Object> postSystemBannerModify(@RequestParam("idx") Long idx, @RequestParam("name") String name,
			@RequestParam("url") String url, @RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate,
			@RequestParam(value = "img", required = false) MultipartFile imageFile, //이미지 파일 선택 여부
			@RequestParam("existingImg") String existingImg, //기존 이미지 경로
			@RequestParam("isUse") String isUse, @RequestParam("desc") String desc) {
		Map<String, Object> response = new HashMap<>();

		try {
			//운영체제에 따라 이미지 저장 디렉토리 설정
			String os = System.getProperty("os.name").toLowerCase();
			String path;
			if (os.contains("win")) {
				path = "Z:\\임시저장소\\프로젝트관리\\1회차\\2조\\Repository\\banner\\";
			} else {
				path = "/home/hee/Repository/banner";
			}

			//디렉토리가 존재하지 않으면 생성
			File p = new File(path);
			if (!p.exists())
				p.mkdir();

			BannerEntity banner = service.getBannerById(idx);
			if (banner == null) {
				response.put("message", "not_found");
				return response;
			}

			//새로운 이미지 파일 처리
			if (imageFile != null && !imageFile.isEmpty()) {
				String originalFilename = imageFile.getOriginalFilename();
				String savedFilename = System.currentTimeMillis() + "_" + originalFilename;

				File savedFile = new File(path + savedFilename);
				imageFile.transferTo(savedFile);

				//기존 이미지 삭제
				if (existingImg != null && !existingImg.isEmpty()) {
					File oldFile = new File(path + existingImg);
					if (oldFile.exists())
						oldFile.delete();
				}

				banner.setImg(savedFilename);
			} else {
				banner.setImg(existingImg);
			}

			banner.setName(name);
			banner.setUrl(url);
			banner.setDesc(desc);
			banner.setStartDate(startDate);
			banner.setEndDate(endDate);
			banner.setIsUse(isUse);

			service.saveBanner(banner);

			response.put("message", "good");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "error");
		}

		return response;
	}

}
