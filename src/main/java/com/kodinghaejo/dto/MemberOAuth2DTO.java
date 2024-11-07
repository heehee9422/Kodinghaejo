package com.kodinghaejo.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Setter;

@Setter
public class MemberOAuth2DTO implements OAuth2User {
	//OAuth2UserDetailsServiceImpl에서 setter로 입력받은 값을 OAuth2User가 읽어옴.

	private Map<String, Object> attribute;
	private Collection<? extends GrantedAuthority> authorities;
	private String name;

	@Override
	public Map<String, Object> getAttributes() {
		return this.attribute;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getName() {
		return this.name;
	}

}