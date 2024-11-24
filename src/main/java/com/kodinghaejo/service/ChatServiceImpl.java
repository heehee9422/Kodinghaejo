package com.kodinghaejo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodinghaejo.dto.ChatDTO;
import com.kodinghaejo.dto.ChatLogDTO;
import com.kodinghaejo.dto.ChatMsgDTO;
import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatLogEntity;
import com.kodinghaejo.entity.ChatMemberEntity;
import com.kodinghaejo.entity.ChatMsgEntity;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.repository.ChatLogRepository;
import com.kodinghaejo.entity.repository.ChatMemberRepository;
import com.kodinghaejo.entity.repository.ChatMsgRepository;
import com.kodinghaejo.entity.repository.ChatRepository;
import com.kodinghaejo.entity.repository.MemberRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

	private Map<String, ChatDTO> chatRooms;
	private final ObjectMapper objectMapper;
	private final ChatRepository chatRepository;
	private final ChatLogRepository chatLogRepository;
	private final ChatMsgRepository chatMsgRepository;
	private final ChatMemberRepository chatMemberRepository;
	private final MemberRepository memberRepository;

	//chatRooms 초기화
	@PostConstruct
	private void init() {
		chatRooms = new LinkedHashMap<>();
	}

	//생성된 전체 대화방 정보 가져 오기
	@Override
	public List<ChatDTO> findAllRooms() {
		List<ChatDTO> chatDTOs = new ArrayList<>();
		chatRepository.findAll().stream().forEach((e) -> chatDTOs.add(new ChatDTO(e)));
		return chatDTOs;
	}

	//대화방 아이디로 특정 대화방 정보 가져 오기
	@Override
	public ChatDTO findRoomById(String idx) {
		return chatRooms.get(idx);
	}
	
	// 채팅방 인원수 가져오기
	@Override
	public int countMembers(Long chatIdx) {
		ChatEntity chatRoom = findRoomById(chatIdx);
		return chatMemberRepository.countByChatIdx(chatRoom);
	}

	//대화방 새로 생성
	@Override
	public Long createRoom(ChatDTO chatdto) {
		ChatEntity entity = ChatEntity
													.builder()
													.type(chatdto.getType())
													.title(chatdto.getTitle())
													.regdate(LocalDateTime.now())
													.isUse("Y")
													.password(chatdto.getPassword())
													.build();
		chatRepository.save(entity);

		return entity.getIdx();
	}
	
	// 유저목록에서 유저를 선택하고 1:1대화방 생성
	//대화방 새로 생성
	@Override
	public Long userinfo(ChatDTO chatdto) {
		ChatEntity entity = ChatEntity
													.builder()
													.type(chatdto.getType())
													.title(chatdto.getTitle())
													.limit(chatdto.getLimit())
													.regdate(LocalDateTime.now())
													.isUse("Y")
													.password(chatdto.getPassword())
													.descr(chatdto.getDescr())
													.build();
		chatRepository.save(entity);
	
		return entity.getIdx();
	}

	@Override
	public Long postMessage(ChatMsgDTO chatmsgdto, ChatEntity chatIdx) {
		ChatMsgEntity msgEntity = ChatMsgEntity
																.builder()
																.chatIdx(chatIdx)
																.email(chatmsgdto.getEmail())
																.content(chatmsgdto.getContent())
																.regdate(LocalDateTime.now())
																.isUse("Y")
																.build();
		chatMsgRepository.save(msgEntity);

		return msgEntity.getIdx();
	}

	@Override
	public Long createLog(ChatLogDTO chatlogdto, ChatEntity chatIdx) {
		ChatLogEntity logEntity = ChatLogEntity
																.builder()
																.chatIdx(chatIdx)
																.content(chatlogdto.getContent())
																.regdate(LocalDateTime.now())
																.isUse("Y")
																.build();
		chatLogRepository.save(logEntity);
		return logEntity.getIdx();
	}

	@Override
	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
			//session.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//모든 회원 정보 가져오기
	@Override
	public List<MemberDTO> getAllUsers() {
		List<MemberEntity> memberEntities = memberRepository.findAll();
		return memberEntities.stream().map(MemberDTO::new).collect(Collectors.toList());
	}

	// 특정 회원 정보 가져오기
	@Override
	public MemberDTO getUserByEmail(String email) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		return new MemberDTO(memberEntity);
	}

	//채팅방 찾는
	@Override
	public ChatEntity findRoomById(Long idx) {
		return chatRepository.findById(idx).orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
	}

	//채팅방에 접속을 하면 채팅멤버 테이블에 채팅멤버가 등록
	public void addUserToRoom(Long chatidx, String email, String username, String manager) {
		ChatEntity chatRoom = findRoomById(chatidx);
		MemberEntity chatEmail = memberRepository.findById(email).get();

		if (chatMemberRepository.existsByChatIdxAndEmail(chatRoom, chatEmail)) {
			return;// 채팅멤버가 이미 있으면 그냥종료
		} else if (manager != "Y") {
			manager = "N";} //createRoom()으로 방을 만들며 입장했으면 관리자로
		
			ChatMemberEntity chatmember = ChatMemberEntity
																			.builder()
																			.chatIdx(chatRoom)
																			.email(chatEmail) 
																			.nickname(username)
																			.manager(manager)
																			.regdate(LocalDateTime.now())
																			.build();
			chatMemberRepository.save(chatmember);
		
	}

	//메세지를 테이블에 저장하는 서비스
	@Override
	public void saveMessage(Long chatIdx, String email, String content) {

		ChatEntity chatRoom = findRoomById(chatIdx);
		MemberEntity member = memberRepository.findById(email).get();

		ChatMsgEntity message = ChatMsgEntity
															.builder()
															.chatIdx(chatRoom)
															.email(member.getEmail())
															.content(content)
															.regdate(LocalDateTime.now())
															.isUse("Y")
															.build();

		chatMsgRepository.save(message);

	}

	//로그를 저장하는 서비스
	@Override
	public void saveLog(Long chatIdx, String content) {
		ChatEntity chatRoom = findRoomById(chatIdx);

		ChatLogEntity log = ChatLogEntity.builder()
													.chatIdx(chatRoom)
													.content(content)
													.regdate(LocalDateTime.now())
													.isUse("Y")
													.build();

		chatLogRepository.save(log);
	}

	//채팅방 나갈때 채팅멤버 삭제
	@Transactional
	@Override
	public void removeUserFromRoom(Long chatIdx, String email) {
		ChatEntity chatRoom = findRoomById(chatIdx);
		MemberEntity member = memberRepository.findById(email).get();
		chatMemberRepository.deleteByChatIdxAndEmail(chatRoom, member);
	}

	//현재 채팅방 멤버 조회
	@Override
	public List<String> getChatMembers(Long chatIdx) {
		ChatEntity chatRoom = findRoomById(chatIdx);

		return chatMemberRepository.findByChatIdx(chatRoom).stream().map(ChatMemberEntity :: getNickname) //멤버의 닉네임 반환
				.collect(Collectors.toList());
	}

	@Override
	public List<Map<String, Object>> getChatMessagesWithUsername(Long chatIdx) {
		ChatEntity chatEntity = chatRepository.findById(chatIdx)
				.orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다. chatIdx: " + chatIdx));

		return chatMsgRepository.findByChatIdxOrderByRegdateAsc(chatEntity).stream().map((msg) -> {
			MemberEntity member = memberRepository.findById(msg.getEmail()).get();

			Map<String, Object> messageData = new HashMap<>();
			messageData.put("email", msg.getEmail());
			messageData.put("username", member.getUsername()); //username 추가
			messageData.put("content", msg.getContent());
			messageData.put("regdate", msg.getRegdate());
			return messageData;
		}).collect(Collectors.toList());
	}

}