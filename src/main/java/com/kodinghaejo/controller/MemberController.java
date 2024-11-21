package com.kodinghaejo.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.service.BaseService;
import com.kodinghaejo.service.MailService;
import com.kodinghaejo.service.MemberService;
import com.kodinghaejo.util.PageUtil;
import com.kodinghaejo.util.PasswordMaker;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class MemberController {

	private final MemberService service;
	private final MailService mailService;
	private final BaseService baseService;
	private final BCryptPasswordEncoder pwEncoder;

	//로그인
	@GetMapping("/member/login")
	public void getLogin() { }

	//로그인 --> 스프링 시큐리티에 의해 인터셉트
	@PostMapping("/member/login")
	public void postLogin() { }

	//로그인 처리
	@ResponseBody
	@PostMapping("member/checkLogin")
	public String postCheckLogin(MemberDTO loginData) {
		//이메일(아이디) 가입여부 확인
		if (service.checkEmail(loginData.getEmail()) == 0) //해당 이메일 회원이 존재하지 않음
			return "{ \"message\": \"ID_NOT_FOUND\" }";

		//비밀번호 일치여부 확인
		if (!pwEncoder.matches(loginData.getPassword(), service.memberInfo(loginData.getEmail()).getPassword())) //비밀번호가 일치하지 않을 경우
			return "{ \"message\": \"PASSWORD_NOT_MATCH\" }";

		return "{ \"message\": \"good\" }";
	}

	//로그아웃 --> 스프링 시큐리티에 의해 인터셉트
	@GetMapping("/member/logout")
	public void getLogout() {
		log.info("==================== 로그아웃 ====================");
	}

	//로그아웃 전처리
	@GetMapping("/member/beforeLogout")
	public String getMemberSessionOut(HttpSession session) {
		String email = (String) session.getAttribute("email");
		service.lastdateUpdate(email, "logout");
		service.memberLogRegistry(email, "logout");
		session.invalidate();

		log.info("==================== 로그아웃 전 로그 등록 및 세션 종료 ====================");

		return "redirect:/member/logout";
	}

	//회원가입 화면
	@GetMapping("/member/join")
	public void getJoin() { }

	//회원가입 및 기본정보 수정
	@ResponseBody
	@PostMapping("/member/join")
	public String postJoin(MemberDTO member, @RequestParam("kind") String kind,
			@RequestParam(name = "fileUpload", required = false) MultipartFile mpf,
			HttpSession session) throws Exception {
		//========== 운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작 ==========
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if (os.contains("win"))
			path = "C:\\Repository\\Kodinghaejo\\profile\\";
		else
			path = "/home/user/Repository/Kodinghaejo/profile/";

		//디렉토리 존재여부 확인 --> 없을 경우 생성 처리
		File p = new File(path);
		if (!p.exists()) p.mkdirs();
		//========== 운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료 ==========

		//프로필 이미지 저장 경로 설정
		String orgImg = "";
		long imgSize = 0L;

		//프로필 이미지가 첨부되었을 경우에만 실행
		if (mpf != null && !mpf.isEmpty()) {
			File targetFile = null;

			orgImg = mpf.getOriginalFilename();

			//파일명.확장자 --> 확장자 분리하여 저장
			String orgExt = orgImg.substring(orgImg.lastIndexOf('.'));

			//UUID.randomUUID() --> 영대소문자 + 특수문자로 이루어진 무작위 문자열 생성
			String storedImg = UUID.randomUUID().toString().replaceAll("-", "") + orgExt;
			imgSize = mpf.getSize(); //파일 사이즈(바이트 단위)
			targetFile = new File(path + storedImg);
			mpf.transferTo(targetFile);
			member.setOrgImg(orgImg);
			member.setStoredImg(storedImg);
			member.setImgSize(imgSize);
		}

		//회원가입
		if (kind.equals("I")) {
			if (service.checkEmail(member.getEmail()) == -1)
				return "{ \"message\": \"DELETE_ACCOUNT_DENY\" }";

			member.setPassword(pwEncoder.encode(member.getPassword()));
			service.join(member);
		}

		//기본정보 수정
		if (kind.equals("U")) {
			if (member.getNickname().equals("")) member.setNickname(member.getUsername());

			//프로필 이미지 변경 시 기존 이미지 파일 삭제
			if (mpf != null && !mpf.isEmpty() ) {
				MemberDTO before = service.memberInfo(member.getEmail());
				File file = new File(path + before.getStoredImg());
				file.delete();
			}

			session.setAttribute("nickname", member.getNickname());

			//수정 내용 반영
			service.editMemberInfo(member);
		}

		log.info("==================== 회원가입 JSON: { \"message\": \"good\", \"username\": \"{}\" }", member.getUsername());
		return "{ \"message\": \"good\", \"username\": \"" + URLEncoder.encode(member.getUsername(), "UTF-8") + "\" }";
	}

	//이메일(아이디) 중복 확인
	@ResponseBody
	@PostMapping("/member/checkEmail")
	public int getCheckEmail(@RequestBody String email) throws Exception {
		return service.checkEmail(email);
	}

	//아이디(이메일) 찾기 화면
	@GetMapping("/member/findId")
	public void getFindId() { }

	//아이디(이메일) 찾기
	@ResponseBody
	@PostMapping("/member/findId")
	public String postFindId(MemberDTO findData) {
		String email = service.findId(findData);

		if (email == "") {
			return "{ \"message\": \"ID_NOT_FOUND\" }";
		} else {
			log.info("==================== 아이디(이메일) 찾기 JSON: { \"message\": \"good\", \"id\": \"{}\" } ====================", email);
			return "{ \"message\": \"good\", \"id\": \"" + email + "\" }";
		}
	}

	//비밀번호 찾기 화면
	@GetMapping("/member/findPassword")
	public void getFindPassword() { }

	//임시 비밀번호 발급받기
	@ResponseBody
	@PostMapping("/member/findPassword")
	public String postFindPassword(MemberDTO findData) {
		//아이디(이메일) 존재여부 확인
		if (service.checkEmail(findData.getEmail()) == 0) //회원이 존재하지 않음
			return "{ \"message\": \"ID_NOT_FOUND\" }";

		//아이디가 존재하면 해당 email로 회원정보 가져오기
		MemberDTO member = service.memberInfo(findData.getEmail());

		//전화번호 확인
		if (!findData.getTel().equals(member.getTel())) {
			return "{ \"message\": \"TEL_NOT_MATCH\" }";
		} else { //전화번호가 일치할 경우
			String password = new PasswordMaker().tempPasswordMaker(8);
			findData.setPassword(pwEncoder.encode(password));
			service.editPassword(findData);
			service.lastdateUpdate(findData.getEmail(), "password");

			mailService.sendSimpleMailMessage(findData.getEmail(), password);

			return "{ \"message\": \"good\" }";
		}
	}

	//회원 프로필 이미지 보기
	@GetMapping("/member/img/{email}")
	public void filedownload(@PathVariable("email") String email, HttpServletResponse rs) throws Exception {
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if (os.contains("win"))
			path = "C:\\Repository\\Kodinghaejo\\profile\\";
		else
			path = "/home/mklee/Repository/Kodinghaejo/profile/";

		//디렉토리가 존재하는지 체크해서 없다면 생성
		File p = new File(path);
		if (!p.exists()) p.mkdirs();
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료

		MemberDTO member = service.memberInfo(email);

		//다운로드할 파일의 경로와 파일명을 매개변수로 입력받아 byte 데이터타입의 1차원 배열로 저장
		byte[] fileByte = FileUtils.readFileToByteArray(new File(path + member.getStoredImg()));

		//예) HTTP Response Header는 Content-Disposition: attachment; filename="hello.jpg";
		//    HTTP Response Body에는 1차원 바이트 타입으로 변환된 배열
		rs.setContentType("application/octet-stream");
		rs.setContentLength(fileByte.length);
		rs.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(member.getOrgImg(), "UTF-8") + "\";");
		rs.getOutputStream().write(fileByte); //stream을 통해 1차원 byte 타입 배열로 변환된 데이터(추후 파일로 변환)를 버퍼에 씀
		rs.getOutputStream().flush(); //버퍼에 있는 내용을 write
		rs.getOutputStream().close(); //스트림 닫기
	}

	//==================== 마이 페이지 ====================

	//내 정보
	@GetMapping("/member/mypage/main")
	public void getMypageMain(Model model, HttpSession session) {
		MemberDTO member = service.memberInfo((String) session.getAttribute("email"));
		Map<String, Object> commonCode = baseService.loadUsedCommonCode();
		model.addAttribute("member", member);
		model.addAttribute("commonCode", commonCode);
	}

	//주요 기술 변경
	@GetMapping("/member/mypage/editTec")
	public void getMypageEditTec(Model model, HttpSession session) {
		MemberDTO member = service.memberInfo((String) session.getAttribute("email"));
		Map<String, Object> commonCode = baseService.loadUsedCommonCode();
		model.addAttribute("member", member);
		model.addAttribute("commonCode", commonCode);
	}

	//주요 기술 변경 저장
	@ResponseBody
	@PostMapping("/member/mypage/editTec")
	public String postMypageEditTec(@RequestParam(name = "tec1", defaultValue = "") String tec1,
			@RequestParam(name = "tec2", defaultValue = "") String tec2,
			@RequestParam(name = "tec3", defaultValue = "") String tec3,
			HttpSession session) {
		log.info("===== tec1: {}, tec2: {}, tec3: {} =====", tec1, tec2, tec3);

		String email = (String) session.getAttribute("email");
		service.editTec(email, tec1, tec2, tec3);

		return "{ \"message\": \"good\" }";
	}

	//희망 직무 변경
	@GetMapping("/member/mypage/editJob")
	public void getMypageEditJob(Model model, HttpSession session) {
		MemberDTO member = service.memberInfo((String) session.getAttribute("email"));
		Map<String, Object> commonCode = baseService.loadUsedCommonCode();
		model.addAttribute("member", member);
		model.addAttribute("commonCode", commonCode);
	}

	//희망직무 변경 저장
	@ResponseBody
	@PostMapping("/member/mypage/editJob")
	public String postMypageEditJob(@RequestParam(name = "job1", defaultValue = "") String job1,
			@RequestParam(name = "job2", defaultValue = "") String job2,
			@RequestParam(name = "job3", defaultValue = "") String job3,
			HttpSession session) {
		log.info("===== job1: {}, job2: {}, job3: {} =====", job1, job2, job3);

		String email = (String) session.getAttribute("email");
		service.editJob(email, job1, job2, job3);

		return "{ \"message\": \"good\" }";
	}

	//내 정보 수정
	@GetMapping("/member/mypage/editInfo")
	public void getMypageEditInfo(Model model, HttpSession session) {
		MemberDTO member = service.memberInfo((String) session.getAttribute("email"));
		model.addAttribute("member", member);
	}

	//비밀번호 변경(화면)
	@GetMapping("/member/mypage/editPassword")
	public void getMypageEditPassword(Model model, HttpSession session) {
		MemberDTO member = service.memberInfo((String) session.getAttribute("email"));
		model.addAttribute("member", member);
	}

	//비밀번호 변경(처리)
	@ResponseBody
	@PostMapping("/member/mypage/editPassword")
	public String postMypageEditPassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("password") String password, HttpSession session) throws Exception {
		String email = (String) session.getAttribute("email");

		//입력받은 기존 비밀번호가 실제 비밀번호와 일치하는지 확인
		if (!pwEncoder.matches(oldPassword, service.memberInfo(email).getPassword()))
			return "{ \"message\": \"PASSWORD_NOT_MATCH\" }";

		//기존 비밀번호와 신규 비밀번호가 동일할 경우 거부
		if (pwEncoder.matches(password, service.memberInfo(email).getPassword()))
			return "{ \"message\": \"SAME_PASSWORD_DENY\" }";

		//신규 비밀번호로 변경
		MemberDTO member = new MemberDTO();
		member.setEmail(email);
		member.setPassword(pwEncoder.encode(password));
		service.editPassword(member);
		service.lastdateUpdate(email, "password");

		return "{ \"message\": \"good\" }";
	}

	//회원탈퇴
	@ResponseBody
	@GetMapping("/member/mypage/deleteAccount")
	public String getDeleteAccount(HttpSession session) throws Exception {
		String email = (String) session.getAttribute("email");

		//본인이 방장인 채팅방이 존재할 경우 거부
		if (service.countChatManager(email) > 0)
			return "{ \"message\": \"EXIST_CHAT_MANAGER\" }";

		//회원 가입정보 삭제
		service.deleteAccount(email);

		//세션 삭제
		session.invalidate();

		return "{ \"message\": \"good\" }";
	}

	//나의 활동
	@GetMapping("/member/mypage/mychat")
	public void getMypageMychat() {

	}

	//내 문제집
	@GetMapping("/member/mypage/mytest")
	public void getMypageMytest() {

	}

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
		model.addAttribute("boardPageList", page.getPageList("/member/mypage/myboard", "boardPage", boardPageNum, postNum, pageListCount, boardTotalCount, ("&replyPage=" + replyPageNum)));
		model.addAttribute("replyPageList", page.getPageList("/member/mypage/myboard", "replyPage", replyPageNum, postNum, pageListCount, replyTotalCount, ("&boardPage=" + boardPageNum)));
	}

}