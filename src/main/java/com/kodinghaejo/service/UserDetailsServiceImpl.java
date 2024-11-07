package com.kodinghaejo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.MemberDTO;

import lombok.AllArgsConstructor;

//스프링 시큐리티가 활성화되어 있으면 스프링이 시작될 때 자동으로 호출되어 작동
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberService service;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//username은 스프링 시큐리티가 필터로 작동하면서 로그인 요청 과정에서 가져온 email
		MemberDTO member = service.memberInfo(username);
		
		if (member == null)
			throw new UsernameNotFoundException("해당 이메일로 가입한 계정이 존재하지 않습니다.");
		
		//SimpleGrantedAuthority : 여러 개의 사용자 Role 값을 받는 객체
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getLvl());
		grantedAuthorities.add(grantedAuthority);

		//User : 스프링 시큐리티에서 사용자 정보를 관리하는 최상위 객체. UserDetails는 User를 상속받은 자식 클래스
		User user = new User(username, member.getPassword(), grantedAuthorities);
		
		return user;
	}
	
}
