package com.kodinghaejo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class MailService {

	private final JavaMailSender javaMailSender;
	
	public void sendSimpleMailMessage(String email, String password) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		try {
			simpleMailMessage.setTo(email); //메일 수신자 설정
			simpleMailMessage.setSubject("[코딩해조] 임시 비밀번호 발급 안내"); //메일 제목 설정

			//메일 내용 구성
			String content = "안녕하세요.\n"
					+ "[코딩해조] 임시 비밀번호 발급 안내 이메일입니다.\n\n"
					+ "회원님의 임시 비밀번호는 [" + password + "]입니다.\n"
					+ "로그인 후에 비밀번호를 변경해주세요.";
			
			//메일 내용 설정
			simpleMailMessage.setText(content);
			
			javaMailSender.send(simpleMailMessage);
			
			log.info("==================== 메일 발송 성공  ====================");
		} catch (Exception e) {
			log.info("==================== 메일 발송 실패  ====================");
			throw new RuntimeException(e);
		}
	}
	
	public void sendMimeMessage(String email, String password) {
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