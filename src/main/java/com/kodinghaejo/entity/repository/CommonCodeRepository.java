package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.CommonCodeEntity;

import jakarta.transaction.Transactional;

public interface CommonCodeRepository extends JpaRepository<CommonCodeEntity, String> {

	public List<CommonCodeEntity> findByIsUseOrderByCodeAsc(String isUse);
	
	public Page<CommonCodeEntity> findByCodeContaining(String SearchKeyword, Pageable pageable);
	
	public Page<CommonCodeEntity> findByType(String type, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM commonCode c WHERE c.code = :code")
	public int deleteByCode(@Param("code") String code);

}