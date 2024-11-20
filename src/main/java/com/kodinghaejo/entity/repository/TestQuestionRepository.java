package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestQuestionEntity;

public interface TestQuestionRepository extends JpaRepository<TestQuestionEntity, Long> {

	Page<TestQuestionEntity> findByTitleContaining(String searchKeyword, Pageable pageable);
	public List<TestQuestionEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

}