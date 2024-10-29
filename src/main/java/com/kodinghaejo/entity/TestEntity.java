package com.kodinghaejo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
public class TestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_SEQ")
	@SequenceGenerator(name = "TEST_SEQ", sequenceName = "jpa_test_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "title", length = 200, nullable = false)
	private String title;

	@Column(name = "diff", nullable = false)
	private int diff;

	@Column(name = "src", length = 20, nullable = false)
	private String src;

	@Column(name = "cat", length = 20, nullable = false)
	private String cat;

	@Column(name = "descr", length = 2000, nullable = false)
	private String descr;

	@Column(name = "restr", length = 200, nullable = false)
	private String restr;

	@Column(name = "ex_str", length = 200, nullable = false)
	private String exStr;

	@Column(name = "ex_descr", length = 2000, nullable = false)
	private String exDescr;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}