package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.BannerEntity;

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
public class BannerDTO {
	private Long idx;
	private String name;
	private String img;
	private String url;
	private String description;
	private LocalDateTime startdate;
	private LocalDateTime enddate;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public BannerDTO(BannerEntity entity) {
		this.idx = entity.getIdx();
		this.name = entity.getName();
		this.img = entity.getImg();
		this.url = entity.getUrl();
		this.description = entity.getDesc();
		this.startdate = entity.getStartDate();
		this.enddate = entity.getEndDate();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public BannerEntity dtoToEntity(BannerDTO dto) {
		BannerEntity entity = BannerEntity
														.builder()
														.idx(dto.getIdx())
														.name(dto.getName())
														.img(dto.getImg())
														.url(dto.getUrl())
														.desc(dto.getDescription())
														.startDate(dto.getStartdate())
														.endDate(dto.getEnddate())
														.regdate(dto.getRegdate())
														.isUse(dto.getIsUse())
														.build();
		return entity;

	}
}
