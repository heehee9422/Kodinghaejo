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
@Entity(name = "banner")
@Table(name = "jpa_banner")
public class BannerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANNER_SEQ")
	@SequenceGenerator(name = "BANNER_SEQ", sequenceName = "jpa_banner_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "img", length = 500, nullable = false)
	private String img;

	@Column(name = "url", length = 500, nullable = false)
	private String url;

	@Column(name = "description", length = 1000, nullable = true)
	private String desc;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}