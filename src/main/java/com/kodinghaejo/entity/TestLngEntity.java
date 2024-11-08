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
@Entity(name = "testLng")
@Table(name = "jpa_test_lng")
public class TestLngEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_LANG_SEQ")
	@SequenceGenerator(name = "TEST_LANG_SEQ", sequenceName = "jpa_test_lang_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "test_idx", nullable = false)
	private TestEntity testIdx;

	@Column(name = "lng", length = 20, nullable = false)
	private String lng;

	@Column(name = "content", length = 2000, nullable = false)
	private String content;

	@Column(name = "correct", length = 2000, nullable = false)
	private String correct;

	@Column(name = "run_src", length = 2000, nullable = false)
	private String runSrc;
	
	@Column(name = "subm_src", length = 2000, nullable = false)
	private String submSrc;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}