package com.kodinghaejo.service;

import java.util.List;
import java.util.Map;

import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.entity.BoardEntity;

public interface BaseService {

	//공통코드 가져오기
	public Map<String, Object> loadUsedCommonCode();

	//등록일 기준 신규 공지
	public List<BoardEntity> getNewNotice(int count);

	//랭킹 리스트
	public List<MemberDTO> memberRank(String kind);

	//랭킹 등급
	public String calGrade(Long score);

}