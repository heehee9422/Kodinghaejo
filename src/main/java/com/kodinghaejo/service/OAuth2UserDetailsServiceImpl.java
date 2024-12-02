package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.MemberOAuth2DTO;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class OAuth2UserDetailsServiceImpl extends DefaultOAuth2UserService {
	
	private final PasswordEncoder pwEncoder;
	private final MemberService service;
	private final MemberRepository memberRepository;
	private final HttpSession session;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		String provider = userRequest.getClientRegistration().getRegistrationId();
		String providerId = oAuth2User.getAttribute("sub");
		String email = null;
		String username = null;
		
		switch (provider) {
			case "google":
				email = oAuth2User.getAttribute("email");
				username = oAuth2User.getAttribute("name");
				break;
				
			case "naver":
				Map<String, Object> response = oAuth2User.getAttribute("response");
				email = (String) response.get("email");
				username = (String) response.get("name");
				break;
		}
		
		log.info("========== OAuth2 로그인 단계: Provider 정보({}) ==========", provider);
		log.info("========== OAuth2 로그인 단계: Provider ID 정보({}) ==========", providerId);
		log.info("========== OAuth2 로그인 단계: email 정보({}) ==========", email);
		log.info("========== OAuth2 로그인 단계: username 정보({}) ==========", username);
		
		oAuth2User.getAttributes().forEach((k, v) -> {
			log.info(k + ": " + v);
		});
		
		MemberEntity member = saveSocialMember(email, provider, username);

		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getLvl());
		grantedAuthorities.add(grantedAuthority);
		
		log.info("========== OAuth2 로그인 단계: role 설정({}) ==========", grantedAuthority.toString());
		
		MemberOAuth2DTO memberOAuth2DTO = new MemberOAuth2DTO();
		memberOAuth2DTO.setAttribute(oAuth2User.getAttributes());
		memberOAuth2DTO.setAuthorities(grantedAuthorities);
		memberOAuth2DTO.setName(member.getUsername());
		
		service.lastdateUpdate(email, "login");
		service.memberLogRegistry(email, "login");
		
		//닉네임 값이 없을 경우 이름으로 대체
		String nickname = (member.getNickname() != null && !member.getNickname().equals("")) ?
												member.getNickname() : member.getUsername();
		
		session.setAttribute("email", email);
		session.setAttribute("username", member.getUsername());
		session.setAttribute("nickname", nickname);
		session.setAttribute("lvl", member.getLvl());
		session.setAttribute("joinRoute", provider);
		session.setAttribute("storedImg", member.getStoredImg());
		session.setAttribute("emailAuth", member.getEmailAuth());

		log.info("========== OAuth2 로그인 단계 : 세션 설정 ==========");
		log.info("========== 세션 email: {} ==========", email);
		log.info("========== 세션 username: {} ==========", member.getUsername());
		log.info("========== 세션 nickname: {} ==========", nickname);
		log.info("========== 세션 lvl: {} ==========", member.getLvl());
		log.info("========== 세션 joinRoute: {} ==========", provider);
		log.info("========== 세션 storedImg: {} ==========", member.getStoredImg());
		
		return memberOAuth2DTO;
	}
	
	private MemberEntity saveSocialMember(String email, String provider, String username) {
		Optional<MemberEntity> result = memberRepository.findById(email);
		MemberEntity member;
		
		if (result.isPresent()) {
			member = result.get();
			
			if (member.getIsUse().equals("N"))
				service.restoreAccount(email);
		} else {
			member = MemberEntity
															.builder()
															.email(email)
															.emailAuth("Y")
															.username(username)
															.nickname(username)
															.password(pwEncoder.encode("0"))
															.tel("")
															.lvl("LVL-0002")
															.score(0L)
															.imgSize(0L)
															.regdate(LocalDateTime.now())
															.pwdate(LocalDateTime.now())
															.notifdate(LocalDateTime.now().plusDays(30))
															.scoredate(LocalDateTime.now())
															.joinRoute(provider)
															.isUse("Y")
															.build();
			memberRepository.save(member);
		}
		
		return member;
	}

}