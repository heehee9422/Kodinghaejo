package com.kodinghaejo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "boardRecommend")
@Table(name = "jpa_board_recommend")
@IdClass(BoardRecommendEntityId.class)
public class BoardRecommendEntity {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "email", nullable = false)
	private MemberEntity email;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "board_idx", nullable = false)
	private BoardEntity boardIdx;

	@Column(name = "good_chk", length = 2, nullable = true)
	private String goodChk;

	@Column(name = "bad_chk", length = 2, nullable = true)
	private String badChk;

	@Column(name = "good_date", nullable = true)
	private LocalDateTime goodDate;

	@Column(name = "bad_date", nullable = true)
	private LocalDateTime badDate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}