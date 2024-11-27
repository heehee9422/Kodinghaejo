package com.kodinghaejo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.MemberDTO;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.CommonCodeEntity;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.CommonCodeRepository;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.TestSubmitRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BaseServiceImpl implements BaseService {
	
	private final CommonCodeRepository commonCodeRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;
	private final TestSubmitRepository submitRepository;

	//공통코드 가져오기
	@Override
	public Map<String, Object> loadUsedCommonCode() {
		List<CommonCodeEntity> entities = commonCodeRepository.findByIsUseOrderByCodeAsc("Y");

		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> lvlList = new ArrayList<>();
		List<Map<String, Object>> tecList = new ArrayList<>();
		List<Map<String, Object>> jobList = new ArrayList<>();
		List<Map<String, Object>> lngList = new ArrayList<>();
		List<Map<String, Object>> catList = new ArrayList<>();

		for (CommonCodeEntity code : entities) {
			Map<String, Object> subdata = new HashMap<>();
			subdata.put("code", code.getCode());
			subdata.put("type", code.getType());
			subdata.put("val", code.getVal());
			subdata.put("note", code.getNote());
			subdata.put("isUse", code.getIsUse());

			switch (code.getType()) {
				case "LVL":
					lvlList.add(subdata);
					break;
				case "TEC":
					tecList.add(subdata);
					break;
				case "JOB":
					jobList.add(subdata);
					break;
				case "LNG":
					lngList.add(subdata);
					break;
				case "CAT":
					catList.add(subdata);
					break;
			}
		}

		data.put("lvl", lvlList);
		data.put("tec", tecList);
		data.put("job", jobList);
		data.put("lng", lngList);
		data.put("cat", catList);

		return data;
	}
	
	//등록일 기준 신규 공지
	public List<BoardEntity> getNewNotice(int count) {
		Pageable pageable = PageRequest.of(0, count);
			
		List<BoardEntity> newNotice = boardRepository.findByCatAndRegdate(pageable);
		
		return newNotice;
	}

	//랭킹 리스트
	public List<MemberDTO> memberRank(String kind) {

		List<MemberEntity> memberEntities = memberRepository.findAll();

		List<MemberDTO> memberDTOs = new ArrayList<>();

		for (MemberEntity member : memberEntities) {
			MemberDTO memberDTO = new MemberDTO(member);

			Long correctCount = submitRepository.countSubmitByEmail(member.getEmail());
			memberDTO.setCorrectCount(correctCount != null ? correctCount : 0);

			Long submitCount = submitRepository.countByEmail(member.getEmail());
			memberDTO.setSubmitCount(submitCount);

			double correctRate = (submitCount > 0) ? (correctCount * 100.0) / submitCount : 0;
			memberDTO.setCorrectRate(correctRate);

			memberDTOs.add(memberDTO);
		}

		if (kind.equals("Q")) {
			memberDTOs.sort((a, b) -> Long.compare(b.getCorrectCount(), a.getCorrectCount()));
		} else {
			memberDTOs.sort((a, b) -> {
				int scoreComparison = Long.compare(b.getScore(), a.getScore());
				if (scoreComparison == 0) {
					if (a.getScoredate() == null && b.getScoredate() == null) {
						return 0;
					} else if (a.getScoredate() == null) {
						return 1;
					} else if (b.getScoredate() == null) {
						return -1;
					} else {
						return b.getScoredate().compareTo(a.getScoredate()); //내림차순으로 비교
					}
				}
				
				return scoreComparison;
			});
		}
		return memberDTOs;
	}

	// 점수 등급
	public String calGrade(Long score) {
		if (score < 400) {
			return "아이언 1";
		} else if (score < 800) {
			return "아이언 2";
		} else if (score < 1000) {
			return "브론즈 1";
		} else if (score < 1200) {
			return "브론즈 2";
		} else if (score < 1400) {
			return "실버 1";
		} else if (score < 1600) {
			return "실버 2";
		} else if (score < 1800) {
			return "골드 1";
		} else if (score < 2000) {
			return "골드 2";
		} else if (score < 2200) {
			return "플래티넘";
		} else if (score < 2400) {
			return "다이아몬드";
		} else if (score < 2600) {
			return "마스터";
		} else if (score < 3000) {
			return "그랜드 마스터";
		} else {
			return "챌린저";
		}
	}

}
