package com.kodinghaejo.entity;

import java.time.LocalDateTime;

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

	@Column(name = "password", length = 20, nullable = false)
	private String password;

	@Column(name = "tel", length = 20, nullable = false)
	private String tel;

	@Column(name = "website", length = 100, nullable = true)
	private String website;

	@Column(name = "descr", length = 2000, nullable = true)
	private String descr;

	@Column(name = "mem_lvl", length = 20, nullable = false)
	private String memLvl;

	@Column(name = "tech_1", length = 20, nullable = true)
	private String tech1;

	@Column(name = "tech_2", length = 20, nullable = true)
	private String tech2;

	@Column(name = "tech_3", length = 20, nullable = true)
	private String tech3;

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

	@Column(name = "join_route", length = 10, nullable = false)
	private String joinRoute;

	@Column(name = "acc_token", length = 1000, nullable = true)
	private String accToken;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}