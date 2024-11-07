package com.kodinghaejo.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;

public interface TestLngRepository extends JpaRepository<TestLngEntity, Long> {
	
	//언어별 문제 가져오기(문제 인덱스, 언어코드로 SELECT)
	public Optional<TestLngEntity> findFirstByTestIdxAndLng(TestEntity testEntity, String lng);

	//문제에 대한 모든 언어 가져오기
	List<TestLngEntity> findByTestIdx(TestEntity testEntity);
	
}