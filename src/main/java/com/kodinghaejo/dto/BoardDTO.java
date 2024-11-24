package com.kodinghaejo.dto;

import java.time.LocalDateTime;

import com.kodinghaejo.entity.BoardEntity;
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
public class BoardDTO {

	private Long idx;
	private String cat;
	private MemberEntity email;
	private String writer;
	private String title;
	private String content;
	private int hitCnt;
	private LocalDateTime regdate;
	private String isUse;

	private String catName; //카테고리 이름
	private Long replyCnt; //해당 게시물의 댓글 수
	private Long goodCnt; //해당 게시물의 추천 수
	private Long badCnt; //해당 게시물의 신고 수
	private String goodChk; //해당 게시물의 추천여부
	private String badChk; //해당 게시물의 신고여부

	//Entity --> DTO 이동
	public BoardDTO(BoardEntity entity) {
		this.idx = entity.getIdx();
		this.cat = entity.getCat();
		this.email = entity.getEmail();
		this.writer = entity.getWriter();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.hitCnt = entity.getHitCnt();
		this.regdate = entity.getRegdate();
		this.isUse = entity.getIsUse();
	}

	//DTO --> Entity 이동
	public BoardEntity dtoToEntity(BoardDTO dto) {
		BoardEntity entity = BoardEntity
													.builder()
													.idx(dto.getIdx())
													.cat(dto.getCat())
													.email(dto.getEmail())
													.writer(dto.getWriter())
													.title(dto.getTitle())
													.content(dto.getContent())
													.hitCnt(dto.getHitCnt())
													.regdate(dto.getRegdate())
													.isUse(dto.getIsUse())
													.build();

		return entity;
	}

}