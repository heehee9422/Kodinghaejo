package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestBookmarkEntity;
import com.kodinghaejo.entity.TestBookmarkEntityId;

public interface TestBookmarkRepository extends JpaRepository<TestBookmarkEntity, TestBookmarkEntityId> {

	//사용자 이메일과 사용여부로 북마크 목록 가져오기
	public List<TestBookmarkEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

}