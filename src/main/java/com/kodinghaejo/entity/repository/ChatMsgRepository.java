package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.ChatEntity;
import com.kodinghaejo.entity.ChatMsgEntity;

public interface ChatMsgRepository extends JpaRepository<ChatMsgEntity, Long> {

	List<ChatMsgEntity> findByChatIdxOrderByRegdateAsc(ChatEntity chatIdx);

}