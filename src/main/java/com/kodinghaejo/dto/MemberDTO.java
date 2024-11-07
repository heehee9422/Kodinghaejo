package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

	private String email;
	private String emailAuth;
	private String username;
	private String nickname;
	private String password;
	private String tel;
	private String website;
	private String descr;
	private String lvl;
	private String tec1;
	private String tec2;
	private String tec3;
	private String job1;
	private String job2;
	private String job3;
	private String orgImg;
	private String storedImg;
	private Long imgSize;
	private LocalDateTime regdate;
	private LocalDateTime logindate;
	private LocalDateTime logoutdate;
	private LocalDateTime pwdate;
	private LocalDateTime notifdate;
	private String joinRoute;
	private String accToken;
	private String isUse;

	//Entity --> DTO 이동
	public MemberDTO(MemberEntity entity) {
		this.email = entity.getEmail();
		this.emailAuth = entity.getEmailAuth();
		this.username = entity.getUsername();
		this.nickname = entity.getNickname();
		this.password = entity.getPassword();
		this.tel = entity.getTel();
		this.website = entity.getWebsite();
		this.descr = entity.getDescr();
		this.lvl = entity.getLvl();
		this.tec1 = entity.getTec1();
		this.tec2 = entity.getTec2();
		this.tec3 = entity.getTec3();
		this.job1 = entity.getJob1();
		this.job2 = entity.getJob2();
		this.job3 = entity.getJob3();
		this.orgImg = entity.getOrgImg();
		this.storedImg = entity.getStoredImg();
		this.imgSize = entity.getImgSize();
		this.regdate = entity.getRegdate();
		this.logindate = entity.getLogindate();
		this.logoutdate = entity.getLogoutdate();
		this.pwdate = entity.getPwdate();
		this.notifdate = entity.getNotifdate();
		this.joinRoute = entity.getJoinRoute();
		this.accToken = entity.getAccToken();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public MemberEntity dtoToEntity(MemberDTO dto) {
		MemberEntity entity = MemberEntity
								.builder()
								.email(dto.getEmail())
								.username(dto.getUsername())
								.nickname(dto.getNickname())
								.password(dto.getPassword())
								.tel(dto.getTel())
								.website(dto.getWebsite())
								.descr(dto.getDescr())
								.lvl(dto.getLvl())
								.tec1(dto.getTec1())
								.tec2(dto.getTec2())
								.tec3(dto.getTec3())
								.job1(dto.getJob1())
								.job2(dto.getJob2())
								.job3(dto.getJob3())
								.orgImg(dto.getOrgImg())
								.storedImg(dto.getStoredImg())
								.imgSize(dto.getImgSize())
								.regdate(dto.getRegdate())
								.logindate(dto.getLogindate())
								.logoutdate(dto.getLogoutdate())
								.pwdate(dto.getPwdate())
								.notifdate(dto.getNotifdate())
								.joinRoute(dto.getJoinRoute())
								.accToken(dto.getAccToken())
								.accToken(dto.getAccToken())
								.build();

		return entity;
	}

}