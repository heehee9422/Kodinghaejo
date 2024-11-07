package com.kodinghaejo.entity;

import java.time.LocalDateTime;

import com.kodinghaejo.dto.MemberDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity(name = "member")
@Table(name = "jpa_member")
public class MemberEntity {

	@Id
	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "email_auth", length = 2, nullable = false)
	private String emailAuth;

	@Column(name = "username", length = 50, nullable = false)
	private String username;
	
	@Column(name = "nickname", length = 50, nullable = true)
	private String nickname;

	@Column(name = "password", length = 100, nullable = false)
	private String password;

	@Column(name = "tel", length = 20, nullable = false)
	private String tel;

	@Column(name = "website", length = 100, nullable = true)
	private String website;

	@Column(name = "descr", length = 2000, nullable = true)
	private String descr;

	@Column(name = "lvl", length = 20, nullable = false)
	private String lvl;

	@Column(name = "tec_1", length = 20, nullable = true)
	private String tec1;

	@Column(name = "tec_2", length = 20, nullable = true)
	private String tec2;

	@Column(name = "tec_3", length = 20, nullable = true)
	private String tec3;

	@Column(name = "job_1", length = 20, nullable = true)
	private String job1;

	@Column(name = "job_2", length = 20, nullable = true)
	private String job2;

	@Column(name = "job_3", length = 20, nullable = true)
	private String job3;

	@Column(name = "org_img", length = 200, nullable = true)
	private String orgImg;

	@Column(name = "stored_img", length = 200, nullable = true)
	private String storedImg;

	@Column(name = "img_size", nullable = true)
	private Long imgSize;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "logindate", nullable = true)
	private LocalDateTime logindate;

	@Column(name = "logoutdate", nullable = true)
	private LocalDateTime logoutdate;

	@Column(name = "pwdate", nullable = true)
	private LocalDateTime pwdate;
	
	@Column(name = "notifdate", nullable = true)
	private LocalDateTime notifdate;

	@Column(name = "join_route", length = 10, nullable = false)
	private String joinRoute;

	@Column(name = "acc_token", length = 1000, nullable = true)
	private String accToken;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;
	
	public void editInfo(MemberDTO member) {
		this.nickname = member.getNickname();
		this.tel = member.getTel();
		this.website = member.getWebsite();
		this.descr = member.getDescr();
		this.tec1 = member.getTec1();
		this.tec2 = member.getTec2();
		this.tec3 = member.getTec3();
		this.job1 = member.getJob1();
		this.job2 = member.getJob2();
		this.job3 = member.getJob3();
		this.orgImg = member.getOrgImg();
		this.storedImg = member.getStoredImg();
		this.imgSize = member.getImgSize();
	}

}