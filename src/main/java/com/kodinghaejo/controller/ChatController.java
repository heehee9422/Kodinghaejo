package com.kodinghaejo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class ChatController {

	/*
	 1. 웹 소켓은 HTML5를 지원하는 웹브라우저에서 작동 
	 2. 실시간 웹 애플리케이션을 위해 설계된 통신 프로토콜이며, TCP(Transmission Control Protocol)를 기반으로 함
	 3. HTTP와 다르게 클라이언트와 서버 간에 최초 연결이 이루어지면, 이 연결을 통해 양방향 통신(Full Duplex)을 지속적으로 유지할수 있음	  
	 */
	
//	private final ChatService chatService;
	
	@GetMapping("/chat/chatmain")
	public void getChatmain() { }
	
	@GetMapping("/chat")
	public void getChat(Model model) throws Exception {
		
//		model.addAttribute("list", chatService.findAllRooms());		
//		log.info("--------------------- 채팅 시작 ---------------------");		
//	}
//	
//	@ResponseBody
//	@PostMapping("/chat")
//	public String postChat(@RequestParam("roomName") String roomName) {
//		ChatRoomDTO chatRoomDTO = chatService.createRoom(roomName);
//		log.info("--------------------- 새로 생성된 roomId : {} ---------------------",chatRoomDTO.getRoomId());
//		return "{\"message\":\"good\",\"roomId\":\"" + chatRoomDTO.getRoomId() + "\"}";
	}
	
}
