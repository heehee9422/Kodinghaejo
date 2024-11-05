package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.TestSubmitEntity;

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
public class TestSubmitDTO {

	private Long idx;
	private TestLngEntity tlIdx;
	private MemberEntity email;
	private String submSts;
	private String content;
	private LocalDateTime regdate;

	//Entity --> DTO 이동
	public TestSubmitDTO(TestSubmitEntity entity) {
		this.idx = entity.getIdx();
		this.tlIdx = entity.getTlIdx();
		this.email = entity.getEmail();
		this.submSts = entity.getSubmSts();
		this.content = entity.getContent();
		this.regdate = entity.getRegdate();
	}

	//DTO --> Entity 이동
	public TestSubmitEntity dtoToEntity(TestSubmitDTO dto) {
		TestSubmitEntity entity = TestSubmitEntity
									.builder()
									.idx(dto.getIdx())
									.tlIdx(dto.getTlIdx())
									.email(dto.getEmail())
									.submSts(dto.getSubmSts())
									.content(dto.getContent())
									.regdate(dto.getRegdate())
									.build();

		return entity;
	}

}