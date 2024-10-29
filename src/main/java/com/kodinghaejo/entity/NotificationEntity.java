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
@Entity(name = "notification")
@Table(name = "jpa_notification")
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQ")
	@SequenceGenerator(name = "NOTIFICATION_SEQ", sequenceName = "jpa_notification_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "email", nullable = false)
	private MemberEntity email;

	@Column(name = "type", length = 2, nullable = false)
	private String type;

	@Column(name = "src_idx", nullable = false)
	private Long srcIdx;

	@Column(name = "src_title", length = 200, nullable = false)
	private String srcTitle;

	@Column(name = "sender", length = 50, nullable = false)
	private String sender;

	@Column(name = "org_sender", length = 200, nullable = true)
	private String orgSender;

	@Column(name = "stored_sender", length = 200, nullable = true)
	private String storedSender;

	@Column(name = "sender_size", nullable = true)
	private Long senderSize;

	@Column(name = "org_src", length = 200, nullable = true)
	private String orgSrc;

	@Column(name = "stored_src", length = 200, nullable = true)
	private String storedSrc;

	@Column(name = "src_size", nullable = true)
	private Long srcSize;

	@Column(name = "is_chk", length = 2, nullable = false)
	private String isChk;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}