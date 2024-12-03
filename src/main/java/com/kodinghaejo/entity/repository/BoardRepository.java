package com.kodinghaejo.entity.repository;

import java.time.LocalDateTime;
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
	public List<BoardEntity> findByCatNotAndIsUseOrderByIdxDesc(String cat, String isUse);

	//게시물 조회수 증가 --> Native SQL
	@Transactional
	@Modifying
	@Query(value="UPDATE jpa_board SET hit_cnt = COALESCE(hit_cnt, 0) + 1 WHERE idx = :idx", nativeQuery = true)
	public void hitno(@Param("idx") Long idx);

	//카테고리 제외
	Page<BoardEntity> findByCatNot(String cat, Pageable pageable);

	//카테고리
	Page<BoardEntity> findByCat(String cat, Pageable pageable);

	//특정 카테고리검색
	Page<BoardEntity> findByTitleContainingAndCat(String searchKeyword, String category, Pageable pageable);

	//특정 카테고리제외 검색
	Page<BoardEntity> findByTitleContainingAndCatNot(String searchKeyword, String category, Pageable pageable);

	//카테고리별 일별 게시글 수
	public long countByCatNotAndRegdateBetween(String cat, LocalDateTime start, LocalDateTime end);

	//본인이 작성한 게시글 확인
	//마이페이지(페이징)
	public Page<BoardEntity> findByEmailAndIsUse(MemberEntity email, String isUse, Pageable pageable);
	//탈퇴 전 확인
	public List<BoardEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

	//등록일 기준으로 공지사항 출력
	//@Query("SELECT b FROM board b WHERE b.cat = 'CAT-0001' ORDER BY b.regdate DESC")
	public List<BoardEntity> findByCatOrderByRegdateDesc(String cat, Pageable pageable);

}