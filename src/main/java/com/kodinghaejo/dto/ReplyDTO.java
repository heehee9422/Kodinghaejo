package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.ReplyEntity;

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
public class ReplyDTO {

	private Long idx;
	private String rePrnt;
	private Long prntIdx;
	private String email;
	private String writer;
	private String content;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public ReplyDTO(ReplyEntity entity) {
		this.idx = entity.getIdx();
		this.rePrnt = entity.getRePrnt();
		this.prntIdx = entity.getPrntIdx();
		this.email = entity.getEmail();
		this.writer = entity.getWriter();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public ReplyEntity dtoToEntity(ReplyDTO dto) {
		ReplyEntity entity = ReplyEntity
								.builder()
								.idx(dto.getIdx())
								.rePrnt(dto.getRePrnt())
								.prntIdx(dto.getPrntIdx())
								.email(dto.getEmail())
								.writer(dto.getWriter())
								.content(dto.getContent())
								.regdate(dto.getRegdate())
								.isUse(dto.getIsUse())
								.build();

		return entity;
	}

}