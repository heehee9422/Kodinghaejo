package com.kodinghaejo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;

public interface TestService {

	//코딩테스트 문제 목록
	public Page<TestDTO> getTestList(int pageNum, int postNum, String email, String keyword, String submSts, String lng, String diff);

	//코딩테스트 문제 가져오기
	public TestDTO loadTest(Long idx) throws Exception;

	//문제에서 선택 가능한 언어 확인
	public String lngAvlChk(Long idx, String lng) throws Exception;

	//코딩테스트 언어별 문제 가져오기
	public TestLngEntity loadTestLng(Long testIdx, String language) throws Exception;

	//코드 실행 및 제출 시 파일 생성
	public void createVerifyFiles(String mainSrc, String correctSrc) throws Exception;

	//코드 실행 및 제출 시 검증 처리
	public String testCode(String language, String filePath) throws Exception;

	//코드 제출 처리
	public void submitTest(Long tlIdx, String email, String submSts, String code);

	//가장 많이 풀어본 문제 가져오기
	public Long getMostPopularTest();

	//등록일 기준 신규 문제 가져오기(등록일 기준 최신 랜덤)
	public Long getNewTest(int count);

	//난이도가 0인 문제 가져오기(난이도0 기준 랜덤)
	public Long getRandomTest();

	//난이도별 문제 보기
	public List<TestDTO> getDiffTest();

	//문제 리스트 중 랜덤으로 하나 선택
	public TestEntity getRandomList(List<TestEntity> problem);

	//문제 리스트 보여주기
	public List<TestDTO> testAllList();

	//문제 검색
	public List<TestDTO> searchtestListByTitle(String searchKeyword);

}