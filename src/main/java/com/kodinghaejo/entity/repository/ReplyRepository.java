package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

	public List<ReplyEntity> findByRePrntAndPrntIdxAndIsUse(String rePrnt, Long prntIdx, String isUse);
	public Page<ReplyEntity> findByRePrntAndPrntIdxAndIsUse(String rePrnt, Long prntIdx, String isUse, PageRequest pageRequest);

	public List<ReplyEntity> findByRePrntAndPrntIdx(String rePrnt, Long prntIdx);

	public List<ReplyEntity> findByPrntIdx(Long prntIdx);

	public Long countByRePrntAndPrntIdxAndIsUse(String rePrnt, Long prntIdx, String isUse);

	public Page<ReplyEntity> findByContentContaining(String searchKeyword, Pageable pageable);

	public Page<ReplyEntity> findByRePrnt(String rePrnt, Pageable pageable);

	//내가 작성한 댓글 확인
	//마이페이지(페이징)
	public Page<ReplyEntity> findByEmailAndIsUse(MemberEntity email, String isUse, PageRequest pageRequest);
	//탈퇴 전 확인
	public List<ReplyEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

}