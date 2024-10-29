package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

}