package com.kodinghaejo.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.BoardRecommendEntity;
import com.kodinghaejo.entity.BoardRecommendEntityId;
import com.kodinghaejo.entity.MemberEntity;

public interface BoardRecommendRepository extends JpaRepository<BoardRecommendEntity, BoardRecommendEntityId> {

	//게시글의 추천 수
	public Long countByBoardIdxAndGoodChk(BoardEntity boardEntity, String goodChk);
	public Long countByBoardIdxAndGoodChkAndIsUse(BoardEntity boardEntity, String goodChk, String isUse);

	//게시글의 신고 수
	public Long countByBoardIdxAndBadChk(BoardEntity boardEntity, String badChk);
	public Long countByBoardIdxAndBadChkAndIsUse(BoardEntity boardEntity, String badChk, String isUse);

	//실제 컬럼명인 B_IDX로 좋아요 개수 조회
	@Query(value = "SELECT COUNT(*) FROM jpa_board_recommend WHERE board_idx = :boardIdx and good_chk = 'Y'", nativeQuery = true)
	long countByBoardIdx(@Param("boardIdx") Long boardIdx);

	//좋아요 상태 확인
//	@Query(value = "SELECT COUNT(*) FROM jpa_board_recommend WHERE email = :email AND board_idx = :boardIdx and good_chk = 'Y'", nativeQuery = true)
//	int countByEmailAndBoardIdx(@Param("email") String email, @Param("boardIdx") Long boardIdx);
	public Long countByEmailAndBoardIdxAndGoodChkAndIsUse(MemberEntity email, BoardEntity boardIdx, String goodChk, String isUse);

	//신고 상태 확인
	@Query(value =  "SELECT COUNT(*) jpa_board_recommend  WHERE email = :email AND board_idx = :boardIdx AND bad_Chk = 'Y'", nativeQuery = true)
	int countReportsByEmailAndBoardIdx(@Param("email") String email, @Param("boardIdx") Long boardIdx);

	//사용자 이메일과 사용여부로 조회
	public List<BoardRecommendEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

}