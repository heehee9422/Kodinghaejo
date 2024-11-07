package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.ChatLogEntity;

public interface ChatLogRepository extends JpaRepository<ChatLogEntity, Long> {

}