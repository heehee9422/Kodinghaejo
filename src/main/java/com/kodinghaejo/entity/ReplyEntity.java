package com.kodinghaejo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
@Entity(name = "reply")
@Table(name = "jpa_reply")
public class ReplyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPLY_SEQ")
	@SequenceGenerator(name = "REPLY_SEQ", sequenceName = "jpa_reply_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "re_prnt", length = 10, nullable = false)
	private String rePrnt;

	@Column(name = "prnt_idx", nullable = false)
	private Long prntIdx;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "writer", length = 50, nullable = false)
	private String writer;

	@Column(name = "content", length = 200, nullable = false)
	private String content;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}