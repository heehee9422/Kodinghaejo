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
@Entity(name = "chatMsg")
@Table(name = "jpa_chat_msg")
public class ChatMsgEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHAT_MSG_SEQ")
	@SequenceGenerator(name = "CHAT_MSG_SEQ", sequenceName = "jpa_chat_msg_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "chat_idx", nullable = false)
	private ChatEntity chatIdx;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "content", length = 2000, nullable = false)
	private String content;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}