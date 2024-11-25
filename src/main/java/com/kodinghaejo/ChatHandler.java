package com.kodinghaejo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodinghaejo.service.ChatService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

	private final ChatService chatService;

	//채팅방 ID(chatIdx)별로 세션을 관리하는 Map
	private static final Map<Long, List<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//URI에서 chatIdx 추출
		String uri = session.getUri().toString();
		Long chatIdx = extractChatIdx(uri);

		//해당 채팅방의 세션 리스트 가져오기
		chatRooms.computeIfAbsent(chatIdx, k -> new ArrayList<>()).add(session);

		log.info("사용자 접속: Session ID = {}, chatIdx = {}", session.getId(), chatIdx);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		//URI에서 chatIdx 추출
		String uri = session.getUri().toString();
		Long chatIdx = extractChatIdx(uri);

		String payload = message.getPayload();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {
		});

		String content = (String) data.get("message");
		String email = (String) data.get("email");

		System.out.println(email);
		System.out.println(content);

		if (email == null) {
			//chatService.saveLog(chatIdx,content);
		} else {
			//chatService.saveMessage(chatIdx,email,content);
		}

		//해당 채팅방에만 메시지 브로드캐스트
		List<WebSocketSession> sessions = chatRooms.get(chatIdx);
		if (sessions != null) {
			for (WebSocketSession s : sessions) {
				if (s.isOpen()) {
					s.sendMessage(message);
				}
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		String uri = session.getUri().toString();
		Long chatIdx = extractChatIdx(uri);

		List<WebSocketSession> sessions = chatRooms.get(chatIdx);
		if (sessions != null) {
			sessions.remove(session);
			log.info("사용자 연결 해제: Session ID = {}, chatIdx = {}", session.getId(), chatIdx);

			//채팅방에 세션이 없으면 Map에서 제거
			if (sessions.isEmpty()) {
				chatRooms.remove(chatIdx);
			}
		}
	}

	//URI에서 chatIdx를 추출하는 메서드
	private Long extractChatIdx(String uri) {
		try {
			String chatIdxParam = uri.split("chatIdx=")[1];
			return Long.parseLong(chatIdxParam);
		} catch (Exception e) {
			log.error("chatIdx 파싱 중 오류 발생: {}", uri);
			throw new IllegalArgumentException("Invalid URI format: chatIdx not found");
		}
	}
}
