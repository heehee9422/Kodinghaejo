package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.entity.FileEntity;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.repository.MemberRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;

	//회원가입
	@Override
	public void signup(MemberDTO member) {
		MemberEntity memberEntity = MemberEntity
										.builder()
										.email(member.getEmail())
										.emailAuth("N")
										.username(member.getUsername())
										.password(member.getPassword())
										.lvl("LVL-0001")
										.regdate(LocalDateTime.now())
										.pwdate(LocalDateTime.now())
										.joinRoute("EMAIL")
										.isUse("Y")
										.build();
		
		memberRepository.save(memberEntity);
	}
	
	//기본정보 수정
	@Override
	public void editInfo(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.editInfo(member);
		memberRepository.save(memberEntity);
	}
	
	//비밀번호 변경
	//비밀번호 찾기(임시비밀번호 발급)
	@Override
	public void editPassword(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setPassword(member.getPassword());
		memberEntity.setPwdate(LocalDateTime.now());
		memberRepository.save(memberEntity);
	}
	
	//이메일 중복 확인
	@Override
	public int checkEmail(String email) {
		return memberRepository.findById(email).isEmpty() ? 0 : 1;
	}
	
	//회원 기본정보
	@Override
	public MemberDTO memberInfo(String email) {
		return memberRepository.findById(email).map((member) -> new MemberDTO(member)).get();
	}
	
	//회원 로그인, 로그아웃, 패스워드변경 일자 등록(Update)
	@Override
	public void lastdateUpdate(String email, String status) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		switch (status) {
			case "login":
				memberEntity.setLogindate(LocalDateTime.now());
				break;
			case "logout":
				memberEntity.setLogoutdate(LocalDateTime.now());
				break;
			case "password":
				memberEntity.setPwdate(LocalDateTime.now());
				break;
		}
		
		memberRepository.save(memberEntity);
	}
	
	//회원 이메일(아이디) 찾기
	@Override
	public String searchEmail(MemberDTO member) {
		return null;
	}
	
	//비밀번호 변경일 연기(30일)
	@Override
	public void editPasswordAfter30(String email) {
		
	}
	
	//회원 첨부파일 목록
	@Override
	public List<FileEntity> getMemberFileList(String email) {
		return null;
	}
	
	//계정 삭제
	@Override
	public void deleteAccount(String email) {
		
	}	
	
}