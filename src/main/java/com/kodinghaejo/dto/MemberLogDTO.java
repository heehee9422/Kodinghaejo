package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.MemberLogEntity;

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
public class MemberLogDTO {
	
	private MemberEntity email;
	private LocalDateTime regdate;
	private String status;
	
	//Entity --> DTO 이동
	public MemberLogDTO(MemberLogEntity entity) {
		this.email = entity.getEmail();
		this.regdate = entity.getRegdate();
		this.status = entity.getStatus();
	}
	
	//DTO --> Entity 이동
	public MemberLogEntity dtoToEntity(MemberLogDTO dto) {
		MemberLogEntity entity = MemberLogEntity
									.builder()
									.email(dto.getEmail())
									.regdate(dto.getRegdate())
									.status(dto.getStatus())
									.build();
		
		return entity;
	}

}