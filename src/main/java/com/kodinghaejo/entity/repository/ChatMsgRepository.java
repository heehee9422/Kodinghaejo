package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.ChatMsgEntity;

public interface ChatMsgRepository extends JpaRepository<ChatMsgEntity, Long> {

}