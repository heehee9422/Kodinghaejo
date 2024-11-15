package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

	public List<ReplyInterface> findByPrntIdxAndIsUse(Long prntIdx, String isUse);

	public List<ReplyEntity> findByPrntIdx(Long prntIdx);

	@Query("SELECT COUNT(r) FROM reply r WHERE r.prntIdx = :prntIdx AND r.isUse = 'Y'")
	int countRepliesByPostId(@Param("prntIdx") Long prntIdx);
	
	List<ReplyEntity> findByContentContaining(String searchKeyword);

	//내가 작성한 댓글 확인
	//마이페이지(페이징)
	public Page<ReplyEntity> findByEmailAndIsUse(MemberEntity email, String isUse, PageRequest pageRequest);
	//탈퇴 전 확인
	public List<ReplyEntity> findByEmailAndIsUse(MemberEntity email, String isUse);
	
}