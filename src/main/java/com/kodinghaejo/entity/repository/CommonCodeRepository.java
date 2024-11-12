package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.CommonCodeEntity;

public interface CommonCodeRepository extends JpaRepository<CommonCodeEntity, String> {

	public List<CommonCodeEntity> findByIsUse(String isUse);
	
}