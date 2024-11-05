package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.TestBookmarkEntity;
import com.kodinghaejo.entity.TestEntity;

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
public class TestBookmarkDTO {

	private MemberEntity email;
	private TestEntity testIdx;
	private String addChk;
	private LocalDateTime addDate;

	//Entity --> DTO 이동
	public TestBookmarkDTO(TestBookmarkEntity entity) {
		this.email = entity.getEmail();
		this.testIdx = entity.getTestIdx();
		this.addChk = entity.getAddChk();
		this.addDate = entity.getAddDate();
	}

	//DTO --> Entity 이동
	public TestBookmarkEntity dtoToEntity(TestBookmarkDTO dto) {
		TestBookmarkEntity entity = TestBookmarkEntity
										.builder()
										.email(dto.getEmail())
										.testIdx(dto.getTestIdx())
										.addChk(dto.getAddChk())
										.addDate(dto.getAddDate())
										.build();

		return entity;
	}

}