package com.kodinghaejo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.kodinghaejo.service.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@Log4j2
public class WebSecurityConfig {

	private final AuthSuccessHandler authSuccessHandler;
	private final AuthFailureHandler authFailureHandler;
	private final UserDetailsServiceImpl userDetailsService;

	//스프링 시큐리티에서 암호화 관련 객체를 가져와 스프링빈으로 등록
	@Bean
	public BCryptPasswordEncoder pwEncoder() {
		return new BCryptPasswordEncoder();
	}

	//스프링 시큐리티 적용 제외 대상 설정 --> 스프링빈을 등록
	@Bean
	WebSecurityCustomizer webSecurityCustoimzer() {
		return (web) -> web.ignoring().requestMatchers("/img/**", "/css/**", "/js/**");
	}

	//스프링 시큐리티 로그인 화면 사용 비활성화, CSRF/CORS 공격 방어용 보안 설정 비활성화
	@Bean
	SecurityFilterChain filter(HttpSecurity http) throws Exception {
		//스프링 시큐리티의 FormLogin 설정
		http
			.formLogin((login) -> login
					.usernameParameter("email") //스프링 시큐리티에서 사용할 id 변수명 등록.
					.loginPage("/member/login") //스프링 시큐리티에서 사용할 로그인 페이지 등록
					.successHandler(authSuccessHandler) //로그인이 성공했을 때 처리할 명령문이 있는 Handler
					.failureHandler(authFailureHandler)); //로그인이 실패했을 때 처리할 명령문이 있는 Handler

		//스프링 시큐리티의 자동로그인 설정
		http
			.rememberMe((me) -> me
				.key("kodindhaejo") //스프링 시큐리티의 자동 로그인을 식별하는 고유키
				.alwaysRemember(false) //웹브라우저 종료한 후 실행시켜도 자동 로그인이 유지
				.tokenValiditySeconds(3600 * 24 * 7) //자동 로그인 유지 시간 설정. 단위는 초 단위 --> 7일간 유지
				.rememberMeParameter("remember-me") //자동 로그인을 위한 name 값
				.userDetailsService(userDetailsService) //스프링 시큐리티에서 로그인을 처리하는 프로그램
				.authenticationSuccessHandler(authSuccessHandler)); //자동 로그인이 성공했을 때 처리할 명령문이 있는 Handler

		//스프링 시큐리티의 접근권한 설정(Access Control)
		http
			.authorizeHttpRequests((authz) -> authz
//				.requestMatchers("/member/**").permitAll()
//				.requestMatchers("/board/**").hasAnyAuthority("USER", "MASTER")
//				.requestMatchers("/master/**").hasAnyAuthority("MASTER")
				.requestMatchers("/**").permitAll()
				.anyRequest().authenticated());

		//세션 설정
		http
			.sessionManagement((management) -> management
				.maximumSessions(1) //최대 접속자 수 설정
				.maxSessionsPreventsLogin(false) //최대 접속 한계가 넘은 상태에서 로그인 페이지를 보여줄지를 설정
				.expiredUrl("/")); //세션 종료되면 이동할 페이지

		//스프링 시큐리티의 로그아웃
		http
			.logout((logout) -> logout
				.logoutUrl("/member/logout") //로그아웃 시 이용할 URL
				.logoutSuccessUrl("/member/login") //로그아웃 성공 시 이동할 페이지
				.invalidateHttpSession(true) //로그아웃 시 세션 삭제
				.deleteCookies("JSESSIONID", "remember-me") //로그아웃 시 쿠키 삭제
				.permitAll()); //로그아웃 후의 접근 권한

		//CSRF, CORS 공격 방어를 위한 보안 설정 비활성화
		http
			.csrf((csrf) -> csrf.disable());
		http
			.cors((cors) -> cors.disable());
		
		log.info("==================== 스프링 시큐리티 설정 완료 ====================");
		
		return http.build();
	}

}