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
		//로그인 정보를 타기관 사이트에 요청하고, 그 요청을 타기관에서 처리한 후
		//응답 메시지를 보내주고, 그 메시지 정보가 OAuth2UserRequest에 저장됨.
		//그리고, 이 정보가 super.loadUser(userRequest)에 의해서
		//OAuthUser에 입력된다.
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String provider = userRequest.getClientRegistration().getRegistrationId();
		String providerId = oAuth2User.getAttribute("sub"); //OAuth2
		String email = null;
		String username = null;

		if (provider.equals("google")) {
			email = oAuth2User.getAttribute("email");
			
		} else if (provider.equals("naver")) {
			Map<String, Object> response = oAuth2User.getAttribute("response");
			email = (String) response.get("email");
		} else if (provider.equals("kakao")) {
			Map<String, Object> account = oAuth2User.getAttribute("kakao_account");
			email = (String) account.get("email");
		}

		log.info("------------------------ OAuth2 로그인 단계 : Provider 정보 확보({}) ------------------------", provider);
		log.info("------------------------ OAuth2 로그인 단계 : ProviderID 정보 확보({}) ------------------------", providerId);
		log.info("------------------------ OAuth2 로그인 단계 : email 정보 확보({}) ------------------------", email);
		log.info("------------------------ OAuth2 로그인 단계 : username 정보 확보({}) ------------------------", username);

		oAuth2User.getAttributes().forEach((k, v) -> {
			log.info(k + ": " + v);
		});
		
		//회원 정보를 가져옴
		MemberEntity member = joinSnsMember(email, username, provider);
		
		//Lvl 값 확인
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getLvl());
		grantedAuthorities.add(grantedAuthority);

		log.info("------------------------ OAuth2 로그인 단계 : Lvl 설정({}) ------------------------", grantedAuthority.toString());

		MemberOAuth2DTO memberOAuth2DTO = new MemberOAuth2DTO();
		//attributes, authorities, name을 MemberOAuth2DTO에 넣어 줌.
		memberOAuth2DTO.setAttribute(oAuth2User.getAttributes());
		memberOAuth2DTO.setAuthorities(grantedAuthorities);
		memberOAuth2DTO.setName(member.getUsername());

		//로그인 날짜 및 회원 로그 정보 등록
		service.lastdateUpdate(email, "login");
		service.memberLogRegistry(email, "login");

		//닉네임 값이 없을 경우 이름으로 대체
		String nickname = (member.getNickname() != null && !member.getNickname().equals("")) ?
							member.getNickname() : member.getUsername();

		//세션 생성
		session.setAttribute("email", email);
		session.setAttribute("username", member.getUsername());
		session.setAttribute("nickname", nickname);
		session.setAttribute("lvl", member.getLvl());
		session.setAttribute("joinRoute", member.getJoinRoute());
		
		log.info("------------------------ OAuth2 로그인 단계 : 세션 설정 ------------------------");
		log.info("------------------------ 세션 email: {} ------------------------", email);
		log.info("------------------------ 세션 username: {} ------------------------", member.getUsername());
		log.info("------------------------ 세션 nickname: {} ------------------------", nickname);
		log.info("------------------------ 세션 lvl: {} ------------------------", member.getLvl());
		log.info("------------------------ 세션 joinRoute: {} ------------------------", member.getJoinRoute());
		
		return memberOAuth2DTO;
	}
	
	private MemberEntity joinSnsMember(String email, String username, String provider) {
		//SNS로 간편 가입한 회원의 경우 사이트 운영에 필요한 최소한의 정보를
		//가공해서 jpa_member에 입력해야 함.

		//기존 회원이 SNS 로그인을 할 경우 기존 회원 정보를 리턴함
		Optional<MemberEntity> result = memberRepository.findByEmailAndIsUse(email, "Y");
		if (result.isPresent()) {
			return result.get();
		}
		
		MemberEntity member = MemberEntity
								.builder()
								.email(email)
								.emailAuth("N")
								.username(username)
								.password(pwEncoder.encode(provider))
								.lvl("LVL-0002")
								.regdate(LocalDateTime.now())
								.joinRoute(provider)
								.isUse("Y")
								.build();
		
		memberRepository.save(member);
		
		return member;
	}

}