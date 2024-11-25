package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatMemberEntity;
import com.kodinghaejo.entity.ChatMemberEntityID;
import com.kodinghaejo.entity.MemberEntity;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, ChatMemberEntityID> {

	//채팅방 멤버 삭제
	void deleteByChatIdxAndEmail(ChatEntity chatRoom, MemberEntity email);

	//채팅방에 멤버가 존재하는지 조회
	boolean existsByChatIdxAndEmail(ChatEntity chatRoom, MemberEntity chatEmail);

	//현재 채팅방 멤버 조회
	 List<ChatMemberEntity> findByChatIdx(ChatEntity chatRoom);

	//사용자 이메일로 방장 역할을 맡고 있는 채팅방 갯수 확인
	public Long countByEmailAndManager(MemberEntity email, String manager);

	//사용자 이메일로 현재 참여 중인 채팅방 모두 확인
	public List<ChatMemberEntity> findByEmail(MemberEntity email);

	// chatIdx 값을 가진 chatmember의 갯수를 반환
	int countByChatIdx(ChatEntity chatRoom);

}