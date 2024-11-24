package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;

public interface TestQuestionAnswerRepository extends JpaRepository<TestQuestionAnswerEntity, Long> {

	List<TestQuestionAnswerEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

	//질문에 해당하는 답변 가져오기
	@Query("SELECT a FROM testQuestionAnswer a WHERE a.tqIdx.idx = :questionIdx ORDER BY a.idx DESC")
	List<TestQuestionAnswerEntity> findByTqIdx(@Param("questionIdx") Long questionIdx);

	//답변 개수 가져오기
	@Query("SELECT COUNT(a) FROM testQuestionAnswer a WHERE a.tqIdx.idx = :questionIdx AND a.isUse = 'Y'")
	int countAnswerByTqIdx(@Param("questionIdx") Long questionIdx);

}