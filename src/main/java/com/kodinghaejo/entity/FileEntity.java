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
@Entity(name = "file")
@Table(name = "jpa_file")
public class FileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_SEQ")
	@SequenceGenerator(name = "FILE_SEQ", sequenceName = "jpa_file_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "idx", nullable = false)
	private Long idx;

	@Column(name = "file_prnt", length = 10, nullable = false)
	private String filePrnt;

	@Column(name = "prnt_idx", nullable = false)
	private Long prntIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "email")
	private MemberEntity email;

	@Column(name = "type", length = 10, nullable = false)
	private String type;

	@Column(name = "org_file", length = 200, nullable = false)
	private String orgFile;

	@Column(name = "stored_file", length = 200, nullable = false)
	private String storedFile;

	@Column(name = "file_size", nullable = false)
	private Long fileSize;

	@Column(name = "regdate", nullable = false)
	private LocalDateTime regdate;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}