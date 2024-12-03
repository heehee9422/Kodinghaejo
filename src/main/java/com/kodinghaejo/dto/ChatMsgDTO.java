package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatMsgEntity;

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
public class ChatMsgDTO {

	public enum MessageType {
		ENTER, TALK, CLOSE
	}

	private Long idx;
	private ChatEntity chatIdx;
	private String email;
	private String content;
	private LocalDateTime regdate;
	private String isUse;

	private MessageType type; //메시지 타입

	//Entity --> DTO 이동
	public ChatMsgDTO(ChatMsgEntity entity) {
		this.idx = entity.getIdx();
		this.chatIdx = entity.getChatIdx();
		this.email = entity.getEmail();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public ChatMsgEntity dtoToEntity(ChatMsgDTO dto) {
		ChatMsgEntity entity = ChatMsgEntity
														.builder()
														.idx(dto.getIdx())
														.chatIdx(dto.getChatIdx())
														.email(dto.getEmail())
														.content(dto.getContent())
														.regdate(dto.getRegdate())
														.isUse(dto.getIsUse())
														.build();

		return entity;
	}

}