package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLangEntity;

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
public class TestLangDTO {

	private Long idx;
	private TestEntity tIdx;
	private String lang;
	private String content;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public TestLangDTO(TestLangEntity entity) {
		this.idx = entity.getIdx();
		this.tIdx = entity.getTIdx();
		this.lang = entity.getLang();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public TestLangEntity dtoToEntity(TestLangDTO dto) {
		TestLangEntity entity = TestLangEntity
									.builder()
									.idx(dto.getIdx())
									.tIdx(dto.getTIdx())
									.lang(dto.getLang())
									.content(dto.getContent())
									.regdate(dto.getRegdate())
									.isUse(dto.getIsUse())
									.build();

		return entity;
	}

}