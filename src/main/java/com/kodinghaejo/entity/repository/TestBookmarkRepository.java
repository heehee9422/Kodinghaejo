package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestBookmarkEntity;
import com.kodinghaejo.entity.TestBookmarkEntityID;

public interface TestBookmarkRepository extends JpaRepository<TestBookmarkEntity, TestBookmarkEntityID> {

	//사용자 이메일과 사용여부로 북마크 목록 가져오기
	public List<TestBookmarkEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

	//좋아요 상태 확인
	@Query(value = "SELECT COUNT(*) FROM jpa_test_bookmark WHERE email = :email AND test_idx = :testIdx and add_chk = 'Y'", nativeQuery = true)
	int countByEmailAndTestIdx(@Param("email") String email, @Param("testIdx") Long boardIdx);

}