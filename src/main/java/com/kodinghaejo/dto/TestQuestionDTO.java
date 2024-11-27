package com.kodinghaejo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.TestLngEntity;
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
	private TestLngEntity tlIdx;
	private MemberEntity email;
	private String writer;
	private String title;
	private String content;
	private LocalDateTime regdate;
	private String isUse;
	
	private Long answerCount;
	private String lngName;
	private String isNew;
	
	private List<ReplyEntity> reply;
	private List<TestQuestionAnswerDTO> answer;

	//Entity --> DTO 이동
	public TestQuestionDTO(TestQuestionEntity entity) {
		this.idx = entity.getIdx();
		this.tlIdx = entity.getTlIdx();
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
																	.tlIdx(dto.getTlIdx())
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