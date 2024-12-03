package com.kodinghaejo.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.kodinghaejo.dto.ChatDTO;
import com.kodinghaejo.dto.ChatLogDTO;
import com.kodinghaejo.dto.ChatMsgDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatMemberEntity;

import jakarta.transaction.Transactional;

public interface ChatService {

	//생성된 전체 대화방 정보 가져 오기
	public List<ChatDTO> findAllRooms();

	//대화방 아이디로 특정 대화방 정보 가져 오기
	public ChatDTO findRoomById(String idx);

	public int countMembers(Long chatIdx);

	//대화방 새로 생성
	public Long createRoom(ChatDTO chatdto);

//	 public ChatEntity createMemberList(ChatMemberDTO chatmemberdto, ChatEntity chatIdx);

	public Long postMessage(ChatMsgDTO chatmsgdto, ChatEntity chatIdx);

	public Long createLog(ChatLogDTO chatlogdto, ChatEntity chatIdx);

	public <T> void sendMessage(WebSocketSession session, T message);

	//모든 회원 정보 가져오기
	public List<MemberDTO> getAllUsers();

	//특정 회원 정보 가져오기
	public MemberDTO getUserByEmail(String email);

	//채팅방 찾는
	public ChatEntity findRoomById(Long idx);

	//채팅방에 접속을 하면 채팅멤버 테이블에 채팅멤버가 등록
	public boolean addUserToRoom(Long chatidx, String email, String username, String manager);

	//메세지를 테이블에 저장하는 서비스
	public void saveMessage(Long chatIdx, String email, String content);

	//로그를 저장하는 서비스
	public void saveLog(Long chatIdx, String content);

	//채팅방 나갈때 채팅멤버 삭제
	@Transactional
	public void removeUserFromRoom(Long chatIdx, String email);

	//현재 채팅방 멤버 조회
	public List<String> getChatMembers(Long chatIdx);

	public List<Map<String, Object>> getChatMessagesWithUsername(Long chatIdx);
//채팅방 멤버에 포함되어있는지 체크
	public boolean isUserInRoom(Long chatIdx, String email);
	
	// 1:1 채팅방
	public boolean isPrivateChatRoomExists(String userEmail, String memberEmail);
	public Long getPrivateChatRoomIdx(String userEmail, String memberEmail);
	
	public ChatMemberEntity getmanager(Long chatIdx);
	
	public List<ChatDTO> findManagers();

}