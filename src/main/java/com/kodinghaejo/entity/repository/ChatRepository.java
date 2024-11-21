package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.ChatEntity;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

	Page<ChatEntity> findByTitleContaining(String searchKeyword, Pageable pageable);

	List<ChatEntity> findChatsByLimit(int limit);

	@Override
	void deleteById(Long idx);

}