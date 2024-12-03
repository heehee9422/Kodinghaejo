package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.BoardRecommendEntity;
import com.kodinghaejo.entity.BoardRecommendEntityID;
import com.kodinghaejo.entity.MemberEntity;

public interface BoardRecommendRepository extends JpaRepository<BoardRecommendEntity, BoardRecommendEntityID> {

	//게시글의 추천 수
	public Long countByBoardIdxAndGoodChk(BoardEntity boardEntity, String goodChk);
	public Long countByBoardIdxAndGoodChkAndIsUse(BoardEntity boardEntity, String goodChk, String isUse);

	//게시글의 신고 수
	public Long countByBoardIdxAndBadChk(BoardEntity boardEntity, String badChk);
	public Long countByBoardIdxAndBadChkAndIsUse(BoardEntity boardEntity, String badChk, String isUse);

	//좋아요 상태 확인
	public Long countByEmailAndBoardIdxAndGoodChkAndIsUse(MemberEntity email, BoardEntity boardIdx, String goodChk, String isUse);

	//신고 여부 확인
	public Long countByEmailAndBoardIdxAndBadChkAndIsUse(MemberEntity email, BoardEntity boardIdx, String badChk, String isUse);

	//사용자 이메일과 사용여부로 조회
	public List<BoardRecommendEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

}