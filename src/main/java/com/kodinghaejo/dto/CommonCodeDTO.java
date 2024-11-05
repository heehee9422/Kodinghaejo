package com.kodinghaejo.dto;

import com.kodinghaejo.entity.CommonCodeEntity;

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
public class CommonCodeDTO {

	private String code;
	private String type;
	private String val;
	private String note;
	private String isUse;

	//Entity --> DTO 이동
	public CommonCodeDTO(CommonCodeEntity entity) {
		this.code = entity.getCode();
		this.type = entity.getType();
		this.val = entity.getVal();
		this.note = entity.getNote();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public CommonCodeEntity dtoToEntity(CommonCodeDTO dto) {
		CommonCodeEntity entity = CommonCodeEntity
									.builder()
									.code(dto.getCode())
									.type(dto.getType())
									.val(dto.getVal())
									.note(dto.getNote())
									.isUse(dto.getIsUse())
									.build();

		return entity;
	}

}