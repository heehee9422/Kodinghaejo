package com.kodinghaejo.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.service.ChatService;

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
public class ChatDTO {

	private Long idx;
	private String type;
	private String password;
	private String title;
	private String descr;
	private int limit;
	private String orgImg;
	private String storedImg;
	private Long imgSize;
	private LocalDateTime regdate;
	private String isUse;
	private final Set<WebSocketSession> sessions = new HashSet<>();

	//Entity --> DTO 이동
	public ChatDTO(ChatEntity entity) {
		this.idx = entity.getIdx();
		this.type = entity.getType();
		this.password = entity.getPassword();
		this.title = entity.getTitle();
		this.descr = entity.getDescr();
		this.limit = entity.getLimit();
		this.orgImg = entity.getOrgImg();
		this.storedImg = entity.getStoredImg();
		this.imgSize = entity.getImgSize();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public ChatEntity dtoToEntity(ChatDTO dto) {
		ChatEntity entity = ChatEntity
													.builder()
													.idx(dto.getIdx())
													.type(dto.getType())
													.password(dto.getPassword())
													.title(dto.getTitle())
													.descr(dto.getDescr())
													.limit(dto.getLimit())
													.orgImg(dto.getOrgImg())
													.storedImg(dto.getStoredImg())
													.imgSize(dto.getImgSize())
													.regdate(dto.getRegdate())
													.isUse(dto.getIsUse())
													.build();

		return entity;
	}
	
	public void handleActions(WebSocketSession session, ChatMsgDTO chatMessage, ChatService chatService) {
		if (chatMessage.getType().equals(ChatMsgDTO.MessageType.ENTER)) {
			sessions.add(session);
			chatMessage.setContent(chatMessage.getEmail() + "님이 입장했습니다.");
		}
		sendMessage(chatMessage, chatService);
	}

	public <T> void sendMessage(T message, ChatService chatService) {
		sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
	}

}