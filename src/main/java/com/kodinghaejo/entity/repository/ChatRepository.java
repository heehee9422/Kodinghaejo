package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.ChatEntity;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

}