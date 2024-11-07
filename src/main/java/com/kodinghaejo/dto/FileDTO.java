package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.FileEntity;
import com.kodinghaejo.entity.MemberEntity;

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
public class FileDTO {

	private Long idx;
	private String filePrnt;
	private Long prntIdx;
	private MemberEntity email;
	private String type;
	private String orgFile;
	private String storedFile;
	private Long fileSize;
	private LocalDateTime regdate;
	private String isUse;

	//Entity --> DTO 이동
	public FileDTO(FileEntity entity) {
		this.idx = entity.getIdx();
		this.filePrnt = entity.getFilePrnt();
		this.prntIdx = entity.getPrntIdx();
		this.email = entity.getEmail();
		this.type = entity.getType();
		this.orgFile = entity.getOrgFile();
		this.storedFile = entity.getStoredFile();
		this.fileSize = entity.getFileSize();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public FileEntity dtoToEntity(FileDTO dto) {
		FileEntity entity = FileEntity
								.builder()
								.idx(dto.getIdx())
								.filePrnt(dto.getFilePrnt())
								.prntIdx(dto.getPrntIdx())
								.email(dto.getEmail())
								.type(dto.getType())
								.orgFile(dto.getOrgFile())
								.storedFile(dto.getStoredFile())
								.fileSize(dto.getFileSize())
								.regdate(dto.getRegdate())
								.isUse(dto.getIsUse())
								.build();

		return entity;
	}

}