package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.entity.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

	List<ReplyInterface> findByPrntIdxAndIsUse(Long prntIdx, String isUse);

	List<ReplyEntity> findByPrntIdx(Long prntIdx);
	
}