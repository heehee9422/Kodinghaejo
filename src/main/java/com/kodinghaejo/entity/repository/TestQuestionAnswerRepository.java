package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;

public interface TestQuestionAnswerRepository extends JpaRepository<TestQuestionAnswerEntity, Long> {

	List<TestQuestionAnswerEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

}