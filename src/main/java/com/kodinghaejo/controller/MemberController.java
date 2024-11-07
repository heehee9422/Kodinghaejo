package com.kodinghaejo.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.service.MailService;
import com.kodinghaejo.service.MemberService;
import com.kodinghaejo.util.PasswordMaker;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class MemberController {
	
	private final MemberService service;
	private final MailService mailService;
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
			@RequestParam(name = "imgUpload", required = false) MultipartFile mpf) throws Exception {
		//========== 운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작 ==========
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if (os.contains("win"))
			path = "C:\\Repository\\profile\\";
		else 
			path = "/home/user/Repository/profile/";
		
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
			member.setPassword(pwEncoder.encode(member.getPassword()));
			service.join(member);
		}
		
		//기본정보 수정
		if (kind.equals("U")) {
			//프로필 이미지 변경 시 기존 이미지 파일 삭제
			if (mpf != null && !mpf.isEmpty()) {
				MemberDTO before = service.memberInfo(member.getEmail());
				File file = new File(path + before.getStoredImg());
				file.delete();
			}
			
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

	
	@GetMapping("/member/mypagemain")
	public void getMypagemain() {
		
	}
	@GetMapping("/member/mypageMyboard")
	public void getMypageMyboard() {
		
	}
	@GetMapping("/member/mypageMychat")
	public void getMypageMychat() {
		
	}
	@GetMapping("/member/mypageMytest")
	public void getMypageMytest() {
		
	}

}