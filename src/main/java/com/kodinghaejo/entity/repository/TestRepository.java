package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
	
	List<TestEntity> findByTitleContaining(String searchKeyword);

}