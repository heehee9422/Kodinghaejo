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
@Entity(name = "testQuestion")
@Table(name = "jpa_test_question")
public class TestQuestionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_QUESTION_SEQ")
	@SequenceGenerator(name = "TEST_QUESTION_SEQ", sequenceName = "jpa_test_question_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "test_idx", nullable = false)
	private TestEntity testIdx;

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

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}