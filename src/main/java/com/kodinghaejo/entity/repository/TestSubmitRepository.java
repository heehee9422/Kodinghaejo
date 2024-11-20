package com.kodinghaejo.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestSubmitEntity;

public interface TestSubmitRepository extends JpaRepository<TestSubmitEntity, Long> {

	//마이페이지 - 회원 탈퇴 시
	public List<TestSubmitEntity> findByEmail(MemberEntity email);
	
	//========== 코딩테스트 문제 리스트 ==========
	
	public List<TestSubmitEntity> findByEmailAndSubmSts(MemberEntity email, String submSts);
	
	public Long countByTlIdx(TestLngEntity tlIdx);
	
	public Long countByTlIdxAndEmail(TestLngEntity tlIdx, MemberEntity email);

	public Long countByTlIdxAndSubmSts(TestLngEntity tlIdx, String submSts);

	public Long countByTlIdxAndEmailAndSubmSts(TestLngEntity tlIdx, MemberEntity email, String submSts);
	
	//========== 관리자 페이지 ==========
	
	public long countByRegdateBetween(LocalDateTime start, LocalDateTime end);
	
	@Query("SELECT COUNT(t) FROM testSubmit t WHERE t.tlIdx.testIdx.idx = :testIdx")
	long countByTestIdx(@Param("testIdx") Long testIdx);
	
	@Query("SELECT COUNT(t) FROM testSubmit t WHERE t.tlIdx.testIdx.idx = :testIdx AND t.submSts = :submSts")
	long countByTestIdxAndSubmSts(@Param("testIdx") Long testIdx,@Param("submSts") String submSts);
	
	@Query("SELECT t.tlIdx.lng, COUNT(t) FROM testSubmit t JOIN t.tlIdx tl WHERE tl.lng IN ('LNG-0001', 'LNG-0002') GROUP BY t.tlIdx.lng")
	List<Object[]> countSubmitByLng();

}