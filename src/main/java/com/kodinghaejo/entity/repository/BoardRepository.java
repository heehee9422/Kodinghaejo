package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.MemberEntity;

import jakarta.transaction.Transactional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	//사용가능(isUse -> Y) 상태인 게시물 전체
	public List<BoardEntity> findByIsUseOrderByRegdateDesc(String isUse);
	
	//게시물 조회수 증가 --> Native SQL 
	@Transactional
	@Modifying
	@Query(value="UPDATE jpa_board SET hit_cnt = COALESCE(hit_cnt, 0) + 1 WHERE idx = :idx", nativeQuery = true)
	public void hitno(@Param("idx") Long idx);
	
	//카테고리 제외
	List<BoardEntity> findByCatNot(String cat);
	
	//카테고리
	List<BoardEntity> findByCat(String cat);
	
	//검색
	List<BoardEntity> findByTitleContainingAndCat(String searchKeyword, String category);
		
	//공지사항 검색
	List<BoardEntity> findByTitleContainingAndCatNot(String searchKeyword, String category);
	
	/*
	// 특정 페이지에 해당하는 게시글 목록 조회
	@Query("SELECT b FROM board b ORDER BY b.regdate DESC")
	List<BoardEntity> findBoards(@Param("offset") int offset, @Param("limit") int limit);
	
	// 전체 게시글 수 조회
	@Query("SELECT COUNT(b) FROM board b")
	int countAllBoards();
	*/

	//마이페이지 -> 본인이 작성한 게시글 확인(사용가능 상태인 걸로)
	public Page<BoardEntity> findByEmailAndIsUse(MemberEntity email, String isUse, Pageable pageable);

}