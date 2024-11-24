package com.kodinghaejo.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;

public interface TestLngRepository extends JpaRepository<TestLngEntity, Long> {

	//언어별 문제 가져오기(문제 인덱스, 언어코드로 SELECT)
	public Optional<TestLngEntity> findFirstByTestIdxAndLng(TestEntity testEntity, String lng);

	//문제에 대한 모든 언어 가져오기
	List<TestLngEntity> findByTestIdx(TestEntity testEntity);

	//testIdx와 lng로 testLngEntity 찾는 쿼리 메서드
	Optional<TestLngEntity> findByTestIdxAndLng(TestEntity testEntity, String lng);
	
	//문제 목록에서 언어 필터 적용
	public List<TestLngEntity> findByLng(String lng);

	//가장 많이 풀어본 test_idx값 출력
	@Query(value = "SELECT ts.test_idx " +
									"FROM jpa_test_lng ts " +
									"WHERE ts.idx = :tlIdx", 
			nativeQuery = true)
	public Long findTestIdxByTlIdx(@Param("tlIdx") Long tlIdx);

}