package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.TestDTO;
import com.kodinghaejo.dto.TestLngDTO;
import com.kodinghaejo.entity.TestEntity;
import com.kodinghaejo.entity.TestLngEntity;
import com.kodinghaejo.entity.repository.TestLngRepository;
import com.kodinghaejo.entity.repository.TestRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final TestRepository testRepository;
	private final TestLngRepository testLngRepository;
	
	//문제 작성
	@Override
	public void saveTestWrite(TestDTO testDTO) {
		TestEntity testEntity = testDTO.dtoToEntity(testDTO);
		testEntity.setRegdate(LocalDateTime.now());
		testRepository.save(testEntity);
		
		if (!testDTO.getTestLngList().isEmpty()) {
			for (TestLngDTO langDTO : testDTO.getTestLngList()) {
				TestLngEntity langEntity = langDTO.dtoToEntity(langDTO);
				langEntity.setTestIdx(testEntity);
				testLngRepository.save(langEntity);
			}
		}
	}
	
	//문제 보여주기
	@Override
	public List<TestDTO> testAllList() {
		List<TestEntity> testEntities = testRepository.findAll(); // 문제 목록 조회
		List<TestDTO> testDTOList = new ArrayList<>();
		
		for (TestEntity test : testEntities) {
			TestDTO testDTO = new TestDTO(test);  // 기존의 TestEntity 정보를 TestDTO로 변환
			
			// 해당 문제에 대한 언어 정보 조회
			List<TestLngEntity> testLangs = testLngRepository.findByTestIdx(test); // testIdx로 언어 정보 조회
			List<TestLngDTO> testLngDTOs = new ArrayList<>();
			
			for (TestLngEntity lang : testLangs) {
				TestLngDTO langDTO = new TestLngDTO();
				langDTO.setLng(lang.getLng());  // 언어 코드만 추가
				testLngDTOs.add(langDTO);  // 언어 DTO 목록에 추가
			}
			
			testDTO.setTestLngList(testLngDTOs);  // TestDTO에 언어 정보 세팅
			testDTOList.add(testDTO);  // 최종 리스트에 추가
		}
		
		return testDTOList;
	}

}