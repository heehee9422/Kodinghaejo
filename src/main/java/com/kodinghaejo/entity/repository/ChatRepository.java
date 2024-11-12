package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.ChatEntity;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
	
	List<ChatEntity> findByTitleContaining(String searchKeyword);
	
	List<ChatEntity> findChatsByLimit(int limit);
	
	void deleteById(Long idx);

}