package com.kodinghaejo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "board")
@Table(name = "jpa_board")
public class BoardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ")
	@SequenceGenerator(name = "BOARD_SEQ", sequenceName = "jpa_board_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "cat", length = 20, nullable = false)
	private String cat;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "email", nullable = false)
	private MemberEntity email;

	@Column(name = "writer", length = 50, nullable = false)
	private String writer;

	@Column(name = "title", length = 200, nullable = false)
	private String title;

	@Column(name = "content", length = 2000, nullable = false)
	private String content;
	
	@Column(name = "hit_cnt", nullable = true)
	private int hitCnt;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}