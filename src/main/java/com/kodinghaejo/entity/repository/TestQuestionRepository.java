package com.kodinghaejo.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestQuestionEntity;

public interface TestQuestionRepository extends JpaRepository<TestQuestionEntity, Long> {

	Page<TestQuestionEntity> findByTitleContaining(String searchKeyword, Pageable pageable);

	public List<TestQuestionEntity> findByEmailAndIsUse(MemberEntity email, String isUse);
	
	//모든 문제 문제정보랑 같이 가져오기 (isUse 가
	@Query("SELECT q FROM testQuestion q JOIN FETCH q.testIdx t WHERE q.isUse = 'Y' ORDER BY q.idx DESC")
	List<TestQuestionEntity> findAllWithTestInfo();

	//특정 문제에 속한 질문 가져오기
	@Query("SELECT q FROM testQuestion q WHERE q.testIdx.idx = :testIdx AND q.isUse = 'Y' ORDER BY q.idx DESC")
	List<TestQuestionEntity> findQuestionsByTestIdx(@Param("testIdx") Long testIdx);

	//모든 질문 개수
	@Query("SELECT COUNT(q) FROM testQuestion q WHERE q.isUse = 'Y'")
	long count();

	//특정 문제의 질문 개수
	@Query("SELECT COUNT(q) FROM testQuestion q WHERE q.testIdx.idx = :testIdx AND q.isUse = 'Y'")
	long countByTestIdx(@Param("testIdx") Long testIdx);

	//특정 이메일의 질문 가져오기
	@Query("SELECT q FROM testQuestion q WHERE q.email.email = :email AND q.isUse = 'Y' ORDER BY q.idx DESC")
	List<TestQuestionEntity> findByEmail(@Param("email") String email);

	//특정 이메일이 쓴 질문의 갯수
	@Query("SELECT COUNT(q) FROM testQuestion q WHERE q.email.email = :email AND q.isUse = 'Y'")
	long countByEmail(@Param("email") String email);

	//질문의 questionIdx를 이용해 문제 정보를 가져오기
	@Query("SELECT t FROM test t JOIN testQuestion q ON t.idx = q.testIdx.idx WHERE q.idx = :questionIdx")
	Optional<TestEntity> findTestByQuestionIdx(@Param("questionIdx") Long questionIdx);

}