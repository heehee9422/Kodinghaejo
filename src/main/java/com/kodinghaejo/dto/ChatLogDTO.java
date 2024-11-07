package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatLogEntity;

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
public class ChatLogDTO {

	private Long idx;
	private ChatEntity chatIdx;
	private String content;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public ChatLogDTO(ChatLogEntity entity) {
		this.idx = entity.getIdx();
		this.chatIdx = entity.getChatIdx();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public ChatLogEntity dtoToEntity(ChatLogDTO dto) {
		ChatLogEntity entity = ChatLogEntity
								.builder()
								.idx(dto.getIdx())
								.chatIdx(dto.getChatIdx())
								.content(dto.getContent())
								.regdate(dto.getRegdate())
								.isUse(dto.getIsUse())
								.build();

		return entity;
	}

}