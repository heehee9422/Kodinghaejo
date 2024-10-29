package com.kodinghaejo.dto;

import java.time.LocalDateTime;

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
public class TestDTO {

	private Long idx;
	private String title;
	private int diff;
	private String src;
	private String cat;
	private String descr;
	private String restr;
	private String exStr;
	private String exDescr;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public TestDTO(TestEntity entity) {
		this.idx = entity.getIdx();
		this.title = entity.getTitle();
		this.diff = entity.getDiff();
		this.src = entity.getSrc();
		this.cat = entity.getCat();
		this.descr = entity.getDescr();
		this.restr = entity.getRestr();
		this.exStr = entity.getExStr();
		this.exDescr = entity.getExDescr();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public TestEntity dtoToEntity(TestDTO dto) {
		TestEntity entity = TestEntity
								.builder()
								.idx(dto.getIdx())
								.title(dto.getTitle())
								.diff(dto.getDiff())
								.src(dto.getSrc())
								.cat(dto.getCat())
								.descr(dto.getDescr())
								.restr(dto.getRestr())
								.exStr(dto.getExStr())
								.exDescr(dto.getExDescr())
								.regdate(dto.getRegdate())
								.isUse(dto.getIsUse())
								.build();

		return entity;
	}

}