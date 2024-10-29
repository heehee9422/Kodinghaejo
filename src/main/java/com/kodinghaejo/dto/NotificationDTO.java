package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.NotificationEntity;

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
public class NotificationDTO {

	private Long idx;
	private MemberEntity email;
	private String type;
	private Long srcIdx;
	private String srcTitle;
	private String sender;
	private String orgSender;
	private String storedSender;
	private Long senderSize;
	private String orgSrc;
	private String storedSrc;
	private Long srcSize;
	private String isChk;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public NotificationDTO(NotificationEntity entity) {
		this.idx = entity.getIdx();
		this.email = entity.getEmail();
		this.type = entity.getType();
		this.srcIdx = entity.getSrcIdx();
		this.srcTitle = entity.getSrcTitle();
		this.sender = entity.getSender();
		this.orgSender = entity.getOrgSender();
		this.storedSender = entity.getStoredSender();
		this.senderSize = entity.getSenderSize();
		this.orgSrc = entity.getOrgSrc();
		this.storedSrc = entity.getStoredSrc();
		this.srcSize = entity.getSrcSize();
		this.isChk = entity.getIsChk();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public NotificationEntity dtoToEntity(NotificationDTO dto) {
		NotificationEntity entity = NotificationEntity
										.builder()
										.idx(dto.getIdx())
										.email(dto.getEmail())
										.type(dto.getType())
										.srcIdx(dto.getSrcIdx())
										.srcTitle(dto.getSrcTitle())
										.sender(dto.getSender())
										.orgSender(dto.getOrgSender())
										.storedSender(dto.getStoredSender())
										.senderSize(dto.getSenderSize())
										.orgSrc(dto.getOrgSrc())
										.storedSrc(dto.getStoredSrc())
										.srcSize(dto.getSrcSize())
										.isChk(dto.getIsChk())
										.regdate(dto.getRegdate())
										.isUse(dto.getIsUse())
										.build();

		return entity;
	}

}