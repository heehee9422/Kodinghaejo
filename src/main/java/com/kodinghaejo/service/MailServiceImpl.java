package com.kodinghaejo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kodinghaejo.entity.repository.MemberRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class MailServiceImpl implements MailService {

	private final JavaMailSender javaMailSender;
	private final MemberRepository memberRepository;

	@Override
	public void sendSimpleMailMessage(String type, String email, String value) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		String title; //메일 제목
		String content; //메일 내용
		String username = memberRepository.findById(email).orElse(null).getUsername(); //사용자 이름
		
		switch (type) {
			case "findPassword":
				title = "[코딩해조] 임시 비밀번호 발급 안내";
				content = "안녕하세요. " + username + "님.\n" +
									"[코딩해조] 임시 비밀번호 발급 안내 이메일입니다.\n\n" +
									"회원님의 임시 비밀번호는 [" + value + "]입니다.\n" +
									"로그인 후에 비밀번호를 변경해주세요.";
				break;
				
			case "emailAuth":
				title = "[코딩해조] 이메일 인증 안내";
				content = "안녕하세요. " + username + "님.\n" +
									"[코딩해조] 이메일 인증 안내 이메일입니다.\n\n" +
									"인증번호 [" + value + "]\n\n" +
									"해당 번호를 인증화면에 입력해주세요.";
				break;
				
			default:
				return;
		}

		try {
			simpleMailMessage.setTo(email); //메일 수신자 설정
			simpleMailMessage.setSubject(title);
			simpleMailMessage.setText(content);

			javaMailSender.send(simpleMailMessage);

			log.info("==================== 메일 발송 성공  ====================");
		} catch (Exception e) {
			log.info("==================== 메일 발송 실패  ====================");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendMimeMessage(String type, String email, String value) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

			mimeMessageHelper.setTo(email); //메일 수신자 설정
			mimeMessageHelper.setSubject(""); //메일 제목 설정

			//메일 내용 구성(HTML)
			String content = "";

			mimeMessageHelper.setText(content, true); //메일 내용 설정

			javaMailSender.send(mimeMessage);

			log.info("==================== 메일 발송 성공  ====================");
		} catch (Exception e) {
			log.info("==================== 메일 발송 성공  ====================");
			throw new RuntimeException(e);
		}
	}

}