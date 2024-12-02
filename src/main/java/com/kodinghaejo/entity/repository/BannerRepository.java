package com.kodinghaejo.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.BannerEntity;

public interface BannerRepository extends JpaRepository<BannerEntity, Long> {
	
	List<BannerEntity> findByIsUse(String isUse);
	
	List<BannerEntity> findByEndDateBeforeAndIsUse(LocalDateTime now, String isUse);

}
