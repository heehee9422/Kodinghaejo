package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.entity.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

	public List<ReplyInterface> findByPrntIdxAndIsUse(Long prntIdx, String isUse);

	public List<ReplyEntity> findByPrntIdx(Long prntIdx);

	@Query("SELECT COUNT(r) FROM reply r WHERE r.prntIdx = :prntIdx AND r.isUse = 'Y'")
	int countRepliesByPostId(@Param("prntIdx") Long prntIdx);
	
	List<ReplyEntity> findByContentContaining(String searchKeyword);

	//내가 작성한 댓글(마이 페이지)
	public Page<ReplyEntity> findByEmailAndIsUse(String email, String isUse, PageRequest pageRequest);
	
}