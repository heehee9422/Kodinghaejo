package com.kodinghaejo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodinghaejo.dto.ChatDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatMemberEntity;
import com.kodinghaejo.service.ChatService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class ChatController {

	/*
	 * 1. 웹 소켓은 HTML5를 지원하는 웹브라우저에서 작동 2. 실시간 웹 애플리케이션을 위해 설계된 통신 프로토콜이며,
	 * TCP(Transmission Control Protocol)를 기반으로 함 3. HTTP와 다르게 클라이언트와 서버 간에 최초 연결이
	 * 이루어지면, 이 연결을 통해 양방향 통신(Full Duplex)을 지속적으로 유지할수 있음
	 */

	private final ChatService chatService;

	// 채팅방 목록
	@GetMapping("/chat/chatmain")
	public void getChat(Model model, HttpSession session) {
		// 로그인중인 유저 정보 조회
		String email = (String) session.getAttribute("email");
		String username = (String) session.getAttribute("username");
		model.addAttribute("email", email);
		model.addAttribute("username", username);
		
		
		// 채팅방 목록 로그
		log.info("--------------------- 채팅방 목록 ---------------------");

		List<ChatDTO> chatRooms = chatService.findManagers();
		
		model.addAttribute("chatRooms", chatRooms);
		

		// 채팅방에 참여자 수를 출력
		Map<Long, Integer> memberCounts = new HashMap<>();
		for (ChatDTO chatRoom : chatRooms) {
			int count = chatService.countMembers(chatRoom.getIdx());
			memberCounts.put(chatRoom.getIdx(), count);

		}
		model.addAttribute("memberCounts", memberCounts);
		
		log.info("--------------------- 유저 목록 ---------------------");
		model.addAttribute("members", chatService.getAllUsers());
	}

	// 채팅방 참여여부 확인
	@ResponseBody
	@GetMapping("/chat/checkMember")
	public boolean checkMember(@RequestParam("chatIdx") Long catIdx, HttpSession session) {
		String email = (String) session.getAttribute("email");
		return chatService.isUserInRoom(catIdx, email);
	}

	//채팅방 생성 페이지
	@GetMapping("/chat/create")
	public void createpage() {
	}

	//채팅방 생성
	@ResponseBody
	@PostMapping("/chat/create")
	public String createRoom(ChatDTO chat, Model model, HttpSession session) {
		Long chatIdx = chatService.createRoom(chat);
		//채팅방 생성 로그
		log.info("--------------------- 새로 생성된 idx : {} ---------------------", chatIdx);

		//현재 로그인 중인 유저의 정보를 가져오기
		String email = (String) session.getAttribute("email");
		String username = (String) session.getAttribute("username");
		String manager = "Y"; //채팅방 생성한 사람에게 관리자권한 부여

		//로그인중인 유저 정보를 추가
		//테스트로 username를 사용했는데 nickname로 바꿔야함
		chatService.addUserToRoom(chatIdx, email, username, manager);
		model.addAttribute("username", username);

		return "{\"message\":\"good\",\"chatidx\":\"" + chatIdx + "\"}";
	}

	//채팅방
	@GetMapping("/chat/chatview")
	public String chatpage(@RequestParam("idx") Long chatidx, Model model, HttpSession session) {

		ChatEntity chatRoom = chatService.findRoomById(chatidx);
		String username = (String) session.getAttribute("username");
		String email = (String) session.getAttribute("email");
		String manager = "";//manager 추가함
		
		ChatMemberEntity managerMember = chatService.getmanager(chatidx);
		String managerEmail = "";
		if (managerMember != null && managerMember.getEmail() != null) {
			managerEmail = managerMember.getEmail().getEmail();
		} else {
			// managerMember가 null인 경우 처리 (필요 시 로그 추가)
			managerEmail = null; // 기본값 설정
		}
		// 유저가 이미 채팅방에 등록되어 있는지 확인
		boolean isUserInRoom = chatService.isUserInRoom(chatidx, email);
		if (!isUserInRoom) {
			// 채팅방에 들어가면 채팅멤버 테이블에 회원 추가
			boolean userAdd = chatService.addUserToRoom(chatidx, email, username, manager);
			if (!userAdd) {
				// 채팅방이 꽉 찼을 경우
				model.addAttribute("error", "채팅방이 꽉 찼습니다.");
				return "redirect:/chat/chatmain";
			}
		}

		// //채팅방에 들어가면 채팅멤버 테이블에 회원 추가
		// chatService.addUserToRoom(chatidx, email, username, manager);//manager 추가함
		
		model.addAttribute("chatRoom", chatRoom);
		model.addAttribute("managerEmail", managerEmail);
		model.addAttribute("username", username);
		model.addAttribute("chatIdx", chatidx);
		return "chat/chatview";
	}

	//유저 정보 열람
	@GetMapping("/chat/userinfo")
	public String userInfo(@RequestParam("email") String email, Model model) {
		MemberDTO member = chatService.getUserByEmail(email);
		log.info("Member: {}", member);
		model.addAttribute("member", member);
		return "chat/userinfo";
	}
	
	// 선택한 유저와 1:1 대화방을 만든 적이 있는지 확인
	

	// 유저 정보를 갖고와서 1:1 채팅방 생성
	

	//채팅방 나갈때 채팅멤버 삭제
	@PostMapping("/chat/chatleave")
	public ResponseEntity<String> leaveChatRoom(@RequestParam Long chatIdx, @RequestParam String email) {

		System.out.println(chatIdx);
		System.out.println(email);
		chatService.removeUserFromRoom(chatIdx, email);
		return ResponseEntity.ok("채팅방에서 나갔습니다.");
	}

	//현재 채팅방 멤버 조회
	@GetMapping("/chat/members")
	public ResponseEntity<List<String>> getChatMembers(@RequestParam Long chatIdx) {
		List<String> members = chatService.getChatMembers(chatIdx);
		return ResponseEntity.ok(members);
	}

	//채팅메세지 로그 조회
	@GetMapping("chat/messagelogs")
	public ResponseEntity<List<Map<String, Object>>> getChatLogs(@RequestParam Long chatIdx) {
		List<Map<String, Object>> messages = chatService.getChatMessagesWithUsername(chatIdx);
		return ResponseEntity.ok(messages);
	}
	
//	// 1:1 채팅방
//	@ResponseBody
//	@PostMapping("/chat/checkOrCreatePrivateRoom")
//	public Map<String, Object> checkOrCreatePrivateRoom(@RequestParam("memberEmail") String memberEmail, HttpSession session) {
//		String userEmail = (String) session.getAttribute("email");
//		String userNickname = (String) session.getAttribute("username");
//		MemberDTO member = chatService.getUserByEmail(memberEmail);
//		String memberNickname = member.getNickname();
//
//		Map<String, Object> response = new HashMap<>();
//		if (chatService.isPrivateChatRoomExists(userEmail, memberEmail)) {
//			// 이미 1:1 대화방이 있는 경우
//			response.put("exists", true);
//			response.put("chatIdx", chatService.getPrivateChatRoomIdx(userEmail, memberEmail));
//		} else {
//			// 1:1 대화방이 없는 경우
//			response.put("exists", false);
//			String roomTitle = userNickname + "님과 " + memberNickname + "님의 채팅방";
//			String roomDescr = userNickname + "님과 " + memberNickname + "님의 채팅방";
//			ChatDTO chatDTO = ChatDTO.builder()
//					.type("OWN")
//					.title(roomTitle)
//					.descr(roomDescr)
//					.limit(2)
//					.build();
//			Long chatIdx = chatService.createRoom(chatDTO);
//			chatService.addUserToRoom(chatIdx, userEmail, userNickname, "Y");
//			chatService.addUserToRoom(chatIdx, memberEmail, memberNickname, "Y");
//			response.put("chatIdx", chatIdx);
//		}
//		return response;
//	}
	
	// 비밀번호 확인
	@ResponseBody
	@PostMapping("/chat/checkPassword")
	public Map<String, Object> checkPassword(@RequestParam("chatIdx") Long chatIdx, @RequestParam("password") String password) {
		ChatEntity chatRoom = chatService.findRoomById(chatIdx);
		Map<String, Object> response = new HashMap<>();
		if (chatRoom.getPassword().equals(password)) {
			response.put("valid", true);
		} else {
			response.put("valid", false);
		}
		return response;
	}

	/*
	//이부분 수정중 <
	
	@ResponseBody
	
	@PostMapping("/chat/chatview") public String postChat(ChatMsgDTO chatmsg) {
	Long chatIdx = chatService.postMessage(chatmsg, chatIdx); //채팅내역 로그
	log.info("--------------------- 작성된 채팅 idx : {} ---------------------"
	,chatMsgIdx); return "{\"message\":\"good\",\"idx\":\"\"" + chatMsgIdx +
	"\"}"; }
	
	@GetMapping("/chat/chat") public void chat0(Model model) { //채팅 시작 로그
	log.info("--------------------- 채팅 시작 ---------------------");
	model.addAttribute("list", chatService.findAllRooms()); }
	
	@ResponseBody
	
	@PostMapping("/chat/chat") public String chat1(@RequestParam("title") String
	title) { ChatDTO chatDTO = chatService.createRoom(title);
	log.info("--------------------- 새로 생성된 idx : {} ---------------------"
	,chatDTO.getIdx()); return "{\"message\":\"good\",\"idx\":\"" +
	chatDTO.getIdx() + "\"}"; }
	 */

}