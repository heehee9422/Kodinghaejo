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
@Entity(name = "chat")
@Table(name = "jpa_chat")
public class ChatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHAT_SEQ")
	@SequenceGenerator(name = "CHAT_SEQ", sequenceName = "jpa_chat_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "type", length = 10, nullable = false)
	private String type;

	@Column(name = "password", length = 100, nullable = true)
	private String password;

	@Column(name = "title", length = 200, nullable = false)
	private String title;

	@Column(name = "descr", length = 2000, nullable = true)
	private String descr;

	@Column(name = "limit", nullable = true)
	private int limit;

	@Column(name = "org_img", length = 200, nullable = true)
	private String orgImg;

	@Column(name = "stored_img", length = 200, nullable = true)
	private String storedImg;

	@Column(name = "img_size", nullable = true)
	private Long imgSize;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}