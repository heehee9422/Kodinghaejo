package com.kodinghaejo;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final MemberService service;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		//authentication.getName() --> 로그인 시 입력받은 email 값을 가져온다.
		MemberDTO member = service.memberInfo(authentication.getName());
		
		//로그인 날짜 및 회원 로그 등록
		service.lastdateUpdate(member.getEmail(), "login");
		service.memberLogRegistry(member.getEmail(), "login");
		
		//닉네임 값이 없을 경우 이름으로 대체
		String nickname = (service.memberInfo(member.getEmail()).getNickname() != null &&
							!service.memberInfo(member.getEmail()).getNickname().equals("")) ?
								service.memberInfo(member.getEmail()).getNickname() :
								service.memberInfo(member.getEmail()).getUsername();
		
		//세션 생성
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(3600 * 24 * 7); //세션 유지 기간 설정
		session.setAttribute("email", service.memberInfo(member.getEmail()).getEmail());
		session.setAttribute("username", service.memberInfo(member.getEmail()).getUsername());
		session.setAttribute("nickname", nickname);
		session.setAttribute("lvl", service.memberInfo(member.getEmail()).getLvl());
		session.setAttribute("joinRoute", service.memberInfo(member.getEmail()).getJoinRoute());
		
		String url = "/";
		
		//비밀번호 변경 알림일자 도래 시
		if (member.getNotifdate().toString().compareTo(LocalDateTime.now().toString()) < 0)
			url = "/member/pwNotice";
		
		setDefaultTargetUrl(url);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
