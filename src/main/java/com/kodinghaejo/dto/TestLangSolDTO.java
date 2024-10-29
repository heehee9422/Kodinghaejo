package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.TestLangEntity;
import com.kodinghaejo.entity.TestLangSolEntity;

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
public class TestLangSolDTO {

	private Long idx;
	private TestLangEntity tlIdx;
	private String content;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public TestLangSolDTO(TestLangSolEntity entity) {
		this.idx = entity.getIdx();
		this.tlIdx = entity.getTlIdx();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public TestLangSolEntity dtoToEntity(TestLangSolDTO dto) {
		TestLangSolEntity entity = TestLangSolEntity
									.builder()
									.idx(dto.getIdx())
									.tlIdx(dto.getTlIdx())
									.content(dto.getContent())
									.regdate(dto.getRegdate())
									.isUse(dto.getIsUse())
									.build();

		return entity;
	}

}