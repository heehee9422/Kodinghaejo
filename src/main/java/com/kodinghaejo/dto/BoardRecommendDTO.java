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
	private BoardEntity bIdx;
	private String goodChk;
	private String badChk;
	private LocalDateTime goodDate;
	private LocalDateTime badDate;

	//Entity --> DTO 이동
	public BoardRecommendDTO(BoardRecommendEntity entity) {
		this.email = entity.getEmail();
		this.bIdx = entity.getBIdx();
		this.goodChk = entity.getGoodChk();
		this.badChk = entity.getBadChk();
		this.goodDate = entity.getGoodDate();
		this.badDate = entity.getBadDate();
	}

	//DTO --> Entity 이동
	public BoardRecommendEntity dtoToEntity(BoardRecommendDTO dto) {
		BoardRecommendEntity entity = BoardRecommendEntity
										.builder()
										.email(dto.getEmail())
										.bIdx(dto.getBIdx())
										.goodChk(dto.getGoodChk())
										.badChk(dto.getBadChk())
										.goodDate(dto.getGoodDate())
										.badDate(dto.getBadDate())
										.build();

		return entity;
	}

}