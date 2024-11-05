package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestQuestionEntity;

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
public class TestQuestionDTO {

	private Long idx;
	private TestEntity testIdx;
	private MemberEntity email;
	private String writer;
	private String title;
	private String content;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public TestQuestionDTO(TestQuestionEntity entity) {
		this.idx = entity.getIdx();
		this.testIdx = entity.getTestIdx();
		this.email = entity.getEmail();
		this.writer = entity.getWriter();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public TestQuestionEntity dtoToEntity(TestQuestionDTO dto) {
		TestQuestionEntity entity = TestQuestionEntity
										.builder()
										.idx(dto.getIdx())
										.testIdx(dto.getTestIdx())
										.email(dto.getEmail())
										.writer(dto.getWriter())
										.title(dto.getTitle())
										.content(dto.getContent())
										.regdate(dto.getRegdate())
										.isUse(dto.getIsUse())
										.build();

		return entity;
	}

}