package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.BoardRecommendEntity;
import com.kodinghaejo.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRecommendDTO {

	private MemberEntity email;
	private BoardEntity boardIdx;
	private String goodChk;
	private LocalDateTime goodDate;

	//Entity --> DTO 이동
	public BoardRecommendDTO(BoardRecommendEntity entity) {
		this.email = entity.getEmail();
		this.boardIdx = entity.getBoardIdx();
		this.goodChk = entity.getGoodChk();
		this.goodDate = entity.getGoodDate();
	}

	//DTO --> Entity 이동
	public BoardRecommendEntity dtoToEntity(BoardRecommendDTO dto) {
		BoardRecommendEntity entity = BoardRecommendEntity
										.builder()
										.email(dto.getEmail())
										.boardIdx(dto.getBoardIdx())
										.goodChk(dto.getGoodChk())
										.goodDate(dto.getGoodDate())
										.build();

		return entity;
	}

}