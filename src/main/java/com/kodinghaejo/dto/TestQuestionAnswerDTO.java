package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestQuestionAnswerEntity;
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
public class TestQuestionAnswerDTO {

	private Long idx;
	private TestQuestionEntity tqIdx;
	private MemberEntity email;
	private String writer;
	private String content;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public TestQuestionAnswerDTO(TestQuestionAnswerEntity entity) {
		this.idx = entity.getIdx();
		this.tqIdx = entity.getTqIdx();
		this.email = entity.getEmail();
		this.writer = entity.getWriter();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public TestQuestionAnswerEntity dtoToEntity(TestQuestionAnswerDTO dto) {
		TestQuestionAnswerEntity entity = TestQuestionAnswerEntity
											.builder()
											.idx(dto.getIdx())
											.tqIdx(dto.getTqIdx())
											.email(dto.getEmail())
											.writer(dto.getWriter())
											.content(dto.getContent())
											.regdate(dto.getRegdate())
											.isUse(dto.getIsUse())
											.build();

		return entity;
	}

}