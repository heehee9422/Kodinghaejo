package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.ChatMemberEntity;
import com.kodinghaejo.entity.ChatMemberEntityId;
import com.kodinghaejo.entity.MemberEntity;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, ChatMemberEntityId> {

	//사용자 이메일로 방장 역할을 맡고 있는 채팅방 갯수 확인
	public Long countByEmailAndManager(MemberEntity email, String manager);
	
	//사용자 이메일로 현재 참여 중인 채팅방 모두 확인
	public List<ChatMemberEntity> findByEmail(MemberEntity email);

}