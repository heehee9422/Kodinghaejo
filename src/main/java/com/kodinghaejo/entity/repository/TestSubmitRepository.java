package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestSubmitEntity;

public interface TestSubmitRepository extends JpaRepository<TestSubmitEntity, Long> {

	public List<TestSubmitEntity> findByEmail(MemberEntity email);
	
}